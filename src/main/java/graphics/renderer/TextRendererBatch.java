package graphics.renderer;//{This comment is intentionally added to create a git merge conflict}

import fonts.GlyphRenderer;
import graphics.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.Transform;
import util.Logger;
import util.Utils;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glUnmapBuffer;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author Asher Haun
 */
public class TextRendererBatch extends RenderBatch {
    private ArrayList<GlyphRenderer> glyphRenderers; // even though this is an arrayList, maxBatchSize still applies
    private int numberOfGlyphRenderers;

    private float[] resetData;

    /**
     * Create a default type render batch
     *
     * @param maxBatchSize maximum number of sprites in the batch
     * @param zIndex       zIndex of the batch. Used for sorting.
     */
    TextRendererBatch(int maxBatchSize, int zIndex) {
        super(maxBatchSize, zIndex, Primitive.QUAD, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT, ShaderDatatype.FLOAT);

        this.glyphRenderers = new ArrayList<>();
        this.numberOfGlyphRenderers = 0;
        resetData = new float[maxBatchSize * primitive.vertexCount * vertexCount];
    }

    /**
     * This function figures out how to add vertices with an origin at the top left
     *
     * @param index index of the primitive to be loaded
     * @param offset offset of where the primitive should start being added to the array
     */
    @Override
    protected void loadVertexProperties(int index, int offset) {

        GlyphRenderer glyphRenderer = glyphRenderers.get(index); // todo
        Vector4f color = glyphRenderer.getColor().toNormalizedVec4f();
        Vector2f[] textureCoordinates = glyphRenderer.getTexCoords();

        int textureID;
        if (glyphRenderer.getTexture() != null)
            textureID = addTexture(glyphRenderer.getTexture());
        else
            textureID = 0;

        // Add vertex with the appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1:
                    yAdd = 0.0f;
                    break;
                case 2:
                    xAdd = 0.0f;
                    break;
                case 3:
                    yAdd = 1.0f;
                    break;
            }

            // Load position
            Transform spr = glyphRenderer.getLocalTransform();
            data[offset] = spr.getPosition().x + (xAdd * spr.scale.x);
            data[offset + 1] = spr.getPosition().y + (yAdd * spr.scale.y);

            // Load color
            data[offset + 2] = color.x; // Red
            data[offset + 3] = color.y; // Green
            data[offset + 4] = color.z; // Blue
            data[offset + 5] = color.w; // Alpha

            // Load texture coordinates
            data[offset + 6] = textureCoordinates[i].x;
            data[offset + 7] = textureCoordinates[i].y;

            // Load texture ID
            data[offset + 8] = textureID;

            // Sticky to camera or not
            data[offset + 9] = glyphRenderer.isSticky() ? 1.0f : 0.0f;

            offset += vertexCount;
        }
    }

    public int getSize () {
        return numberOfGlyphRenderers;
    }

    /**
     * Remove the object at index
     * @param i the index
     */
    public void removeIndex (int i) {
        if (glyphRenderers.size() > 0) {
            glyphRenderers.remove(i);
            remove(i);
            numberOfGlyphRenderers --;
            if (i >= 1)
                super.updateBuffer(i - 1);
        }
    }

    public void removeGlyphRenderers () {
        glyphRenderers.clear();
        this.numberOfGlyphRenderers = 0;
        data = resetData;
    }

    @Override
    public void updateBuffer () {
        for (int i = 0; i < glyphRenderers.size(); i ++) {
            if (glyphRenderers.get(i).isDirty()) {
                load(i);
                glyphRenderers.get(i).setClean();
            }
        }
        super.updateBuffer();
    }

    /**
     * Function for calling loadVertexProperties but also sets up necessary stuff relating
     * to uploading data to the gpu.
     * Always call this function instead of calling loadVertexProperties()
     *
     * @param index index of the sprite to be loaded
     */
    @Override
    protected void load(int index) {
        if (index >= spriteCount) spriteCount++;
//        primitiveVerticesOffset = 0;
        loadVertexProperties(index, getOffset(index));
    }

    /**
     * Adds a Text object to this batch
     *
     * @param
     * @return if the sprite was successfully added to the batch
     */
    public boolean addGlyphRenderer (GlyphRenderer glyphR) {
        // If the batch still has room, and is at the same z index as the sprite, then add it to the batch
        if (hasRoomLeft() && zIndex() == glyphR.getParentText().zIndex()) {
            Texture tex = glyphR.getTexture();
            if (tex == null || (hasTexture(tex) || hasTextureRoom())) {
                // Get the index and add the renderObject
                int index = this.numberOfGlyphRenderers;
//                glyphR.setRendererBatch(this, index);
                this.glyphRenderers.add(glyphR); // = glyphR;
                this.numberOfGlyphRenderers++;

                // Add properties to local vertices array
                load(index);

                if (this.numberOfGlyphRenderers >= this.maxBatchSize) {
                    this.hasRoom = false;
                }
                return true;
            }
        }  else if (!hasRoomLeft()) {
//            Logger.logInfo("Text length has reached the maximum string length of " + (maxBatchSize - 1) + "; this is an artificial cap set for performance reasons.");
        }
        return false;
    }

}
