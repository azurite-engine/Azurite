package ecs;

import graphics.Texture;
import org.joml.Vector2f;

/**
 * Contains the texture and UV coordinates used by openGL to render an image
 */

public class Sprite {

	protected Texture texture;

	protected Vector2f[] textureCoordinates;
	
	// This contains the default coordinates for an entire image UV
	protected Vector2f[] defaultTextureCoordinates = {
		new Vector2f(0, 0),
		new Vector2f(0, 1),
		new Vector2f(1, 1),
		new Vector2f(1, 0)
	};

	/**
	 * Construct a Sprite using custom texture coordinates (uv).
	 * @param texture
	 * @param uv
	 */
	public Sprite (Texture texture, Vector2f[] uv) {
		this.texture = texture;
		this.textureCoordinates = uv;
	}

	/**
	 * Construct a texture using default texture coordinates.
	 * @param texture
	 */
	public Sprite (Texture texture) {
		this.texture = texture;
		this.textureCoordinates = defaultTextureCoordinates;
	}

	public Texture getTexture () {
		return this.texture;
	}
	
	public Vector2f[] getTextureCoordinates () {
		return this.textureCoordinates;
	}

	public int getTextureID () {
		return texture.getTextureID();
	}

	public float getWidth () {
		return texture.getWidth();
	}

	public float getHeight () {
		return texture.getHeight();
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
