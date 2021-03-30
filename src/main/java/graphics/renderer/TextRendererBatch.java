package graphics.renderer;

import fonts.GlyphRenderer;
import graphics.Color;
import graphics.Primitive;
import graphics.ShaderDatatype;
import graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics.Transform;

public class TextRendererBatch extends RenderBatch {
    private GlyphRenderer[] glyphRenderers;
    private int numberOfGlyphRenderers;

    /**
     * Create a default type render batch
     *
     * @param maxBatchSize maximum number of sprites in the batch
     * @param zIndex       zIndex of the batch. Used for sorting.
     */
    TextRendererBatch(int maxBatchSize, int zIndex) {
        super(maxBatchSize, zIndex, Primitive.QUAD, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT);

        this.glyphRenderers = new GlyphRenderer[maxBatchSize];
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
        GlyphRenderer glyphRenderer = glyphRenderers[index]; // todo
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

    /**
     * Checks if any sprite is dirty (has changed any of its properties).
     * If so, resets its data in the data[] via load().
     *
     * Calls the RenderBatch::updateBuffer method to re-upload the data if required
     */
//    public void updateBuffer() {
//        for (int i = 0; i < numberOfGlyphRenderers; i ++) {
//            GlyphRenderer gr = glyphRenderers[i];
//            if (gr.isDirty()) {
//                load(i);
//                gr.setClean();
//            }
//        }
//        super.updateBuffer();
//    }

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
                this.glyphRenderers[index] = glyphR;
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
