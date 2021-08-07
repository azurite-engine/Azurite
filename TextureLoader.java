package graphics.textures;

import graphics.Color;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.stb.STBImage.*;

public class TextureLoader {

    private static final ImageData BACKUP = generateCheckerboard();

    /**
     * Loads an image from a filepath.
      * @param filepath the filepath to read the data from
     * @return the data of the loaded image, or a checkerboard-like image if an error occurs
     */
    public static ImageData loadTexture(String filepath) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            IntBuffer width = memoryStack.mallocInt(1);
            IntBuffer height = memoryStack.mallocInt(1);
            IntBuffer channels = memoryStack.mallocInt(1);

            stbi_set_flip_vertically_on_load(true);
            ByteBuffer data = stbi_load(filepath, width, height, channels, 0);

            if (data == null) {
                System.out.println("[FATAL] Failed to load image \"" + filepath + "\"!");
                System.out.println(stbi_failure_reason());
            } else {
                boolean hasTransparency = channels.get(0) == 4;
                // Creates the ImageData helper
                return new ImageData(data, width.get(0), height.get(0), hasTransparency ? GL_RGBA : GL_RGB);
            }

        }

        return BACKUP;
    }

    /**
     * Generates a backup image following a checkerboard pattern with beautiful, vibrant colours
     * @return the generated image
     */
    private static ImageData generateCheckerboard() {
        int size = 64;
        int channels = 3;
        byte[] data = new byte[size * size * channels];

        for (int y = 0; y < size; y ++) {
            for (int x = 0; x < size; x ++) {
                Color current = ((x / 8 + y / 8) % 2) == 0 ? Color.GREEN : Color.PINK;
                data[(x + (y * size)) * channels    ] = (byte) current.r;
                data[(x + (y * size)) * channels + 1] = (byte) current.g;
                data[(x + (y * size)) * channels + 2] = (byte) current.b;
            }
        }

        ByteBuffer dataBuffer = MemoryUtil.memAlloc(data.length);
        dataBuffer.put(data);
        dataBuffer.flip();

        return new ImageData(dataBuffer, size, size, channels);
    }

}
