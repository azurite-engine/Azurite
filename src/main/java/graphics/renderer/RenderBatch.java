package graphics.renderer;

import graphics.Color;
import graphics.Primitive;
import graphics.ShaderDatatype;
import graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import util.Log;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * <p>
 * A render batch is a collection of elements that are "batched" together into
 * a single object. This objects conglomerates the vertex data associated with
 * a specified primitive and prepares it to be rendered by it's associated
 * renderer. The pipeline of a {@code RenderBatch} object is as follows:
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
public class RenderBatch implements Comparable<RenderBatch> {
    private static int num = 0;
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
     * Is this batch full due to filled up geometry
     */
    public boolean isFull;
    /**
     * Is this batch full due to having 8 textures occupied already
     */
    public boolean isFull_Textures;
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
     * The internal data offset
     */
    protected int dataOffset;
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

        dataOffset = 0;
        isFull = false;
        isFull_Textures = false;
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
    public void init() {
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
     * Get batch ready for submission of data
     */
    public void start() {
        textureIndex = 0;
        dataOffset = 0;
        isFull_Textures = false;
        isFull = false;
        textures.clear();
        // No need to reset data array or anything. Stuff will get overridden and correctly handled.
    }

    /**
     * Finish setting batch data. upload to gpu
     */
    public void finish() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, data);
    }

    /**
     * Add a texture to this batch
     *
     * @param texture the texture to be rendered
     * @return the index at which texture is placed.
     * The texture will be bound to this texture slot. Hence, set the texture attribute to this value.
     */
    public int addTexture(Texture texture) {
        int texIndex;
        if (textures.contains(texture)) {
            texIndex = textures.indexOf(texture) + 1;
        } else {
            textures.add(texture);
            texIndex = ++textureIndex;

            if (textures.size() >= 8) {
                isFull_Textures = true;
                isFull = false;
            }
        }
        return texIndex;
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
     * @USELESS Unbinds the vertex array and all the textures
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
        // Safety check
        if (dataOffset % vertexCount != 0)
            Log.warn("a renderer seems to not have the correct amount of data!!!", 2);
        return (dataOffset * primitive.elementCount) / (vertexCount * primitive.vertexCount);
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

    private void checkFullness() {
        if (dataOffset >= data.length) {
            isFull = true;
            isFull_Textures = false;
        }
    }

    /**
     * Push a float to the data array
     *
     * @param f the value
     */
    public void pushFloat(float f) {
        data[dataOffset++] = f;
        checkFullness();
    }

    /**
     * Push an int to the data array
     *
     * @param i the value
     */
    public void pushInt(int i) {
        data[dataOffset++] = i;
        checkFullness();
    }

    /**
     * Push two floats to the data array
     *
     * @param x x value
     * @param y y value
     */
    public void pushVec2(float x, float y) {
        data[dataOffset++] = x;
        data[dataOffset++] = y;
        checkFullness();
    }

    /**
     * Push two floats to the data array
     *
     * @param v the 2d vector
     */
    public void pushVec2(Vector2f v) {
        data[dataOffset++] = v.x;
        data[dataOffset++] = v.y;
        checkFullness();
    }

    /**
     * Push three floats to the data array
     *
     * @param x x value
     * @param y y value
     * @param z z value
     */
    public void pushVec3(float x, float y, float z) {
        data[dataOffset++] = x;
        data[dataOffset++] = y;
        data[dataOffset++] = z;
        checkFullness();
    }

    /**
     * Push three floats to the data array
     *
     * @param v the 3d vector
     */
    public void pushVec3(Vector3f v) {
        data[dataOffset++] = v.x;
        data[dataOffset++] = v.y;
        data[dataOffset++] = v.z;
        checkFullness();
    }

    /**
     * Push four floats to the data array
     *
     * @param x x value
     * @param y y value
     * @param z z value
     * @param w w value
     */
    public void pushVec4(float x, float y, float z, float w) {
        data[dataOffset++] = x;
        data[dataOffset++] = y;
        data[dataOffset++] = z;
        data[dataOffset++] = w;
        checkFullness();
    }

    /**
     * Push four floats to the data array
     *
     * @param v the 4d vector
     */
    public void pushVec4(Vector4f v) {
        data[dataOffset++] = v.x;
        data[dataOffset++] = v.y;
        data[dataOffset++] = v.z;
        data[dataOffset++] = v.w;
        checkFullness();
    }

    /**
     * Push four floats to the data array
     *
     * @param c the color
     */
    public void pushColor(Color c) {
        Vector4f v = c.toNormalizedVec4f();
        data[dataOffset++] = v.x;
        data[dataOffset++] = v.y;
        data[dataOffset++] = v.z;
        data[dataOffset++] = v.w;
        checkFullness();
    }

//    public void beginVertex() {
//        // DOES LITERALLY NOTHING
//    }
//
//    public void endVertex() {
//        vertexCount++;
//        checkFullness();
//    }
}
