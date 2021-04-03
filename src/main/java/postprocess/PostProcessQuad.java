package postprocess;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class PostProcessQuad {
	public static PostProcessQuad instance;

	public static PostProcessQuad getInstance() {
		if (instance == null)
			instance = new PostProcessQuad();
		return instance;
	}

	public static void bindQuad() {
		getInstance()._bindQuad();
	}

	public static void unbindQuad() {
		getInstance()._unbindQuad();
	}

	private final int quadVao;

	public PostProcessQuad() {
		quadVao = glGenVertexArrays();
		glBindVertexArray(quadVao);
		int buffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		glBufferData(GL_ARRAY_BUFFER, new float[] {
				-1.0f, -1.0f,   0.0f, 0.0f,
				 1.0f, -1.0f,   1.0f, 0.0f,
				 1.0f,  1.0f,   1.0f, 1.0f,

				-1.0f, -1.0f,   0.0f, 0.0f,
				 1.0f,  1.0f,   1.0f, 1.0f,
				-1.0f,  1.0f,   0.0f, 1.0f
		}, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
		glEnableVertexAttribArray(1);
	}

	public void _bindQuad() {
		glBindVertexArray(quadVao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
	}

	private void _unbindQuad() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}
}
