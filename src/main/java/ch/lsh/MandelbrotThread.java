package ch.lsh;

import org.apache.commons.math3.complex.Complex;

public class MandelbrotThread extends Thread {

    private int y_start;
    private int y_finish;
    private int maxval;

    private int width;
    private int height;

    private double xc;
    private double yc;
    private double size;

    private int[][] result;

    public MandelbrotThread(int y_start, int y_finish, int maxval, int width, int height,
            double xc, double yc,
            double size) {
        this.y_start = y_start;
        this.y_finish = y_finish;
        this.maxval = maxval;
        this.width = width;
        this.height = height;
        this.xc = xc;
        this.yc = yc;
        this.size = size;

        result = new int[y_finish-y_start][width];
    }

    @Override
    public void run() {
        for (int y = y_start; y < y_finish; y++) {
            // System.out.println(y);
            for (int x = 0; x < width; x++) {
                Complex z = new Complex(xc - size / 2 + size * x / width, yc - size / 2 + size * y / height);
                result[y-y_start][x] = maxval - mandelbrot(z, maxval);
                // System.out.println(this.getName() + ": " + x + " " + y + " " + result[y][x]);
            }
        }
        super.run();
    }

    private static int mandelbrot(Complex z0, int max) {
        Complex z = z0;
        for (int t = 0; t < max; t++) {
            if (z.abs() > 2.0)
                return t;
            z = z.multiply(z).add(z0);
        }

        return max;
    }

    public int[][] getResult() {
        return result;
    }

    public int getY_start() {
        return y_start;
    }

    public int getY_finish() {
        return y_finish;
    }
}
