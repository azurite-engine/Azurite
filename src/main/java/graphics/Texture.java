package graphics;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * This is a class for an OpenGL texture
 */
public class Texture {
	/**
	 * Filepath of this texture.
	 * If the instance is just a wrapper around an id (by using Texture.wrap()),
	 * 		this will be set to "==== Wrapper ===="
	 */
	private String filepath;

	/**
	 * The id of the texture
	 */
	private int textureID;

	/**
	 * Width and the height of the texture
	 */
	private int width;
	private int height;

	/**
	 * Wrap the given id into a texture object
	 *
	 * @param id the id to be wrapped
	 */
	private Texture(int id) {
		this.textureID = id;
		this.width = -1;
		this.height = -1;
		filepath = "==== Wrapper ====";
	}

	public Texture() {
		this.textureID  = glGenTextures();
	}

	/**
	 * Load a Texture from a filepath.
	 * Recommended to use Assets.loadTexture instead of calling this function
	 *
	 * @param filepath filepath of the texture
	 */
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

	/**
	 * Uploads image data with specified width and height.
	 *
	 * @param width  Width of the image
	 * @param height Height of the image
	 * @param data   Pixel data of the image
	 */
	public void uploadData(int width, int height, ByteBuffer data) {
		uploadData(GL_RGBA8, width, height, GL_RGBA, data);
	}

	/**
	 * Uploads image data with specified internal format, width, height and
	 * image format.
	 *
	 * @param internalFormat Internal format of the image data
	 * @param width          Width of the image
	 * @param height         Height of the image
	 * @param format         Format of the image data
	 * @param data           Pixel data of the image
	 */
	public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);
	}

	/**
	 * Creates a texture with specified width, height and data.
	 *
	 * @param width  Width of the texture
	 * @param height Height of the texture
	 * @param data   Picture Data in RGBA format
	 *
	 * @return Texture from the specified data
	 */
	public Texture createTexture(int width, int height, ByteBuffer data) {

		setWidth(width);
		setHeight(height);

		bind();

		// Set texture parameters
		// tile image in both directions
		setParameter(GL_TEXTURE_WRAP_S, GL_REPEAT);
		setParameter(GL_TEXTURE_WRAP_T, GL_REPEAT);

		// When stretching the image, pixelate it
		setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);

		// Also pixelate image when shrinking image
		setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		uploadData(GL_RGBA8, width, height, GL_RGBA, data);

		return this;

	}

	/**
	 * Sets a parameter of the texture.
	 *
	 * @param name  Name of the parameter
	 * @param value Value to set
	 */
	public void setParameter(int name, int value) {
		glTexParameteri(GL_TEXTURE_2D, name, value);
	}

	/**
	 * Wrap the given id into a texture object
	 * @param id the id to be wrapped
	 * @return the texture instance
	 */
	public static Texture wrap(int id) {
		return new Texture(id);
	}

	/**
	 * Bind this texture to the currently active texture slot
	 */
	public void bind () {
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	/**
	 * Bind this texture to a specific texture slot
	 * @param unit the texture unit to bind this texture to
	 */
	public void bindToSlot(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	/**
	 * Unbind the texture
	 */
	public void unbind () {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	/**
	 * Get This texture's width. Will be -1 if the instance is just a wrapper
	 *
	 * @return the width of the texture
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Get This texture's height. Will be -1 if the instance is just a wrapper
	 *
	 * @return the height of the texture
	 */
	public int getHeight() {
		return this.height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Get the texture's id.
	 *
	 * @return the id of the texture
	 */
	public int getTextureID () {
		return textureID;
	}

	/**
	 * The filepath from which this texture was loaded.
	 * Will return "==== Wrapper ====" if it is just a wrapper around an id.
	 *
	 * @return filepath from which the texture was loaded
	 */
	public String getFilePath () {
		return filepath;
	}

	public void setId(int id) {
		this.textureID = id;
	}
}
