package ch.lsh;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.math3.complex.Complex;

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
    
        public static void main(String[] args)  {
            // BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            // args = bf.readLine().split(" ");

            double xc   = Double.parseDouble(args[0]);
            double yc   = Double.parseDouble(args[1]);
            double size = Double.parseDouble(args[2]);
    
            int n   = 46340;   // create n-by-n image
            int max = 255;   // maximum number of iterations
    
            long startTime = System.currentTimeMillis();

            Picture picture = new Picture(n, n);
            for (int i = 0; i < n; i++) {
                if(i % 10 == 0) System.out.println(i + "/" + n + " " + (((float)i)/n*100) + "%");
                for (int j = 0; j < n; j++) {
                    double x0 = xc - size/2 + size*i/n;
                    double y0 = yc - size/2 + size*j/n;
                    Complex z0 = new Complex(x0, y0);
                    int gray = max - mandelbrot(z0, max);
                    Color color = new Color(gray, gray, gray);
                    picture.set(i, n-1-j, color);
                }
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Elapsed Time: " + (elapsedTime/1000f) + "s");

            picture.save(new File("/home/lsh/Desktop/" + n + ".png"));

            // picture.show();
        }
}
