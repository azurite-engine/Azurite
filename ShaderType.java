package graphics.shaders;

import org.lwjgl.opengl.GL45;

public enum ShaderType {

    /**
     * Represents one of GLSL's shader types.
     */

    VERTEX(GL45.GL_VERTEX_SHADER),
    FRAGMENT(GL45.GL_FRAGMENT_SHADER),
    GEOMETRY(GL45.GL_GEOMETRY_SHADER),
    TESS_CONTROL(GL45.GL_TESS_CONTROL_SHADER),
    TESS_EVALUATION(GL45.GL_TESS_EVALUATION_SHADER),
    COMPUTE(GL45.GL_COMPUTE_SHADER);

    /**
     * The OpenGL code of the shader type
     */
    private final int openglCode;

    ShaderType(int openglCode) {
        this.openglCode = openglCode;
    }

    public int getOpenglCode() {
        return openglCode;
    }

}