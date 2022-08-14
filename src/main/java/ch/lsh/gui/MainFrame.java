package ch.lsh.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.math3.util.Precision;

import ch.lsh.gui.mandelbrot.MandelbrotDisplay;

public class MainFrame {

    private final JFrame frame;
    private JPanel panel;
    private final MandelbrotDisplay display;
    
    public MainFrame(int width, int height) {
        display = new MandelbrotDisplay(width, height);
        
        frame = new JFrame("Mandelbrot Generator by LSH");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width,height);
        frame.setResizable(false);
        panel = new JPanel();

        panel.add(display);

        frame.add(panel);
        frame.setVisible(true);

        display.displayMandelbrot();
    }

    public void benchmark() {
        display.enableBenchmarking();
        long startTime = System.currentTimeMillis();
        int completed = 0;
        int seconds = 120;
        while(true) {
            long elapsed = System.currentTimeMillis() - startTime;
            if(elapsed >= seconds*1000) {
                break;
            }

            display.fillCanvas(125);
            display.displayMandelbrot();
            completed++;

            System.out.println(Precision.round(elapsed / 1000d, 2) + "/" + seconds + "\t" + (Precision.round(elapsed / 100d / seconds, 4)) + "%");
        }
        System.out.println(completed + " frames in "+ (Precision.round((System.currentTimeMillis() - startTime) / 1000d, 2)) + "s\t" + Precision.round(completed / (float)seconds, 10));
        System.exit(0);
    }

}
