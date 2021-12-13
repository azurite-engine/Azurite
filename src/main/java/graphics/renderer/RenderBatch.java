package graphics.renderer;

import graphics.Primitive;
import graphics.ShaderDatatype;
import graphics.Texture;
import org.lwjgl.BufferUtils;
import util.MathUtils;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * <h1>Azurite</h1>
 * <p>
 *     A render batch is a collection of elements that are "batched" together into
 *     a single object. This objects conglomerates the vertex data associated with
 *     a specified primitive and prepares it to be rendered by it's associated
 *     renderer. The pipeline of a {@code RenderBatch} object is as follows:
 *     <ol>
 *         <li>
 *             The {@code RenderBatch} class is extended by another class intended
 *             to be a batch of a certain primitive; for this example let's say it's
 *             a quadrilateral.
 *         </li>
 *         <li>
 *             The {@code Renderer} class is extended by a class intended to render
 *             the aforementioned quadrilateral render batch, where the type parameter
 *             is specified as our {@code RenderBatch}.
 *         </li>
 *         <li>
 *             Now that all classes are set up, a quadrilateral render batch is created,
 *             and several quadrilateral vertices are loaded into its {@code data} field
 *             via an overloaded {@code loadVertexProperties} function (which specifies
 *             if the quads will have color, what texture they would use, etc.) This is
 *             also when element indices are created, which specify in what order the
 *             GPU should process/render vertices.
 *         </li>
 *         <li>
 *             This data is then submitted to the GPU, any associated metadata about the
 *             vertices and the quads are rendered.
 *         </li>
 *     </ol>
 *     Of course, one doesn't necessarily have to use quads for rendering; one could
 *     get as inventive as they'd like. The {@code graphics.renderer} API pre-specifies
 *     some useful renderers (hopefully so that most developers don't have to make their
 *     own renderers.)
 * </p>
 *
 * @see Renderer
 */
public abstract class RenderBatch implements Comparable<RenderBatch> {
    /**
     * The primitive that this batch draws
     */
    public final Primitive primitive;
    /**
     * Max number of primitives a batch can hold
     */
    protected final int maxBatchSize;
    /**
     * zIndex for this batch. Used for batch ordering
     */
    private final int zIndex;
    /**
     * The attributes for the Vertex Array
     */
    private final ShaderDatatype[] attributes;
    /**
     * Does the batch have room for more submissions
     */
    public boolean hasRoom;
    /**
     * How many floats/ints in a single vertex
     */
    protected int vertexCount;
    /**
     * How many bytes for a single vertex
     */
    protected int vertexSize;
    /**
     * Vertices of one primitive
     */
    protected float[] primitiveVertices;
    /**
     * The List of submitted textures
     */
    protected List<Texture> textures;
    /**
     * The data which is uploaded to the GPU
     */
    protected float[] data;
    /**
     * Internal count of how many primitives have been submitted to this batch
     */
    protected int spriteCount;
    /**
     * Vertex Array id
     */
    protected int vao;
    /**
     * Vertex Buffer id
     */
    protected int vbo;
    /**
     * Internal index for how many textures have been submitted to this batch
     */
    private int textureIndex;
    /**
     * Index Buffer (Element Buffer) id
     */
    private int ebo;

    /**
     * Parent Renderer
     */
    private Renderer<?> renderer;

    /**
     * @param maxBatchSize the maximum number of primitives in a batch
     * @param zIndex       the zIndex of the batch. Used to sort the batches in order of which sprites appear above others.
     * @param primitive    the primitive
     * @param attributes   attributes for the Vertex array
     */
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

