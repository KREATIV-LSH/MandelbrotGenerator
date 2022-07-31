package ch.lsh.gui.mandelbrot;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

import ch.lsh.picture.GrayCanvas;
import ch.lsh.picture.PictureException;
import ch.lsh.rendering.RenderManager;

public class MandelbrotDisplay extends JPanel {

    private boolean needsReDraw;
    private BufferedImage image;
    private WritableRaster raster;
    private GrayCanvas mandelBrotCanvas;
    private Point mouseClicked;

    private double lastX;
    private double lastY;

    public MandelbrotDisplay(int width, int height) {

        // this.addMouseListener(new MouseAdapter() {
        //     public void mouseReleased(MouseEvent e) {
        //         mouseClicked = e.getPoint();
        //         needsReDraw = true;
        //         displayMandelbrot();
        //     }
        // });

        this.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseClicked = e.getPoint();
                // System.out.println(e);
                displayMandelbrot();
            }
            
        });

        lastX = -0.5d;
        lastY = 0d;

        needsReDraw = false;
        image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        raster = image.getRaster();

        fillCanvas(255);
    }

    public Dimension getPreferredSize() {
        return new Dimension(image.getWidth(), image.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(image, null, null);
    }

    private void displayMandelbrot() {
        try {
            double[] mousePos = mapMouse();
            RenderManager.mandelbrotDisplayRender(raster, 255, 4, 4, mousePos[0], mousePos[1], 2, false);
        } catch (PictureException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private double[] mapMouse() {
        double[] mousePos = new double[2];
        
        return mousePos;
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
