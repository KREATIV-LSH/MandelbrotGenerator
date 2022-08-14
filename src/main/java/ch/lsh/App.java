package ch.lsh;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.fraction.Fraction;

import ch.lsh.gui.MainFrame;
import ch.lsh.picture.GrayCanvas;
import ch.lsh.picture.PictureException;
import ch.lsh.picture.SaveUtil;
import ch.lsh.rendering.RenderManager;

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

    public static MainFrame frame;

    public static void main(String[] args) throws PictureException, InterruptedException, IOException {
        frame = new MainFrame(1000, 1000);

        // Check if flag for benchmarking mode is set
         if(args.length > 0) {
            if(args[0].equalsIgnoreCase("bench")) {
                System.out.println("Benchmarking mode");
                frame.benchmark();
            }
         }
    }


}
