package graphics;

import java.nio.IntBuffer;
import java.util.function.BiConsumer;

public enum Primitive {
	QUAD(4, 6, 	(elements, i) -> {
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
	});

	public final int vertexCount;
	public final int elementCount;
	public final BiConsumer<IntBuffer, Integer> elementCreation;

	Primitive(int vertexCount, int elementCount, BiConsumer<IntBuffer, Integer> elementCreation) {
		this.vertexCount = vertexCount;
		this.elementCount = elementCount;
		this.elementCreation = elementCreation;
	}
}
