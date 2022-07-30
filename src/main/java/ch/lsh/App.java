package ch.lsh;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.Precision;

import ch.lsh.picture.GrayCanvas;
import ch.lsh.picture.PictureException;
import ch.lsh.picture.SaveUtil;

/**
 * @author Luis Hutterli
 *
 */
public class App {
    // return number of iterations to check if c = a + ib is in Mandelbrot set
    public static int mandelbrot(Complex z0, int max) {
        Complex z = z0;
        for (int t = 0; t < max; t++) {
            if (z.abs() > 2.0)
                return t;
            z = z.multiply(z).add(z0);
        }

        return max;
    }

    public static void main(String[] args) throws PictureException, InterruptedException, IOException {

        double xc = -0.5d;
        double yc = 0d;
        double size = 2d;

        int n = 16_000; // create n-by-n image
        int max = 100; // maximum number of iterations

        int maxThreads = 20;
        int linesPerThread = 20;
        int pollingRate = 2;



        if(n % linesPerThread != 0) throw new PictureException("Size has to be a multiple of linesPerThread(lpt): n=x*lpt or lpt%n=0");        

        long startTime = System.currentTimeMillis();

        GrayCanvas canvas = new GrayCanvas(n, n, max);

        ArrayList<MandelbrotThread> threads = new ArrayList<>();
        int next_y = 0;
        int index = 0;
        int completed = 0;
        while (next_y < canvas.getHeight()) {
            if (threads.size() < maxThreads) {
                MandelbrotThread th = new MandelbrotThread(next_y, next_y + linesPerThread, max, canvas.getWidth(),
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

                        // System.out.println(Arrays.deepToString(results));
                        for (int y = th.getY_start(); y < th.getY_finish(); y++) {
                            for (int x = 0; x < canvas.getWidth(); x++) {
                                canvas.setPixel(x, y, results[y-th.getY_start()][x]);
                            }
                        }
                        completed += linesPerThread;
                        showStatus(completed, canvas.getHeight());
                    }
                }
            }

            index++;
        }
        System.out.println("Finished main loop");
        System.out.println("Collecting results...");

        for (int i = 0; i < threads.size(); i++) {
            MandelbrotThread th = threads.get(i);
            th.join();
            int[][] results = th.getResult();
            for (int y = th.getY_start(); y < th.getY_finish(); y++) {
                for (int x = 0; x < canvas.getWidth(); x++) {
                    canvas.setPixel(x, y, results[y-th.getY_start()][x]);
                }
            }
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed Time: " + (elapsedTime / 1000f) + "s");
        SaveUtil.saveGrayCanvas(canvas, new File("/home/lsh/Desktop/" + n + ".threading.pgm"));

        // for(int y = 0; y < canvas.getHeight(); y++) {
        // for(int x = 0; x < canvas.getWidth(); x++) {
        // Complex z = new Complex(xc - size/2 + size*x/canvas.getWidth(), yc - size/2 +
        // size*y/canvas.getHeight());
        // canvas.setPixel(x, y, max - mandelbrot(z, max));
        // }
        // if(y % 10 == 0) showStatus(y,canvas.getHeight(), startTime);
        // }
    }

    private static void showStatus(int y, int height) {
        System.out.println(y + "/" + height + "\t" + Precision.round(((double) y) / height * 100d, 3) + "%" + "\t");
    }
}
