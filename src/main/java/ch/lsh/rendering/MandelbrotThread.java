package ch.lsh.rendering;

import org.apache.commons.math3.complex.Complex;

public class MandelbrotThread extends Thread {

    private int y_start;
    private int y_finish;
    private int x_start;
    private int x_finish;


    private int maxval;

    private int width;
    private int height;

    private double xc;
    private double yc;
    private double size;

    private int[][] result;

    public MandelbrotThread(int y_start, int y_finish, int x_start, int x_finish, int maxval, int width, int height,
            double xc, double yc,
            double size) {
        this.y_start = y_start;
        this.y_finish = y_finish;
        this.x_start = x_start;
        this.x_finish = x_finish;

        this.maxval = maxval;
        this.width = width;
        this.height = height;
        this.xc = xc;
        this.yc = yc;
        this.size = size;

        result = new int[y_finish-y_start][x_finish-x_start];
    }

    @Override
    public void run() {
        for (int y = y_start; y < y_finish; y++) {
            for (int x = x_start; x < x_finish; x++) {
                Complex z = new Complex(xc - size / 2 + size * x / width, yc - size / 2 + size * y / height);
                result[y-y_start][x-x_start] = maxval - mandelbrot(z, maxval);
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

    public int getX_start() {
        return x_start;
    }

    public int getX_finish() {
        return x_finish;
    }
}
