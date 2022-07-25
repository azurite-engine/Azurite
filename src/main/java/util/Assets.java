package util;

import audio.AudioBuffer;
import graphics.Shader;
import graphics.Spritesheet;
import graphics.Texture;
import io.bin.BinaryIO;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * The Assets class contains methods to assist in loading common resources used by the engine from the filesystem as well as HashMaps to keep track of loaded resources.
 * If the same path is loaded again via the Assets class, it will call the item up from the hashmap rather than reload it.
 */
public class Assets {
    private static HashMap<String, Shader> shaders = new HashMap<>();
    private static HashMap<String, ByteBuffer> dataFiles = new HashMap<>();
    private static HashMap<String, Texture> textures = new HashMap<>();
    private static HashMap<String, AudioBuffer> audioBuffers = new HashMap<>();
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
        Log.debug("shader requested to load: \"" + path + "\"");
        Shader shader = new Shader(path);
        shader.compile();
        Log.debug("shader compilation successful");
        shaders.put(file.getAbsolutePath(), shader);
        return shader;
    }

    /**
     * Loads a text file from the filesystem and returns it in a String
     *
     * @param path to data file
     * @return returns type String
     */
    public static ByteBuffer getDataFile(String path) {
        try {
            File file = new File(path);
            if (dataFiles.containsKey(file.getAbsolutePath())) {
                return dataFiles.get(file.getAbsolutePath());
            }
            Log.debug("data file requested to load: \"" + path + "\"");
            ByteBuffer data = BinaryIO.readData(file);
            Log.debug("data successfully loaded");
            dataFiles.put(file.getAbsolutePath(), data);
            return data;
        } catch (IOException e) {
            Log.fatal("file not found: \"" + path + "\"", 1);
            e.printStackTrace();
            return null;
        }
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
        Log.debug("texture requested to load: \"" + path + "\"");
        Texture texture = new Texture(path);
        Log.debug("loading texture successfully");
        textures.put(file.getAbsolutePath(), texture);
        return texture;
    }

    public static AudioBuffer getAudioBuffer(String path) {
        File file = new File(path);
        if (audioBuffers.containsKey(file.getAbsolutePath())) {
            return audioBuffers.get(file.getAbsolutePath());
        }
        Log.debug("audiobuffer requested to load: \"" + path + "\"");
        AudioBuffer audioBuffer = new AudioBuffer(path);
        Log.debug("loading audiobuffer successfully");
        audioBuffers.put(file.getAbsolutePath(), audioBuffer);
        return audioBuffer;
    }

    /**
     * Adds a filepath and spritesheet to the Asset class's spritesheet hashmap. (private)
     * * @param path to Texture resource (usually a .png file)
     *
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
        if (!Assets.spritesheets.containsKey(file.getAbsolutePath()))
            Log.fatal("tried to access spritesheet \"" + path + "\", but it does not exist or is not loaded, try using \"Assets.loadSpritesheet()\".");
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
