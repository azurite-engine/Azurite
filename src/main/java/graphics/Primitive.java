package graphics;

import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;
import java.util.function.BiConsumer;

/**
 * The basic primitive that a batch draws.
 */
public enum Primitive {
	QUAD(4, 6, GL11.GL_TRIANGLES, (elements, i) -> {
		int offset = 4 * i;

		// 3, 2, 0, 0, 2, 1, 7, 6, 4, 4, 6, 5

		// Triangle 1
		elements.put(offset + 3);
		elements.put(offset + 2);
		elements.put(offset + 0);

		// Triangle 2
		elements.put(offset + 0);
		elements.put(offset + 2);
		elements.put(offset + 1);
	}),
	LINE(2, 2, GL11.GL_LINES, (elements, i) -> {
		int offset = 2 * i;

		elements.put(offset);
		elements.put(offset + 1);
	});


	/** Number of vertices in the primitive */
	public final int vertexCount;
	/** Number of elements in the primitive */
	public final int elementCount;
	/** Primitive ID that opengl expects */
	public final int openglPrimitive;
	/** Puts index data in the provided int buffer */
	public final BiConsumer<IntBuffer, Integer> elementCreation;

	Primitive(int vertexCount, int elementCount, int openglPrimitive, BiConsumer<IntBuffer, Integer> elementCreation) {
		this.vertexCount = vertexCount;
		this.elementCount = elementCount;
		this.openglPrimitive = openglPrimitive;
		this.elementCreation = elementCreation;
	}
}
