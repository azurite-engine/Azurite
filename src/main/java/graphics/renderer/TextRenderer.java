package graphics.renderer;

import ecs.Text;
import fonts.GlyphRenderer;
import graphics.Framebuffer;
import graphics.Shader;
import util.Assets;
import util.Engine;
import util.Logger;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Asher Haun
 */

public class TextRenderer extends Renderer<TextRendererBatch> {
    private static final int MAX_BATCH_SIZE = 1000;

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
     * Upload uniforms to the shader
     * @param shader the shader
     */
    @Override
    protected void uploadUniforms(Shader shader) {
        shader.uploadIntArray("uTextures", textureSlots);
        shader.uploadMat4f("uProjection", Engine.scenes().currentScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Engine.scenes().currentScene().camera().getViewMatrix());
    }

    @Override
    protected void prepare() {}

    /**
     * This method is called when the user wants to modify the string in the Text object.
     * This method is called by the Text.change(String) method.
     * @param textObject the Text object that is being modified
     * @param textBatch the batch that the previously mentioned Text object belongs to.
     */
    public void changeText (Text textObject, TextRendererBatch textBatch) {
        textBatch.removeGlyphRenderers();

        for (GlyphRenderer g : textObject.getGlyphRenderers()) {
            textBatch.addGlyphRenderer(g);
            g.setRendererBatch(textBatch, textBatch.getSize() - 1);
        }
    }

    /**
     * Add a Text object to this renderer
     * @param textObject the Text with renderable glyphs
     */
    public void add(Text textObject) {
        if (textObject != null) {
            addText(textObject);
        }
    }

    /**
     * Adds the Text component to a single batch
     * @param text Text: The text component to be added
     */
    protected void addText (Text text) {
        TextRendererBatch newBatch = new TextRendererBatch(MAX_BATCH_SIZE, text.zIndex());
        newBatch.start();
        batches.add(newBatch);

        for (GlyphRenderer g : text.getGlyphRenderers()) {
            newBatch.addGlyphRenderer(g);
            g.setRendererBatch(newBatch, newBatch.getSize() - 1);
        }

        Collections.sort(batches);
    }

    public static int getMaxBatchSize () {
        return MAX_BATCH_SIZE;
    }
}