        this.primitiveVertices = new float[vertexCount * primitive.vertexCount];
    }

    /**
     * Create the GPU resources.
     * Generates a vao, a dynamic vbo, and a static buffer of indices.
     */
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

    /**
     * Get the renderer this batch belongs to
     *
     * @return the renderer this batch belongs to
     */
    public Renderer<?> getRenderer() {
        return renderer;
    }

    /**
     * Set the renderer this batch belongs to
     *
     * @param renderer the renderer this batch belongs to
     */
    public void setRenderer(Renderer<?> renderer) {
        this.renderer = renderer;
    }

    /**
     * Load up a primitive to the data array
     *
     * @param index  index of the primitive to be loaded
     * @param offset offset of where the primitive should start being added to the array
     */
    protected abstract void loadVertexProperties(int index, int offset);

    /**
     * Calculates offset into the data array based on index of the sprite
     *
     * @param index index of the sprite
     * @return offset into data array at which the sprites data has to be added
     */
    protected int getOffset(int index) {
        return index * primitive.vertexCount * vertexCount;
    }

    /**
     * Function for calling loadVertexProperties but also sets up necessary stuff relating
     * to uploading data to the gpu.
     * Always call this function instead of calling loadVertexProperties()
     *
     * @param index index of the sprite to be loaded
     */
    protected void load(int index) {
        if (index >= spriteCount) spriteCount++;
        loadVertexProperties(index, getOffset(index));
    }

    /**
     * Remove the object at index
     *
     * @param index the index
     */
    protected void remove(int index) {
        MathUtils.shiftOverwrite(data, getOffset(index), getOffset(index + 1));
        spriteCount--;
    }

    /**
     * Add a texture to this batch
     *
     * @param texture the texture to be rendered
     * @return the index at which texture is placed.
     * The texture will be bound to this texture slot. Hence, set the texture attribute to this value.
     */
    protected int addTexture(Texture texture) {
        int texIndex;
        if (textures.contains(texture)) {
            texIndex = textures.indexOf(texture) + 1;
        } else {
            textures.add(texture);
            texIndex = ++textureIndex;
        }
        return texIndex;
    }

    /**
     * Update the buffer on the GPU
     */
    public void updateBufferFull() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, data);
    }

    /**
     * Update the buffer on the GPU
     */
    public void updateBuffer() {
        updateBufferFull();
    }

    /**
     * Update the buffer with a memory taken as a pointer from GPU and only update the sprite that needs updating
     *
     * @param spriteIndex the index of a sprite that needs updating
     */
    public void updateBuffer(int spriteIndex) {
        //Create a pointer to a buffer memory where the mapping will begin
        FloatBuffer vertexPtr;
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        vertexPtr = Objects.requireNonNull(glMapBufferRange(GL_ARRAY_BUFFER, spriteIndex * primitive.vertexCount * vertexSize,
                primitive.vertexCount * vertexSize, GL_MAP_WRITE_BIT | GL_MAP_INVALIDATE_RANGE_BIT))
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        load(spriteIndex);

        // Get vertices from createQuad
        vertexPtr.put(primitiveVertices).position(0);
        glUnmapBuffer(GL_ARRAY_BUFFER);
    }


    /**
     * Binds the vertex array and all the textures to the required slots
     */
    public void bind() {
        glBindVertexArray(vao);
        for (int i = 0; i < textures.size(); i++)
            textures.get(i).bindToSlot(i + 1);
    }

    /**
     * Unbinds the vertex array and all the textures
     */
    public void unbind() {
        for (Texture texture : textures)
            texture.unbind();
        glBindVertexArray(0);
    }

    /**
     * Delete the vertex array, vertex buffer and index buffer (element buffer)
     */
    public void delete() {
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        glDeleteVertexArrays(vao);
    }

    /**
     * Get the number of vertices to be drawn
     *
     * @return the number of vertices to be drawn
     */
    public int getVertexCount() {
        return spriteCount * primitive.elementCount;
    }

    /**
     * Create the indices and load them up into an IntBuffer
     *
     * @return the buffer of indices
     */
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

    public boolean hasTexture(Texture tex) {
        return this.textures.contains(tex);
    }

    public int zIndex() {
        return zIndex;
    }

    @Override
    public int compareTo(RenderBatch a) {
        return Integer.compare(this.zIndex, a.zIndex);
    }
}
