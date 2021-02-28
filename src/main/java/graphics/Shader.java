package graphics;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

	private int shaderProgramID;
	private boolean beingUsed = false;

	// Setup shaders
	public static int vertexID;
	public static int fragmentID;

	private String vertexSource;
	private String fragmentSource;
	private String filepath;

	public Shader(String filePath) {
		this.filepath = filePath;
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
			System.out.println("[FATAL] Vertex shader compilation failed. " + filepath + "\n\t");
			System.out.println(glGetShaderInfoLog(vertexID, length));
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
			System.out.println("[FATAL] Fragment shader compilation failed. " + filepath + "\n\t");
			System.out.println(glGetShaderInfoLog(fragmentID, length));
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
			System.out.println("[FATAL] Shader linking failed. " + filepath + "\n\t");
			System.out.println(glGetProgramInfoLog(shaderProgramID, length));
			assert false : "";
		}
	}

	public void attach() {
		if (!beingUsed) {
			// bind shader program
			glUseProgram(shaderProgramID);
			beingUsed = true;
		}
	}

	public void detach() {
		glUseProgram(0);
		beingUsed = false;
	}
	
	public void uploadMat4f (String varName, Matrix4f mat4) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		attach(); // make sure the shader is being used
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer);
		glUniformMatrix4fv(varLocation, false, matBuffer);
	}
	
	public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        attach();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }
	
	public void uploadVec4f (String varName, Vector4f v) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		attach(); // make sure the shader is being used
		glUniform4f(varLocation, v.x, v.y, v.z, v.w);
	}

	/**
	 * Upload a vector3f array to the gpu
	 * @param varName String: name of the uniform
	 * @param vec Vector3f[]: the array to be uploaded
	 */
	public void uploadVec3fArray(String varName, Vector3f[] vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		attach();
		float[] vals = new float[vec.length * 3];
		for (int i = 0; i < vec.length; i++) {
			vals[i * 3] = vec[i].x;
			vals[i * 3 + 1] = vec[i].y;
			vals[i * 3 + 2] = vec[i].z;
		}
		glUniform3fv(varLocation, vals);
	}
	
	public void uploadVec2f(String varName, Vector2f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        attach();
        glUniform2f(varLocation, vec.x, vec.y);
    }

	/**
	 * Upload a vector2f array to the gpu
	 * @param varName String: name of the uniform
	 * @param vec Vector2f[]: the array to be uploaded
	 */
	public void uploadVec2fArray(String varName, Vector2f[] vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		attach();
		float[] vals = new float[vec.length * 2];
		for (int i = 0; i < vec.length; i++) {
			vals[i * 2] = vec[i].x;
			vals[i * 2 + 1] = vec[i].y;
		}
		glUniform2fv(varLocation, vals);
	}

	public void uploadFloat (String varName, float val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		attach(); // make sure the shader is being used
		glUniform1f(varLocation, val);
	}
	/**
	 * Upload a float array to the gpu
	 * @param varName String: name of the uniform
	 * @param array float[]: the array to be uploaded
	 */
	public void uploadFloatArray(String varName, float[] array) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		attach(); // make sure the shader is being used
		glUniform1fv(varLocation, array);
	}
	
	public void uploadInt (String varName, int val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		attach(); // make sure the shader is being used
		glUniform1i(varLocation, val);
	}
	
	public void uploadTexture (String varName, int slot) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		attach(); // make sure the shader is being used
		glUniform1i(varLocation, slot);
	}
	
	public void uploadIntArray(String varName, int[] array) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        attach();
        glUniform1iv(varLocation, array);
    }
}
