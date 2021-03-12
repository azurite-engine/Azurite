package graphics.renderer;

import ecs.GameObject;
import ecs.SpriteRenderer;
import ecs.Text;
import graphics.Framebuffer;
import graphics.Shader;
import graphics.Window;
import util.Assets;
import util.specs.FramebufferSpec;
import util.specs.FramebufferTextureSpec;

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
        return new Framebuffer(Window.getWidth(), Window.getHeight(), new FramebufferSpec(new FramebufferTextureSpec(FramebufferTextureSpec.FramebufferTextureFormat.RGBA8)));
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
     * Add a gameObject to this renderer
     *
     * @param gameObject the GameObject with renderable components
     */
    @Override
    public void add(GameObject gameObject) {
        Text t = gameObject.getComponent(Text.class);
        if (t != null) {
            addSpriteRenderer(t);
        }
    }

    @Override
    protected void prepare() {}

    /**
     * Adds the Text component to a single batch, and creates a new batch if their is no space.
     * @param text Text: The text component to be added
     */
    protected void addSpriteRenderer (Text text) {
        for (TextRendererBatch batch : batches) {
            if (batch.addText(text)) {
                return;
            }
        }
        // If unable to add to previous batch, create a new one
        TextRendererBatch newBatch = new TextRendererBatch(MAX_BATCH_SIZE, text.gameObject.zIndex());
        newBatch.start();
        batches.add(newBatch);
        newBatch.addText(text);
        Collections.sort(batches);
    }
}
