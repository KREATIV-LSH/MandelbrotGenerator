package ch.lsh.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.lsh.gui.mandelbrot.MandelbrotDisplay;

public class MainFrame {

    private static JFrame frame;
    private JPanel panel;
    public static MandelbrotDisplay display;
    
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
    }

    
    public void redraw() {
        display.repaint();
    }

    public void showwwwwdbg() {
        display.displayMandelbrot();
    }

}
