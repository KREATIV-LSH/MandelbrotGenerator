package ch.lsh.gui.mandelbrot;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.apache.commons.math3.util.Precision;

import ch.lsh.picture.GrayCanvas;
import ch.lsh.picture.PictureException;
import ch.lsh.rendering.MandelbrotThread;
import ch.lsh.rendering.RenderManager;

public class MandelbrotDisplay extends JPanel {

    private BufferedImage image;
    private WritableRaster raster;

    private double xOffset = -.5;
    private double yOffset;
    private double size = 2;

    private boolean working = false;

    private boolean benchmarkingModeEnable = false;
    
    private long lastFrameTime = 0;

    public MandelbrotDisplay(int width, int height) {

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (!benchmarkingModeEnable) {
                    double notches = e.getWheelRotation();
                    if (notches < 0) {
                        size /= 1.5;
                    } else {
                        size *= 1.5;
                    }
                    showPosStats();
                    displayMandelbrot();
                }
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!benchmarkingModeEnable) {
                    double mouseX = e.getPoint().getX();
                    double mouseY = e.getPoint().getY();

                    double idkX = mouseX - (raster.getWidth() / 2f);
                    double idkY = mouseY - (raster.getHeight() / 2f);

                    xOffset += (map(idkX, -(raster.getWidth() / 2), (raster.getWidth() / 2), -0.5, 0.5)) * size;
                    yOffset += (map(idkY, -(raster.getHeight() / 2), (raster.getHeight() / 2), -0.5, 0.5)) * size;
                    showPosStats();
                    displayMandelbrot();
                }
            }
        });

        image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        raster = image.getRaster();
    }

    public void enableBenchmarking() {
        benchmarkingModeEnable = true;
    }

    private void showPosStats() {
        System.out.print("X: " + Precision.round(xOffset, 5) + " Y:" + Precision.round(yOffset, 5) + " Size: "
                + size);
        if(lastFrameTime > 0) {
            System.out.print(" Frame time:" + lastFrameTime + " FPS:" + Precision.round(1000f / lastFrameTime, 2));
        }
        System.out.println();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(image, null, null);
    }

    public void displayMandelbrot() {
        if (working)
            return;
        working = true;
        
        long start = System.currentTimeMillis();

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                int pollingRate = 2;
                int linesPerThread = 8;
                int threadNum = 16;

                if (raster.getWidth() % linesPerThread != 0)
                    try {
                        throw new PictureException(
                                "Size has to be a multiple of linesPerThread(lpt): n=x*lpt or lpt%n=0");
                    } catch (PictureException e) {
                        e.printStackTrace();
                    }

                ArrayList<MandelbrotThread> threads = new ArrayList<>();
                int next_y = 0;
                int index = 0;
                int completed = 0;
                while (next_y < raster.getWidth()) {
                    if (threads.size() < threadNum) {
                        MandelbrotThread th = new MandelbrotThread(next_y, next_y + linesPerThread, 0,
                                raster.getWidth(), 255, raster.getWidth(),
                                raster.getWidth(), xOffset, yOffset, size);
                        th.start();
                        threads.add(th);
                        next_y += linesPerThread;
                    }
                    if (index % pollingRate == 0) {
                        for (int i = 0; i < threads.size(); i++) {
                            if (!threads.get(i).isAlive()) {
                                MandelbrotThread th = threads.get(i);
                                threads.remove(i);
                                int[][] results = th.getResult();

                                for (int y = th.getY_start(); y < th.getY_finish(); y++) {
                                    for (int x = 0; x < raster.getWidth(); x++) {
                                        // canvas.setPixel(x, y, results[y-th.getY_start()][x]);
                                        raster.setSample(x, y, 0, results[y - th.getY_start()][x]);
                                    }
                                    repaint();
                                }
                                completed += linesPerThread;
                                // if(debug) showStatus(completed, raster.getWidth());
                            }
                        }
                    }

                    index++;
                }

                for (int i = 0; i < threads.size(); i++) {
                    MandelbrotThread th = threads.get(i);
                    try {
                        th.join();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    int[][] results = th.getResult();
                    for (int y = th.getY_start(); y < th.getY_finish(); y++) {
                        for (int x = 0; x < raster.getWidth(); x++) {
                            raster.setSample(x, y, 0, results[y - th.getY_start()][x]);
                            repaint();
                        }
                    }
                    completed += linesPerThread;
                    // if(debug) RenderManager.showStatus(completed, raster.getWidth());
                }
            }
        });

        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        long stopTime = System.currentTimeMillis();

        lastFrameTime = stopTime - start;

        working = false;
    }

    private void drawCords() {
        for (int x = 0; x < raster.getWidth(); x++) {
            raster.setSample(x, (raster.getHeight() / 2) - 1, 0, 256 / 2);
            raster.setSample(x, (raster.getHeight() / 2), 0, 256 / 2);
            raster.setSample(x, (raster.getHeight() / 2) + 1, 0, 256 / 2);
        }
        for (int y = 0; y < raster.getHeight(); y++) {
            raster.setSample((raster.getWidth() / 2) - 1, y, 0, 256 / 2);
            raster.setSample((raster.getWidth() / 2), y, 0, 256 / 2);
            raster.setSample((raster.getWidth() / 2) + 1, y, 0, 256 / 2);
        }
        repaint();
    }

    private double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public void fillCanvas(int c) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                raster.setSample(x, y, 0, c);
            }
        }
        repaint();
    }

}
