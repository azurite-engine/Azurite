package graphics.textures;

import java.nio.ByteBuffer;

public class ImageData {

    /** The data of the image */
    private final ByteBuffer data;
    /** The width of the image */
    private final int width;
    /** The height of the image */
    private final int height;
    /** The channels of the image (an OpenGl value, such as RGB, RGBA, ...) */
    private final int openglChannels;

    /**
     * Creates a helper which stores the data of an image
     * @param data buffer of data
     * @param width width of the image
     * @param height height of the image
     * @param openglChannels OpenGl value representing the image's channels
     */
    public ImageData(ByteBuffer data, int width, int height, int openglChannels) {
        this.data = data;
        this.width = width;
        this.height = height;
        this.openglChannels = openglChannels;
    }

    public ByteBuffer getData() {
        return data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOpenglChannels() {
        return openglChannels;
    }

}