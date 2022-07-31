package ch.lsh.rendering;

import java.awt.image.WritableRaster;
import java.util.ArrayList;

import org.apache.commons.math3.util.Precision;

import ch.lsh.App;
import ch.lsh.gui.MainFrame;
import ch.lsh.picture.GrayCanvas;
import ch.lsh.picture.PictureException;

public class RenderManager {

    // Constructor to satisfy sonarlint S111
    private RenderManager() {
        throw new IllegalStateException("Utility class");
    }
    
    public static void mandelbrotDisplayRender(WritableRaster raster, int maxvalue, int threadNum, int linesPerThread, double xc, double yc, double size, boolean debug) throws PictureException, InterruptedException {
        int pollingRate = 5;

        if(raster.getWidth() % linesPerThread != 0) throw new PictureException("Size has to be a multiple of linesPerThread(lpt): n=x*lpt or lpt%n=0");        



        ArrayList<MandelbrotThread> threads = new ArrayList<>();
        int next_y = 0;
        int index = 0;
        int completed = 0;
        while (next_y < raster.getWidth()) {
            if (threads.size() < threadNum) {
                MandelbrotThread th = new MandelbrotThread(next_y, next_y + linesPerThread, 0, raster.getWidth(), maxvalue, raster.getWidth(),
                        raster.getWidth(), xc, yc, size);
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
                                raster.setSample(x, y, 0, results[y-th.getY_start()][x]);
                            }
                            App.frame.redraw();
                        }
                        completed += linesPerThread;
                        if(debug) showStatus(completed, raster.getWidth());
                    }
                }
            }

            index++;
        }

        for (int i = 0; i < threads.size(); i++) {
            MandelbrotThread th = threads.get(i);
            th.join();
            int[][] results = th.getResult();
            for (int y = th.getY_start(); y < th.getY_finish(); y++) {
                for (int x = 0; x < raster.getWidth(); x++) {
                    raster.setSample(x, y, 0, results[y-th.getY_start()][x]);
                }
            }
            completed += linesPerThread;
            if(debug) showStatus(completed, raster.getWidth());
        }

    }
    
    public static GrayCanvas simpleRender(int resolution, int maxvalue, int threadNum, int linesPerThread, boolean debug) throws PictureException, InterruptedException {
        double xc = -0.5d;
        double yc = 0d;
        double size = 2d;
        int pollingRate = 5;

        if(resolution % linesPerThread != 0) throw new PictureException("Size has to be a multiple of linesPerThread(lpt): n=x*lpt or lpt%n=0");        


        GrayCanvas canvas = new GrayCanvas(resolution, resolution, maxvalue);

        ArrayList<MandelbrotThread> threads = new ArrayList<>();
        int next_y = 0;
        int index = 0;
        int completed = 0;
        while (next_y < canvas.getHeight()) {
            if (threads.size() < threadNum) {
                MandelbrotThread th = new MandelbrotThread(next_y, next_y + linesPerThread, 0, canvas.getWidth(), maxvalue, canvas.getWidth(),
                        canvas.getHeight(), xc, yc, size);
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
                            for (int x = 0; x < canvas.getWidth(); x++) {
                                canvas.setPixel(x, y, results[y-th.getY_start()][x]);
                            }
                        }
                        completed += linesPerThread;
                        if(debug) showStatus(completed, canvas.getHeight());
                    }
                }
            }

            index++;
        }

        for (int i = 0; i < threads.size(); i++) {
            MandelbrotThread th = threads.get(i);
            th.join();
            int[][] results = th.getResult();
            for (int y = th.getY_start(); y < th.getY_finish(); y++) {
                for (int x = 0; x < canvas.getWidth(); x++) {
                    canvas.setPixel(x, y, results[y-th.getY_start()][x]);
                }
            }
            completed += linesPerThread;
            if(debug) showStatus(completed, canvas.getHeight());
        }

        return canvas;
    }

    private static void showStatus(int y, int height) {
        System.out.println(y + "/" + height + "\t" + Precision.round(((double) y) / height * 100d, 3) + "%" + "\t");
    }

}
