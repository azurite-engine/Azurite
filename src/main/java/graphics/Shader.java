package graphics;

import org.joml.*;
import org.lwjgl.BufferUtils;
import util.Log;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    /**
     * Vertex Shader ID
     */
    public static int vertexID;
    /**
     * Fragment Shader ID
     */
    public static int fragmentID;

    // Setup shaders
    /**
     * The Shader Program's ID
     */
    private int shaderProgramID;
    /**
     * Is this shader currently bound
     */
    private boolean beingUsed = false;
    /**
     * Vertex Shader code
     */
    private String vertexSource;
    /**
     * Fragment Shader code
     */
    private String fragmentSource;
    /**
     * Filepath to the shader file
     */
    private String filepath;

    private HashMap<String, Integer> uniformLocations;

    /**
     * Loads the shader file at filepath. The shader file should have both shaders.
     * Vertex Shader and fragment shader. #type should be used to separate them.
     */
    public Shader(String filePath) {
        this.filepath = filePath;
        this.uniformLocations = new HashMap<>();
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Find first pattern after #type
            int index = source.indexOf("#type") + 6;
            int endOfLine = source.indexOf("\n", index); // \r\n on windows
            String firstPattern = source.substring(index, endOfLine).trim();

            // find the second pattern after #type
            index = source.indexOf("#type", endOfLine) + 6;
            endOfLine = source.indexOf("\n", index);
            String secondPattern = source.substring(index, endOfLine).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token \"" + firstPattern + "\"");
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token \"" + secondPattern + "\"");
            }

        } catch (IOException e) {
            e.printStackTrace();
            assert false : "[ERROR] could not open shader file at \"" + filepath + "\"";
        }
    }

    /**
     * Creates vertex and fragment shader objects and compiles them.
     * Also links them with the shader program
     */
    public void compile() {
        // load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Add the shader source to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            Log.fatal("vertex shader compilation failed. " + filepath + "\n\t", 2);
            Log.fatal(glGetShaderInfoLog(vertexID, length), false);
            assert false : "";
        }

        // load and compile vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Add the shader source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Check for errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            Log.fatal("fragment shader compilation failed. " + filepath + "\n\t", 2);
            Log.fatal(glGetShaderInfoLog(fragmentID, length), false);
            assert false : "";
        }

        // Compile and link shaders
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int length = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            Log.fatal("shader linking failed. " + filepath + "\n\t", 2);
            Log.fatal(glGetProgramInfoLog(shaderProgramID, length), false);
            assert false : "";
        }
    }

    /**
     * Bind the shader program
     */
    public void attach() {
        if (!beingUsed) {
            // bind shader program
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    /**
     * Unbind the shader program
     */
    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    private int getLocation(String name) {
        int location = uniformLocations.getOrDefault(name, -1);
        if (location == -1) {
            location = glGetUniformLocation(shaderProgramID, name);
            uniformLocations.put(name, location);
        }
        return location;
    }

    /**
     * Upload a 4x4 Matrix to the gpu
     *
     * @param varName name of the uniform
     * @param mat4    the matrix to be uploaded
     */
    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = getLocation(varName);
        attach(); // make sure the shader is being used
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    /**
     * Upload a 3x3 Matrix to the gpu
     *
     * @param varName name of the uniform
     * @param mat3    the matrix to be uploaded
     */
    public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = getLocation(varName);
        attach();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    /**
     * Upload a 4-d Vector to the gpu
     *
     * @param varName name of the uniform
     * @param vec     the vector to be uploaded
     */
    public void uploadVec4f(String varName, Vector4f vec) {
        int varLocation = getLocation(varName);
        attach(); // make sure the shader is being used
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    /**
     * Upload a 3-d Vector array to the gpu
     *
     * @param varName name of the uniform
     * @param vec     the array to be uploaded
     */
    public void uploadVec3fArray(String varName, Vector3f[] vec) {
        int varLocation = getLocation(varName);
        attach();
        float[] vals = new float[vec.length * 3];
        for (int i = 0; i < vec.length; i++) {
            vals[i * 3] = vec[i].x;
            vals[i * 3 + 1] = vec[i].y;
            vals[i * 3 + 2] = vec[i].z;
        }
        glUniform3fv(varLocation, vals);
    }

    /**
     * Upload a 2-d Vector to the gpu
     *
     * @param varName name of the uniform
     * @param vec     the vector to be uploaded
     */
    public void uploadVec2f(String varName, Vector2f vec) {
        int varLocation = getLocation(varName);
        attach();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    /**
     * Upload a 2-d Vector array to the gpu
     *
     * @param varName name of the uniform
     * @param vec     the array to be uploaded
     */
    public void uploadVec2fArray(String varName, Vector2f[] vec) {
        int varLocation = getLocation(varName);
        attach();
        float[] vals = new float[vec.length * 2];
        for (int i = 0; i < vec.length; i++) {
            vals[i * 2] = vec[i].x;
            vals[i * 2 + 1] = vec[i].y;
        }
        glUniform2fv(varLocation, vals);
    }

    /**
     * Upload a float to the gpu
     *
     * @param varName name of the uniform
     * @param val     the float value to be uploaded
     */
    public void uploadFloat(String varName, float val) {
        int varLocation = getLocation(varName);
        attach(); // make sure the shader is being used
        glUniform1f(varLocation, val);
    }

    /**
     * Upload a float array to the gpu
     *
     * @param varName name of the uniform
     * @param array   the array to be uploaded
     */
    public void uploadFloatArray(String varName, float[] array) {
        int varLocation = getLocation(varName);
        attach(); // make sure the shader is being used
        glUniform1fv(varLocation, array);
    }

    /**
     * Upload an int to the gpu
     *
     * @param varName name of the uniform
     * @param val     the int value to be uploaded
     */
    public void uploadInt(String varName, int val) {
        int varLocation = getLocation(varName);
        attach(); // make sure the shader is being used
        glUniform1i(varLocation, val);
    }

    /**
     * Upload a integer texture sampler to the gpu
     *
     * @param varName name of the uniform
     * @param slot    the texture slot to which the texture is bound
     */
    public void uploadTexture(String varName, int slot) {
        int varLocation = getLocation(varName);
        attach(); // make sure the shader is being used
        glActiveTexture(varLocation);
        glUniform1i(varLocation, slot);
    }

    /**
     * Upload a int array to the gpu
     *
     * @param varName name of the uniform
     * @param array   the array to be uploaded
     */
    public void uploadIntArray(String varName, int[] array) {
        int varLocation = getLocation(varName);
        attach();
        glUniform1iv(varLocation, array);
    }
}
