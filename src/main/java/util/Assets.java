package util;

import graphics.Shader;
import graphics.Spritesheet;
import graphics.Texture;

import java.io.File;
import java.util.HashMap;

public class Assets {
	private static HashMap<String, Shader> shaders = new HashMap<>();
	private static HashMap<String, Texture> textures = new HashMap<>();
	private static HashMap<String, Spritesheet> spritesheets = new HashMap<>();

	public static Shader getShader(String path) {
		File file = new File(path);
		if (shaders.containsKey(file.getAbsolutePath())) {
			return shaders.get(file.getAbsolutePath());
		}
		Shader shader = new Shader(path);
		shader.compile();
		shaders.put(file.getAbsolutePath(), shader);
		return shader;
	}

	public static Texture getTexture(String path) {
		File file = new File(path);
		if (textures.containsKey(file.getAbsolutePath())) {
			return textures.get(file.getAbsolutePath());
		}
		Texture texture = new Texture(path);
		textures.put(file.getAbsolutePath(), texture);
		return texture;
	}
	
	private static void addSpritesheet (String path, Spritesheet spritesheet) {
		File file = new File(path);
		if (!Assets.spritesheets.containsKey(file.getAbsolutePath())) {
			Assets.spritesheets.put(file.getAbsolutePath(), spritesheet);
		}
	}
	
	private static Spritesheet getSpritesheet (String path) {
		File file = new File(path);
		if (!Assets.spritesheets.containsKey(file.getAbsolutePath())) {
			assert false : "[ERROR] Tried to access spritesheet \"" + path + "\", but it does not exist.";
		}
		return Assets.spritesheets.getOrDefault(file.getAbsolutePath(), null);
	}
	
	public static Spritesheet loadSpritesheet (String path, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
		addSpritesheet(path, new Spritesheet(getTexture(path), spriteWidth, spriteHeight, numSprites, spacing));
		return getSpritesheet(path);
	}
	
}
