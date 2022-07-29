package ch.lsh.picture;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Optional;

import javax.imageio.ImageIO;

public class SaveUtil {

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf('.') != -1 && fileName.lastIndexOf('.') != 0) {
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        } else {
            return "File don't have extension";
        }
    }

    public static void saveGrayCanvas(GrayCanvas canvas, File file) {
        if (getFileExtension(file).equalsIgnoreCase("pgm")) {
            savePGMGrayCanvas(canvas, file);
        } else if (getFileExtension(file).equalsIgnoreCase("png")) {
            try {
                BufferedImage im = new BufferedImage(canvas.getWidth(), canvas.getHeight(),
                        BufferedImage.TYPE_BYTE_GRAY);

                
                WritableRaster raster = im.getRaster();

                for (int y = 0; y < canvas.getHeight(); y++) {
                    for (int x = 0; x < canvas.getWidth(); x++) {
                        raster.setSample(x, y, 0, canvas.getPixel(x, y));
                    }
                }
                FileOutputStream fos = new FileOutputStream(file);
                ImageIO.write(im, "png", fos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void savePGMGrayCanvas(GrayCanvas canvas, File file) {
        try {
            if (!file.exists()) {
                if (!file.createNewFile())
                    throw new Exception("File could not be created.");
                file.setWritable(true);
            }

            FileOutputStream fos = new FileOutputStream(file);
            Writer w = new BufferedWriter(new OutputStreamWriter(fos, "Cp850"));
            StringBuilder sb = new StringBuilder();

            w.write("P2\n");
            w.write(canvas.getWidth() + " " + canvas.getHeight() + "\n");
            w.write(canvas.getMaxval() + "\n");

            for (int y = 0; y < canvas.getHeight(); y++) {
                sb.setLength(0);
                for (int x = 0; x < canvas.getWidth(); x++) {
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
