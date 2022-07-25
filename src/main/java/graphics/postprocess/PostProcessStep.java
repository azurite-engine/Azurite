package graphics.postprocess;

import graphics.Framebuffer;
import graphics.Shader;
import graphics.Texture;
import org.lwjgl.opengl.GL11;

import java.util.function.Supplier;

/**
 * Denotes A Step in Post Processing
 */
public abstract class PostProcessStep {
    /**
     * The framebuffer to which to render to
     */
    public Framebuffer framebuffer;
    /**
     * The shader to be used for this step
     */
    private Shader shader;
    /**
     * Enum to show where to render. Framebuffer gets constructed based on this.
     */
    private Target target;

    public PostProcessStep(Target target) {
        this.target = target;
    }

    /**
     * Create the shader to be used for this step
     */
    public abstract Shader createShader();

    /**
     * Prepare the framebuffer by clearing it and binding any textures required
     */
    public abstract void prepare();

    /**
     * Upload uniforms to the shader
     */
    protected abstract void uploadUniforms(Shader shader);

    /**
     * Create Framebuffer based on target
     */
    protected Framebuffer createFramebuffer() {
        return target.createFramebuffer.get();
    }

    /**
     * Create the shader and framebuffer
     */
    public void init() {
        shader = createShader();
        framebuffer = createFramebuffer();
    }

    /**
     * Run this Step
     *
     * @return id of the texture if the framebuffer to render to is not default.
     */
    public Texture apply() {
        framebuffer.bind();
        shader.attach();
        prepare();
        uploadUniforms(shader);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);

        shader.detach();
        Framebuffer.unbind();
        return framebuffer.isDefault() ? null : framebuffer.getColorAttachment(0);
    }

    /**
     * Enum to show where to render. Framebuffer gets constructed based on this.
     */
    public enum Target {
        DEFAULT_FRAMEBUFFER(Framebuffer::createDefault),
        ONE_COLOR_TEXTURE_FRAMEBUFFER(Framebuffer::createWithColorAttachment),
        ONE_COLOR_HALF_SIZE_TEXTURE_FRAMEBUFFER(Framebuffer::createHalfResWithColorAttachment);

        public Supplier<Framebuffer> createFramebuffer;
        Target(Supplier<Framebuffer> createFramebuffer) {
            this.createFramebuffer = createFramebuffer;
        }
    }
}
