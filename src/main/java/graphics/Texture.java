package graphics;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

	private String filepath;
	private int textureID;

	private int width, height;

	private Texture(int id) {
		this.textureID = id;
		filepath = "==== Wrapper ====";
	}

	public Texture (String filepath) {
		this.filepath = filepath;

		// generate texture on GPU
		textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);

		// Set texture parameters
		// tile image in both directions
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		// When stretching the image, pixelate it
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

		// Also pixelate image when shrinking image
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		// Load image using STB
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		stbi_set_flip_vertically_on_load(true);
		ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

		if (image != null) {

			this.width = width.get(0);
			this.height = height.get(0);

			if (channels.get(0) == 3) {
				// RGB
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
			} else if (channels.get(0) == 4) {
				// RGBA
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
			} else {
				assert false : "[ERROR] Graphics.Texture - Unknown number of channels \"" + channels.get(0) + "\".";
			}
		} else {
			assert false : "[ERROR] Graphics.Texture - Could not load image \"" + filepath + "\".";
		}

		stbi_image_free(image);
	}

	public static Texture wrap(int id) {
		return new Texture(id);
	}

	public void bind () {
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	public void bindToSlot(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	public void unbind () {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getTextureID () {
		return textureID;
	}

	public String getFilePath () {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public void setId(int id) {
		this.textureID = id;
	}
}
