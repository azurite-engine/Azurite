package util;

import graphics.shaders.Shader;
import graphics.shaders.ShaderProgram;
import graphics.shaders.ShaderType;
import graphics.textures.TextureLoader;
import tiles.Spritesheet;
import graphics.textures.Texture;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Assets {

	private static final HashMap<String, ShaderProgram> SHADERS = new HashMap<>();
	private static final HashMap<String, Texture> TEXTURES = new HashMap<>();
	private static final HashMap<String, Spritesheet> SPRITESHEETS = new HashMap<>();

	/**
	 * (Temporary place, a little scuffed, didn't know where else to put it).
	 * The watcher will watch the "src/assets" directory, and will update every 2 seconds.
	 */
	private static final AssetWatcher WATCHER = new AssetWatcher("src/assets", 2, file -> {
		String directory = file.getParent();
		// Gets the parent directory of modified file, and checks its name.

		if (directory.endsWith("images")) {

			Texture texture = getTexture(file.getAbsolutePath());
			// Load the new data and upload it to the texture.
			texture.reupload(TextureLoader.loadTexture(file.getAbsolutePath()));

		} else if (directory.endsWith("shaders")) {

			// Load the shaders from modified file.
			Map<ShaderType, String> newData = Shader.parseFromFilepath(file.getAbsolutePath());

			ShaderProgram shaderProgram = getShader(file.getAbsolutePath());
			Shader[] shaders = shaderProgram.getShaders();

			// For each file present in shader program, try to compile it with the new data.
			// If at least one of them fails to compile, do not reload the program.
			for (Shader shader : shaders) {
				if (!shader.compile(newData.get(shader.getType()), false)) {
					return;
				}
			}

			// If the shaders have all compiled successfully, link them to the program and validate it.
			shaderProgram.link();
		}

	});

	/**
	 * Loads a shader from the filesystem, compiles it, then returns type Shader.
	 * @param path to GLSL shader resource
	 * @return returns type Shader
	 */
	public static ShaderProgram getShader(String path) {
		File file = new File(path);
		if (SHADERS.containsKey(file.getAbsolutePath())) {
			return SHADERS.get(file.getAbsolutePath());
		}
		ShaderProgram shader = new ShaderProgram(path);
		shader.link();
		SHADERS.put(file.getAbsolutePath(), shader);
		return shader;
	}

	/**
	 * Loads a image from the filesystem, and returns a Texture.
	 * @param path to Texture resource (usually a .png file)
	 * @return returns type Texture
	 */
	public static Texture getTexture(String path) {
		File file = new File(path);
		if (TEXTURES.containsKey(file.getAbsolutePath())) {
			return TEXTURES.get(file.getAbsolutePath());
		}
		Texture texture = new Texture(path);
		TEXTURES.put(file.getAbsolutePath(), texture);
		return texture;
	}

	/**
	 * Adds a filepath and spritesheet to the Asset class's spritesheet hashmap. (private)
	 * @param path to Texture resource (usually a .png file)
	 * @param spritesheet object
	 */
	private static void addSpritesheet (String path, Spritesheet spritesheet) {
		File file = new File(path);
		if (!Assets.SPRITESHEETS.containsKey(file.getAbsolutePath())) {
			Assets.SPRITESHEETS.put(file.getAbsolutePath(), spritesheet);
		}
	}


	private static Spritesheet getSpritesheet (String path) {
		File file = new File(path);
		if (!Assets.SPRITESHEETS.containsKey(file.getAbsolutePath())) {
			assert false : "[ERROR] Tried to access spritesheet \"" + path + "\", but it does not exist or is not loaded, try using \"Assets.loadSpritesheet()\".";
		}
		return Assets.SPRITESHEETS.getOrDefault(file.getAbsolutePath(), null);
	}

	/**
	 * Loads a image from the filesystem, and returns a Spritesheet object.
	 * @param path to Texture resource (usually a .png file)
	 * @param width of each sprite
	 * @param height of each sprite
	 * @param number of sprites in the sheet
	 * @param pixel spacing between sprites (0 if no spacing)
	 * @return returns type Spritesheet
	 */
	public static Spritesheet loadSpritesheet (String path, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
		addSpritesheet(path, new Spritesheet(getTexture(path), spriteWidth, spriteHeight, numSprites, spacing));
		return getSpritesheet(path);
	}

	public static void update() {
		WATCHER.update();
	}

	/**
	 * For each asset hashmap, dispose of its contents.
	 * Shuts down the watcher
	 */
	public static void cleanUp() {
		TEXTURES.values().forEach(Texture::delete);
		SHADERS.values().forEach(ShaderProgram::delete);

		WATCHER.stop();
	}

}
