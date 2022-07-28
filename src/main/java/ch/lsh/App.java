package ch.lsh;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import org.apache.commons.math3.complex.Complex;

import ch.lsh.picture.GrayCanvas;
import ch.lsh.picture.PictureException;
import ch.lsh.picture.SaveUtil;
import edu.princeton.cs.introcs.Picture;

/**
 * @author Luis Hutterli
 *
 */
public class App 
{
        // return number of iterations to check if c = a + ib is in Mandelbrot set
        public static int mandelbrot(Complex z0, int max) {
            Complex z = z0;
            for (int t = 0; t < max; t++) {
                if (z.abs() > 2.0) return t;
                z = z.multiply(z).add(z0);
            }

            return max;
        }
    
        public static void main(String[] args) throws PictureException  {
            // Random random = new Random();

            // int size = (int) 50_000;
            // GrayCanvas canvas = new GrayCanvas(size, size);
            // for(int y = 0; y < canvas.getHeight(); y++) {
            //     for(int x = 0; x < canvas.getWidth(); x++) {
            //         int val = random.nextInt(255);
            //         canvas.setPixel(x, y, val);
            //     }
            // }
            // System.out.println("finished creating");

            // SaveUtil.saveGrayCanvas(canvas, new File("/home/lsh/Desktop/canvas_test.pgm"));

            // BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            // args = bf.readLine().split(" ");

            double xc   = -0.5d;
            double yc   = 0d;
            double size = 2d;
    
            int n   = 46_340;   // create n-by-n image
            int max = 100;   // maximum number of iterations
    
            long startTime = System.currentTimeMillis();

            GrayCanvas canvas = new GrayCanvas(n, n, max);
            for (int i = 0; i < n; i++) {
                if(i % 10 == 0) System.out.println(i + "/" + n + " " + (((float)i)/n*100) + "%");
                for (int j = 0; j < n; j++) {
                    double x0 = xc - size/2 + size*i/n;
                    double y0 = yc - size/2 + size*j/n;
                    Complex z0 = new Complex(x0, y0);
                    int gray = max - mandelbrot(z0, max);
                    // Color color = new Color(gray, gray, gray);
                    // picture.set(i, n-1-j, color);
                    canvas.setPixel(i, j, gray);
                }
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Elapsed Time: " + (elapsedTime/1000f) + "s");
            SaveUtil.saveGrayCanvas(canvas, new File("/home/lsh/Desktop/" + n + ".pgm"));
        }
}
