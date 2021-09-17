package util;

import graphics.Shader;
import graphics.Texture;
import tiles.Spritesheet;

import java.io.File;
import java.util.HashMap;

public class Assets {
    private static HashMap<String, Shader> shaders = new HashMap<>();
    private static HashMap<String, Texture> textures = new HashMap<>();
    private static HashMap<String, Spritesheet> spritesheets = new HashMap<>();

    /**
     * Loads a shader from the filesystem, compiles it, then returns type Shader.
     *
     * @param path to GLSL shader resource
     * @return returns type Shader
     */
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

    /**
     * Loads a image from the filesystem, and returns a Texture.
     *
     * @param path to Texture resource (usually a .png file)
     * @return returns type Texture
     */
    public static Texture getTexture(String path) {
        File file = new File(path);
        if (textures.containsKey(file.getAbsolutePath())) {
            return textures.get(file.getAbsolutePath());
        }
        Texture texture = new Texture(path);
        textures.put(file.getAbsolutePath(), texture);
        return texture;
    }

    /**
     * Adds a filepath and spritesheet to the Asset class's spritesheet hashmap. (private)
     *
     * @param path        to Texture resource (usually a .png file)
     * @param spritesheet object
     */
    private static void addSpritesheet(String path, Spritesheet spritesheet) {
        File file = new File(path);
        if (!Assets.spritesheets.containsKey(file.getAbsolutePath())) {
            Assets.spritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }


    private static Spritesheet getSpritesheet(String path) {
        File file = new File(path);
        assert Assets.spritesheets.containsKey(file.getAbsolutePath()) : "[ERROR] Tried to access spritesheet \"" + path + "\", but it does not exist or is not loaded, try using \"Assets.loadSpritesheet()\".";
        return Assets.spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }

    /**
     * Loads a image from the filesystem, and returns a Spritesheet object.
     *
     * @param path         to Texture resource (usually a .png file)
     * @param spriteWidth  of each sprite
     * @param spriteHeight of each sprite
     * @param numSprites   of sprites in the sheet
     * @param spacing      spacing between sprites (0 if no spacing)
     * @return returns type Spritesheet
     */
    public static Spritesheet loadSpritesheet(String path, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        addSpritesheet(path, new Spritesheet(getTexture(path), spriteWidth, spriteHeight, numSprites, spacing));
        return getSpritesheet(path);
    }

}
