package ch.lsh.picture;

/**
 * @author Luis Hutterli
 */

public class GrayCanvas {
    
    private int width;
    private int height;
    private byte[][] pixels;

    public GrayCanvas(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new byte[this.height][this.width];
    }

    public void setPixel(int x, int y, int grayColor) throws PictureException {
        if(x < 0 || y < 0) throw new PictureException("Provided coordinates can not be smaller than zero: " + x + " " + y);
        if(x >= width || y >= height)  throw new PictureException("Provided coordinates for pixel do not exist: " + x + " " + y);
        if(grayColor >= 0 && grayColor > 255) throw new PictureException("Provided grayscale color has to be in range 0-255: " + grayColor);

        pixels[y][x] = (byte) (grayColor-128);
    }

    public int getPixel(int x, int y) throws PictureException {
        if(x < 0 || y < 0) throw new PictureException("Provided coordinates can not be smaller than zero: " + x + " " + y);
        if(x >= width || y >= height)  throw new PictureException("Provided coordinates for pixel do not exist: " + x + " " + y);

        return ((int) (pixels[y][x])) + 128;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
