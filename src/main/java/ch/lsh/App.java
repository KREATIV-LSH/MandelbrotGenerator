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
        
        // long startTime = System.currentTimeMillis();

        // GrayCanvas canvas = RenderManager.simpleRender(16_000, 100, 16, 10, true);

        // long elapsedTime = System.currentTimeMillis() - startTime;
        // System.out.println("Elapsed Time: " + (elapsedTime / 1000f) + "s");
        // SaveUtil.saveGrayCanvas(canvas, new File("/home/lsh/Desktop/" + canvas.getWidth() + ".threading.pgm"));

        // for(int y = 0; y < canvas.getHeight(); y++) {
        // for(int x = 0; x < canvas.getWidth(); x++) {
        // Complex z = new Complex(xc - size/2 + size*x/canvas.getWidth(), yc - size/2 +
        // size*y/canvas.getHeight());
        // canvas.setPixel(x, y, max - mandelbrot(z, max));
        // }
        // if(y % 10 == 0) showStatus(y,canvas.getHeight(), startTime);
        // }
    }


}
