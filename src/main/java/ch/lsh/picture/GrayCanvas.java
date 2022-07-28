package ch.lsh.picture;

public class GrayCanvas {
    
    private byte[][] pixels;

    public GrayCanvas(int width, int height) {
        pixels = new byte[width][height];
    }

    public void setPixel(int x, int y, int grayColor) throws PictureException {
        if(grayColor >= 0 && grayColor > 255) throw new PictureException("Provided grayscale color has to be in range 0-255");
        pixels[x][y] = (byte) (grayColor-128);
    }

}
