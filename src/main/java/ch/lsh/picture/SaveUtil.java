package ch.lsh.picture;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class SaveUtil {
    
    public static void saveGrayCanvas(GrayCanvas canvas, File file) {
        try {
            if(!file.exists()) {
                if(!file.createNewFile()) throw new Exception("File could not be created.");
                file.setWritable(true);
            }

            FileOutputStream fos = new FileOutputStream(file);
            Writer w = new BufferedWriter(new OutputStreamWriter(fos, "Cp850"));
            StringBuilder sb = new StringBuilder();

            w.write("P2\n");
            w.write(canvas.getWidth() + " " + canvas.getHeight() + "\n");
            w.write(canvas.getMaxval() + "\n");

            for(int y = 0; y < canvas.getHeight(); y++) {
                sb.setLength(0);
                for(int x = 0; x < canvas.getWidth(); x++) {
                    sb.append(canvas.getPixel(x, y));
                    sb.append(" ");
                }
                sb.append("\n");
                w.write(sb.toString());
            }
            w.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}
