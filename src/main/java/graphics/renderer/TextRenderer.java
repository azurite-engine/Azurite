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
     *
     * @return the created shader
     */
    @Override
    protected Shader createShader() {
        return Assets.getShader("src/assets/shaders/text.glsl");
    }

    /**
     * Create a framebuffer
     *
     * @return the created Framebuffer
     */
    @Override
    protected Framebuffer createFramebuffer() {
        return Framebuffer.createDefault();
    }

    /**
     * Upload uniforms to the shader
     *
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
     * Add a Text object to this renderer
     *
     * @param textObject the Text with renderable glpyhs
     */
    public void add(Text textObject) {
        if (textObject != null) {
            addText(textObject);
        }
    }

    public void removeGlyphRenderer (GlyphRenderer gr) {
        if (gr != null && gr.getBatch() != null) {
            gr.getBatch().removeIndex(gr.getBatchIndex());
        }
    }

    public void removeAllGlyphRenderers (ArrayList<GlyphRenderer> grs) {
        for (int i = grs.size() - 1; i >= 0; i --) {
            removeGlyphRenderer(grs.get(i));
        }
    }

    /**
     * Adds the Text component to a single batch, and creates a new batch if their is no space.
     * @param text Text: The text component to be added
     */
    protected void addText (Text text) {

        boolean createNewBatch = false;
        int continueFromIndex = 0;
        if (batches.size() == 0) createNewBatch = true;
        // If unable to add to previous batch, create a new one
        TextRendererBatch newBatch = new TextRendererBatch(MAX_BATCH_SIZE, text.zIndex());
        newBatch.start();
        batches.add(newBatch);

        for (int i = continueFromIndex; i < text.getGlyphRenderers().size(); i ++) {
            GlyphRenderer g = text.getGlyphRenderers().get(i);
            newBatch.addGlyphRenderer(g);
            g.setRendererBatch(newBatch, newBatch.getSize() - 1);
//            Logger.debugLog("Added GlyphRenderer \"" + g.getCharacter() + "\" (" + (newBatch.getSize() - 1) + ") to new batch.");
        }

        Collections.sort(batches);
    }
}
