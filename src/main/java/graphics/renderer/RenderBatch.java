package graphics.renderer;

import graphics.Primitive;
import graphics.ShaderDatatype;
import graphics.Texture;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
	private Primitive primitive;
	private final ShaderDatatype[] attributes;
	protected boolean shouldRebufferData;

	private int vbo;
	private int vao;
	private int ebo;

	public RenderBatch(int maxBatchSize, int zIndex, Primitive primitive, ShaderDatatype... attributes) {
		this.maxBatchSize = maxBatchSize;
		this.zIndex = zIndex;
		this.primitive = primitive;
		this.attributes = attributes;

		spriteCount = 0;
		hasRoom = true;
		textureIndex = 0;
		textures = new ArrayList<>();
		for (ShaderDatatype t : attributes) {
			vertexCount += t.count;
			vertexSize += t.size;
		}
		data = new float[maxBatchSize * primitive.vertexCount * vertexCount];
	}

	public void start() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, maxBatchSize * primitive.vertexCount * vertexSize, GL_DYNAMIC_DRAW);
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

	protected abstract void loadVertexProperties(int index, int offset);

	protected int getOffset(int index) {
		return index * 4 * vertexCount;
	}

	protected void load(int index) {
		shouldRebufferData = true;
		spriteCount++;
		int offset = getOffset(index);
		loadVertexProperties(index, offset);
	}

	protected int addTexture(Texture texture) {
		int texIndex;
		if (textures.contains(texture)) {
			texIndex = textures.indexOf(texture) + 1;
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
		return spriteCount * primitive.elementCount;
	}

	private IntBuffer generateIndices() {
		IntBuffer elementBuffer = BufferUtils.createIntBuffer(primitive.elementCount * maxBatchSize);
		for (int i = 0; i < maxBatchSize; i++) {
			primitive.elementCreation.accept(elementBuffer, i);
		}
		elementBuffer.flip();
		return elementBuffer;
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
