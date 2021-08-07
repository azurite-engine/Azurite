package graphics.shaders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    /** The ID of the shader object */
    private final int shaderID;
    /** The type of the shader object */
    private final ShaderType type;

    /**
     * Create a shader object
     * @param type type of the shader
     * @param source initial source of the shader
     */
    Shader(ShaderType type, String source) {
        this.shaderID = glCreateShader(type.getOpenglCode());
        this.type = type;

        compile(source, true);
    }

    /**
     * Separates a file into different shaders.
     * /!\ Warning: the type passed after "#type" must be one of the shader types declared in the ShaderType enumeration.
     * @see ShaderType
     * @param filepath the file to read data from
     * @return a map containing the list of all shaders included in the file. The map associates a shader type to some source code.
     */
    public static Map<ShaderType, String> parseFromFilepath(String filepath) {
        Map<ShaderType, String> result = new HashMap<>();

        try {

            String data = new String(Files.readAllBytes(Paths.get(filepath)));
            // separates the data into chunks
            String[] matches = Pattern.compile("#type").split(data);
            // For each chunk of GLSL code, gets its source and type
            for (String match : matches) {

                if (!match.isEmpty()) {
                    String type = match.substring(0, match.indexOf(System.lineSeparator()));
                    String source = match.replace(type, "");

                    try {
                        result.put(ShaderType.valueOf(type.toUpperCase().trim()), source);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        System.out.println("[FATAL] Unknown shader type \"" + type + "\"");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "[ERROR] could not open shader file at \"" + filepath + "\"";
        }

        return result;
    }

    /**
     * Compiles the shader with given source or with backup source if an error occurs
     * @param source the source that will be attached to the shader
     * @param useBackup whether a backup should be compiled if something goes wrong
     * @return whether the shader's compilation was successful.
     */
    public boolean compile(String source, boolean useBackup) {
        // Add the shader source to the GPU
        glShaderSource(shaderID, source);
        // Compile the shader
        glCompileShader(shaderID);

        // Check for errors, use backup if necessary
        int success = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
            System.out.println("[FATAL] " + type.name().toLowerCase() + " shader compilation failed.");
            System.out.println(glGetShaderInfoLog(shaderID, length));

            if (useBackup) {
                // Repeat the same process, this time without backup
                compile(BackupShaders.get(type), false);
            }

            return false;
        }

        return true;
    }

    /**
     * Attaches the shader to the given shader program
     * @param programID the ID of the shader program
     */
    public void attach(int programID) {
        glAttachShader(programID, shaderID);
    }

    /**
     * Detaches the shader to the given shader program
     * @param programID the ID of the shader program
     */
    public void detach(int programID) {
        glDetachShader(programID, shaderID);
    }

    /**
     * Deletes the shader
     */
    public void delete() {
        glDeleteShader(shaderID);
    }

    public ShaderType getType() {
        return type;
    }

}