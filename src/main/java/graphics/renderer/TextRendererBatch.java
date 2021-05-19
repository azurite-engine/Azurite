package graphics.renderer;

import fonts.GlyphRenderer;
import graphics.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics.Transform;
import util.Logger;

import java.util.ArrayList;

public class TextRendererBatch extends RenderBatch {
    private ArrayList<GlyphRenderer> glyphRenderers; // even though this is an arrayList, maxBatchSize still applies
    private int numberOfGlyphRenderers;

    /**
     * Create a default type render batch
     *
     * @param maxBatchSize maximum number of sprites in the batch
     * @param zIndex       zIndex of the batch. Used for sorting.
     */
    TextRendererBatch(int maxBatchSize, int zIndex) {
        super(maxBatchSize, zIndex, Primitive.QUAD, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT);

        this.glyphRenderers = new ArrayList<>();
        this.numberOfGlyphRenderers = 0;
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
        Vector4f color = Color.WHITE.toNormalizedVec4f();
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
            data[offset] = spr.position.x + (xAdd * spr.scale.x);
            data[offset + 1] = spr.position.y + (yAdd * spr.scale.y);

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

            offset += vertexCount;
        }
    }

    public int getSize () {
        return numberOfGlyphRenderers;
    }

    public void removeIndex (int i) {
        if (glyphRenderers.size() > 0) {
            Logger.logInfo("Removed GlyphRenderer \"" + glyphRenderers.get(i).getCharacter() + "\" (" + i + ") from batch.");
            glyphRenderers.remove(i);
            numberOfGlyphRenderers --;
//            super.updateBuffer();
        }
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
                this.glyphRenderers.add(glyphR); // = glyphR;
                this.numberOfGlyphRenderers++;

                // Add properties to local vertices array
                load(index);

                if (this.numberOfGlyphRenderers >= this.maxBatchSize) {
                    this.hasRoom = false;
                }
                return true;
            }
        }
        return false;
    }

}