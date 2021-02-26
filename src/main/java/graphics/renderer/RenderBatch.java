package graphics.renderer;

import graphics.ShaderDatatype;
import graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public abstract class RenderBatch implements Comparable<RenderBatch> {
	protected int vertexCount;
	protected int vertexSize;
	protected List<Texture> textures;

	public boolean hasRoom;

	protected float[] data;
	protected int spriteCount;
	protected int textureIndex;
	protected final int maxBatchSize;
	private final int zIndex;
	private final ShaderDatatype[] attributes;
	private final int primitiveVertexCount;
	private final int primitiveElementCount;
	protected boolean shouldRebufferData;

	private int vbo;
	private int vao;
	private int ebo;

	public RenderBatch(int maxBatchSize, int zIndex, ShaderDatatype... attributes) {
		this.maxBatchSize = maxBatchSize;
		this.zIndex = zIndex;
		this.attributes = attributes;
		this.primitiveVertexCount = 4; // FIXME: Change this
		this.primitiveElementCount = 6; // FIXME: Change this

		spriteCount = 0;
		hasRoom = true;
		textureIndex = 0;
		textures = new ArrayList<>();
		for (ShaderDatatype t : attributes) {
			vertexCount += t.count;
			vertexSize += t.size;
		}
		data = new float[maxBatchSize * primitiveVertexCount * vertexCount];
	}

	public void start() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, maxBatchSize * primitiveVertexCount * vertexSize, GL_DYNAMIC_DRAW);
		ebo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, generateIndices(), GL_STATIC_DRAW);

		int currentOffset = 0;
		for (int i = 0; i < attributes.length; i++) {
			ShaderDatatype attrib = attributes[i];
			glVertexAttribPointer(i, attrib.count, attrib.openglType, false, vertexSize, currentOffset);
			glEnableVertexAttribArray(i);
			currentOffset += attrib.size;
		}
	}

	protected abstract void loadVertexProperties(int index);

	protected int getOffset(int index) {
		return index * 4 * vertexCount;
	}

	protected int addTexture(Texture texture) {
		int texIndex;
		if (textures.contains(texture)) {
			texIndex = textures.indexOf(texture);
		} else {
			textures.add(texture);
			texIndex = textureIndex++;
		}
		return texIndex;
	}

	public void updateBuffer() {
		if (shouldRebufferData) {
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferSubData(GL_ARRAY_BUFFER, 0, data);
			shouldRebufferData = false;
		}
	}

	public void bind() {
		glBindVertexArray(vao);
		for (int i = 0; i < textures.size(); i++)
			textures.get(i).bindToSlot(i + 1);
	}

	public void unbind() {
		for (Texture texture : textures)
			texture.unbind();
		glBindVertexArray(0);
	}

	public void delete() {
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);
		glDeleteVertexArrays(vao);
	}

	public int getVertexCount() {
		return spriteCount * primitiveElementCount;
	}

	private int[] generateIndices() {
		// 6 indices/quad (3/triangle)
		int[] elements = new int[primitiveElementCount * maxBatchSize];
		for (int i = 0; i < maxBatchSize; i++) {
			loadElementIndices(elements, i);
		}

		return elements;
	}

	private void loadElementIndices(int[] elements, int i) {
		int offsetArrayIndex = primitiveElementCount * i;
		int offset = primitiveVertexCount * i;

		// 3, 2, 0, 0, 2, 1, 7, 6, 4, 4, 6, 5

		// Triangle 1
		elements[offsetArrayIndex] = offset + 3;
		elements[offsetArrayIndex + 1] = offset + 2;
		elements[offsetArrayIndex + 2] = offset + 0;

		// Triangle 2
		elements[offsetArrayIndex + 3] = offset + 0;
		elements[offsetArrayIndex + 4] = offset + 2;
		elements[offsetArrayIndex + 5] = offset + 1;
	}

	public boolean hasRoomLeft() {
		return hasRoom;
	}

	public boolean hasTextureRoom() {
		return this.textures.size() < 8;
	}

	public boolean hasTexture (Texture tex) {
		return this.textures.contains(tex);
	}

	public int zIndex () {
		return zIndex;
	}

	@Override
	public int compareTo(RenderBatch a) {
		return Integer.compare(this.zIndex, a.zIndex);
	}
}
