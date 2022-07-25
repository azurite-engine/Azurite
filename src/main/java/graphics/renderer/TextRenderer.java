package graphics.renderer;

import graphics.Framebuffer;
import graphics.Primitive;
import graphics.Shader;
import graphics.ShaderDatatype;
import org.joml.Vector2f;
import ui.Text;
import ui.fonts.GlyphRenderer;
import util.Assets;
import util.Engine;
import util.Transform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Asher Haun
 */

public class TextRenderer extends Renderer {
    private static final int MAX_BATCH_SIZE = 1000;

    private final List<Text> texts;

    public TextRenderer() {
        texts = new ArrayList<>();
    }

    /**
     * Create a shader
     * @return the created shader
     */
    @Override
    protected Shader createShader() {
        return Assets.getShader("src/assets/shaders/text.glsl");
    }

    /**
     * Create a framebuffer
     * @return the created Framebuffer
     */
    @Override
    protected Framebuffer createFramebuffer() {
        return Framebuffer.createDefault();
    }

    /**
     * Create a new Batch with appropriate parameters
     *
     * @param zIndex
     * @return a new batch
     */
    @Override
    protected RenderBatch createBatch(int zIndex) {
        return new RenderBatch(500, zIndex, Primitive.QUAD,
                ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT, ShaderDatatype.FLOAT);
    }

    /**
     * Upload uniforms to the shader
     * @param shader the shader
     */
    @Override
    protected void uploadUniforms(Shader shader) {
        shader.uploadIntArray("uTextures", textureSlots);
        shader.uploadMat4f("uProjection", Engine.scenes().currentScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Engine.scenes().currentScene().camera().getViewMatrix());
    }

    /**
     * Rebuffer all the data into batches
     */
    @Override
    protected void rebuffer() {
        for (Text text : texts) {
            ArrayList<GlyphRenderer> glyphs = text.getGlyphRenderers();
            for (GlyphRenderer glyph : glyphs) {
                RenderBatch batch = getAvailableBatch(glyph.getTexture(), text.zIndex());
                pushGlyph(batch, glyph);
            }
        }
    }

    /**
     * Push a glyph to the batch
     * @param batch the batch to which to push the glyph
     * @param glyph the glyph which to push to the batch
     */
    private static void pushGlyph(RenderBatch batch, GlyphRenderer glyph) {
        Transform spr = glyph.getLocalTransform();
        Vector2f[] textureCoordinates = glyph.getTexCoords();

        int textureID;
        if (glyph.getTexture() != null) textureID = batch.addTexture(glyph.getTexture());
        else textureID = 0;

        // Push verts to the batch
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1: yAdd = 0.0f; break;
                case 2: xAdd = 0.0f; break;
                case 3: yAdd = 1.0f; break;
            }
            batch.pushVec2(spr.getPosition().x + (xAdd * spr.scale.x), spr.getPosition().y + (yAdd * spr.scale.y));
            batch.pushColor(glyph.getColor());
            batch.pushVec2(textureCoordinates[i]);
            batch.pushInt(textureID);
            batch.pushFloat(glyph.isSticky() ? 1.f : 0.f);
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    protected void prepare() {}

    /**
     * Add a Text object to this renderer
     * @param textObject the Text with renderable glyphs
     */
    public void add(Text textObject) {
        if (textObject != null) {
            texts.add(textObject);
        }
    }

    public static int getMaxBatchSize () {
        return MAX_BATCH_SIZE;
    }

    public void remove(Text text) {
        if (text != null) {
            texts.remove(text);
        }
    }
}
