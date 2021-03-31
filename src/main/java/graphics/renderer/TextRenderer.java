package graphics.renderer;

import ecs.GameObject;
import ecs.SpriteRenderer;
import ecs.Text;
import fonts.Glyph;
import fonts.GlyphRenderer;
import graphics.Framebuffer;
import graphics.Shader;
import graphics.Window;
import util.Assets;
import util.Logger;
import util.specs.FramebufferSpec;
import util.specs.FramebufferTextureSpec;

import java.util.ArrayList;
import java.util.Collections;

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
        shader.uploadMat4f("uProjection", Window.currentScene.camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.currentScene.camera().getViewMatrix());
    }

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

    @Override
    protected void prepare() {}

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

        for (TextRendererBatch batch : batches) {
            for (int i = 0; i < text.getGlyphRenderers().size(); i ++) {

                GlyphRenderer g = text.getGlyphRenderers().get(i);

                if (!batch.addGlyphRenderer(g)) {
                    // If unable to add to current batch, create a new one
                    createNewBatch = true;
                    continueFromIndex = i;
                } else {
                    g.setRendererBatch(batch, batch.getSize() - 1);
                    Logger.debugLog("Added GlyphRenderer \"" + g.getCharacter() + "\" (" + (batch.getSize() - 1) + ") to existing batch.");
                }

            }
        }

        if (createNewBatch) {
            // If unable to add to previous batch, create a new one
            TextRendererBatch newBatch = new TextRendererBatch(MAX_BATCH_SIZE, text.zIndex());
            newBatch.start();
            batches.add(newBatch);

            for (int i = continueFromIndex; i < text.getGlyphRenderers().size(); i ++) {
                GlyphRenderer g = text.getGlyphRenderers().get(i);
                newBatch.addGlyphRenderer(g);
                g.setRendererBatch(newBatch, newBatch.getSize() - 1);
                Logger.debugLog("Added GlyphRenderer \"" + g.getCharacter() + "\" (" + (newBatch.getSize() - 1) + ") to new batch.");
            }

            Collections.sort(batches);
        }
    }
}
