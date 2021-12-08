package graphics.renderer;

import graphics.Framebuffer;
import graphics.Graphics;
import graphics.Shader;
import ui.ElementRenderer;
import util.Assets;
import util.Engine;

import java.util.Collections;

/**
 * Used to render sprites, which are rendered as {@code Primitive.QUAD}s
 * with textures. This should be used to render any renderable {@code gameObject}.
 *
 * @see UIRenderBatch
 */
public class UIRenderer extends Renderer<UIRenderBatch> {
    private static final int MAX_BATCH_SIZE = 1000;

    /**
     * Create a shader
     *
     * @return the created shader
     */
    @Override
    protected Shader createShader() {
        return Assets.getShader("src/assets/shaders/ui.glsl");
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

        // This is here so that all renderers can have different cameras OR no cameras at all
        shader.uploadMat4f("uProjection", Engine.window().currentScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Engine.window().currentScene().camera().getViewMatrix());
    }

    /**
     *
     * @param e UI ElementRenderer
     */
    public void add(ElementRenderer e) {
        if (e == null) return;
        addElementRenderer(e);
    }

    /**
     * Remove a UIComponentRenderer from this renderer
     *
     * @param r UIComponentRenderer
     */
    @Override
    public void remove(ElementRenderer r) {
        if (r != null) {
            r.markDirty();
            r.remove();
            r.getBatch().removeSprite(r);
        }
    }

    /**
     * Prepare for rendering. Do anything like setting background here.
     */
    @Override
    protected void prepare() {
        Graphics.background(Graphics.defaultBackground);
    }

    /**
     * Adds the ElementRenderer to a single batch, and creates a new batch if their is no space.
     *
     * @param elementRenderer elementRenderer: The ElementRenderer to be added
     */
    protected void addElementRenderer(ElementRenderer elementRenderer) {
        for (UIRenderBatch batch : batches) {
            if (batch.addElementRenderer(elementRenderer)) return;
        }

        UIRenderBatch newBatch = new UIRenderBatch(MAX_BATCH_SIZE, elementRenderer.zIndex());
        newBatch.setRenderer(this);
        newBatch.start();
        batches.add(newBatch);
        newBatch.addElementRenderer(elementRenderer);

        Collections.sort(batches);
    }
}
