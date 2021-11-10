package graphics.renderer;

import components.GameObject;
import components.SpriteRenderer;
import graphics.Framebuffer;
import graphics.Graphics;
import graphics.Shader;
import ui.UIComponentRenderer;
import util.Assets;
import util.Engine;

import java.util.Collections;

/**
 * <h1>Azurite</h1>
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
        return Assets.getShader("src/assets/shaders/default.glsl");
    }

    /**
     * Create a framebuffer
     *
     * @return the created Framebuffer
     */
    @Override
    protected Framebuffer createFramebuffer() {
        return Framebuffer.createWithColorAttachment();
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

        shader.uploadInt("uLightmap", 8);
    }

    /**
     *
     * @param UIComponentRenderer
     */

    public void add(UIComponentRenderer r) {
        if (r != null) {
            addSpriteRenderer(r);
        }
    }

    /**
     * Remove a UIComponentRenderer from this renderer
     *
     * @param UIComponentRenderer
     */
    public void remove(UIComponentRenderer r) {
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
     * Adds the SpriteRenderer to a single batch, and creates a new batch if their is no space.
     *
     * @param componentRenderer SpriteRenderer: The SpriteRenderer component to be added
     */
    protected void addSpriteRenderer(UIComponentRenderer componentRenderer) {
        for (UIRenderBatch batch : batches) {
            if (batch.addSprite(componentRenderer)) {
                return;
            }
        }
        // If unable to add to previous batch, create a new one
        UIRenderBatch newBatch = new UIRenderBatch(MAX_BATCH_SIZE, componentRenderer.zIndex());
        newBatch.setRenderer(this);
        newBatch.start();
        batches.add(newBatch);
        newBatch.addSprite(componentRenderer);
        Collections.sort(batches);
    }
}
