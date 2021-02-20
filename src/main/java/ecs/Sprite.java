package ecs;

import graphics.Texture;
import org.joml.Vector2f;

public class Sprite {
	
	private Texture texture;

	private Vector2f[] textureCoordinates;
	
	// This contains the default coordinates for an entire image UV
	private Vector2f[] defaultTextureCoordinates = {
		new Vector2f(0, 0),
		new Vector2f(0, 1),
		new Vector2f(1, 1),
		new Vector2f(1, 0)
	};
	
	public Sprite (Texture texture, Vector2f[] uv) {
		this.texture = texture;
		this.textureCoordinates = uv;
	}
	
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
}
