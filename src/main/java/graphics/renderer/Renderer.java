package graphics.renderer;//{This comment is intentionally added to create a git merge conflict}

import ecs.GameObject;
import graphics.Framebuffer;
import graphics.Shader;
import graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;

public abstract class Renderer<T extends RenderBatch> {
    /**
     * Texture slots to be uploaded to the shader. You don't have to upload them in your custom renderer.
     */
    protected final int[] textureSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    /**
     * A list of batches
     */
    protected final List<T> batches;
    /**
     * Framebuffer to which this renderer will render
     */
    public Framebuffer framebuffer;
    /**
     * Shader to be used for rendering
     */
    private Shader shader;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    /**
     * Create a shader
     *
     * @return the created shader
     */
    protected abstract Shader createShader();

    /**
     * Create a framebuffer
     *
     * @return the created fbo
     */
    protected abstract Framebuffer createFramebuffer();

    /**
     * Upload the required uniforms
     *
     * @param shader the shader
     */
    protected abstract void uploadUniforms(Shader shader);

    /**
     * Add a gameObject to the renderer, and if it contains a component that affects rendering, like a sprite or light, those are added to a batch.
     *
     * @param gameObject the GameObject with renderable components
     */
    public void add(GameObject gameObject) {
    }

    /**
     * Remove a gameObject from the renderer if it contains the component that gets rendered.
     *
     * @param gameObject the GameObject with renderable components
     */
    public void remove(GameObject gameObject) {
    }

    /**
     * Creates the renderer's shader and framebuffer
     */
    public void init() {
        shader = createShader();
        framebuffer = createFramebuffer();
    }

    /**
     * Get a color attachment texture from the framebuffer
     *
     * @param index index of the required color attachment texture. Will return -1 if there is no attachment at that index.
     * @return the texture ID of the attachment
     */
    public Texture fetchColorAttachment(int index) {
        return framebuffer.getColorAttachment(index);
    }

    /**
     * Loop through all render batches and render them
     */
    public void render() {
        framebuffer.bind();
        prepare();
        shader.attach();
        uploadUniforms(shader);
        for (T batch : batches) {
            batch.updateBuffer();
            batch.bind();
            glDrawElements(batch.primitive.openglPrimitive, batch.getVertexCount(), GL_UNSIGNED_INT, 0);
            batch.unbind();
        }
        shader.detach();
        Framebuffer.unbind();
    }

    /**
     * Prepare for rendering. Do anything like setting background here.
     */
    protected abstract void prepare();

    /**
     * Delete all the Batches.
     */
    public void clean() {
        batches.forEach(RenderBatch::delete);
    }
}
