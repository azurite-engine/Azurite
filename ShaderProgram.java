package graphics.shaders;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    /** The Shader Program's ID */
    private final int shaderProgramID;
    /** The shaders that will be used in this program */
    private final Shader[] shaders;
    /** The uniforms that will be used by this program */
    private final HashMap<String, Integer> uniformLocations;

    /** Is this shader currently bound */
    private boolean beingUsed;

    /**
     * Creates the shader program object
     * @param shaders the shaders that will form this program
     */
    public ShaderProgram(Shader[] shaders) {
        this.shaderProgramID = glCreateProgram();
        this.shaders = shaders;
        this.uniformLocations = new HashMap<>();

        this.beingUsed = false;
    }

    /**
     * Creates a shader program directly from a file
     * @See Shader#parseFromFilepath()
     * @param filepath the file to read the data from
     */
    public ShaderProgram(String filepath) {
        Map<ShaderType, String> map = Shader.parseFromFilepath(filepath);
        Shader[] shaders = new Shader[map.size()];

        int index = 0;
        // Creates shader objects from the data inside the map
        for(Map.Entry<ShaderType, String> entry : map.entrySet()) {
            shaders[index ++] = new Shader(entry.getKey(), entry.getValue());
        }

        this.shaderProgramID = glCreateProgram();
        this.shaders = shaders;
        this.uniformLocations = new HashMap<>();

        this.beingUsed = false;
    }

    /**
     * Attaches the shaders to the program
     * Links the program and checks for errors
     * Validates the program and checks for errors
     * Detaches the shaders from the program
     */
    public void link() {
        for(Shader shader : shaders) {
            shader.attach(shaderProgramID);
        }

        glLinkProgram(shaderProgramID);

        int linking = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (linking == GL_FALSE) {
            int length = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("[FATAL] Shader linking failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, length));
            assert false : "";
        }

        glValidateProgram(shaderProgramID);

        int validation = glGetProgrami(shaderProgramID, GL_VALIDATE_STATUS);
        if (validation == GL_FALSE) {
            int length = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("[FATAL] Shader validation failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, length));
            assert false : "";
        }

        for(Shader shader : shaders) {
            shader.detach(shaderProgramID);
        }
    }

    /** Binds the shader program */
    public void attach() {
        if (!beingUsed) {
            // bind shader program
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    /** Unbinds the shader program */
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
     * @param varName name of the uniform
     * @param mat4 the matrix to be uploaded
     */
    public void uploadMat4f (String varName, Matrix4f mat4) {
        int varLocation = getLocation(varName);
        attach(); // make sure the shader is being used
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    /**
     * Upload a 3x3 Matrix to the gpu
     * @param varName name of the uniform
     * @param mat3 the matrix to be uploaded
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
     * @param varName name of the uniform
     * @param vec the vector to be uploaded
     */
    public void uploadVec4f (String varName, Vector4f vec) {
        int varLocation = getLocation(varName);
        attach(); // make sure the shader is being used
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    /**
     * Upload a 3-d Vector array to the gpu
     * @param varName name of the uniform
     * @param vec the array to be uploaded
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
     * @param varName name of the uniform
     * @param vec the vector to be uploaded
     */
    public void uploadVec2f(String varName, Vector2f vec) {
        int varLocation = getLocation(varName);
        attach();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    /**
     * Upload a 2-d Vector array to the gpu
     * @param varName name of the uniform
     * @param vec the array to be uploaded
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
     * @param varName name of the uniform
     * @param val the float value to be uploaded
     */
    public void uploadFloat (String varName, float val) {
        int varLocation = getLocation(varName);
        attach(); // make sure the shader is being used
        glUniform1f(varLocation, val);
    }

    /**
     * Upload a float array to the gpu
     * @param varName name of the uniform
     * @param array the array to be uploaded
     */
    public void uploadFloatArray(String varName, float[] array) {
        int varLocation = getLocation(varName);
        attach(); // make sure the shader is being used
        glUniform1fv(varLocation, array);
    }

    /**
     * Upload an int to the gpu
     * @param varName name of the uniform
     * @param val the int value to be uploaded
     */
    public void uploadInt (String varName, int val) {
        int varLocation = getLocation(varName);
        attach(); // make sure the shader is being used
        glUniform1i(varLocation, val);
    }

    /**
     * Upload an integer texture sampler to the gpu
     * @param varName name of the uniform
     * @param slot the texture slot to which the texture is bound
     */
    public void uploadTexture (String varName, int slot) {
        int varLocation = getLocation(varName);
        attach(); // make sure the shader is being used
        glUniform1i(varLocation, slot);
    }

    /**
     * Upload an int array to the gpu
     * @param varName name of the uniform
     * @param array the array to be uploaded
     */
    public void uploadIntArray(String varName, int[] array) {
        int varLocation = getLocation(varName);
        attach();
        glUniform1iv(varLocation, array);
    }

    /**
     * Deletes shaders used within this program
     */
    public void delete() {
        for(Shader shader : shaders) {
            shader.delete();
        }
    }

    public Shader[] getShaders() {
        return shaders;
    }

}