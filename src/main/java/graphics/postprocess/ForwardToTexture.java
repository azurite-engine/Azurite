package graphics.postprocess;

import graphics.Graphics;
import graphics.Shader;
import graphics.Texture;
import util.Assets;

/**
 * A Post Processing Step that renders the texture as-is to another framebuffer.
 * Perfect for debugging steps.
 * <p>
 * NOTE: If you want to forward any texture to the screen, the scene class has a forwardToScreen field that can be used
 */
public class ForwardToTexture extends PostProcessStep {
    /**
     * Id of input texture
     */
    private Texture texture;

    /**
     * Default Constructor
     *
     * @param target Where the final texture is to be rendered
     */
    public ForwardToTexture(Target target) {
        super(target);
    }

    /**
     * Create the shader to be used for this step
     *
     * @return the created shader
     */
    @Override
    public Shader createShader() {
        return Assets.getShader("src/assets/shaders/forward.glsl");
    }

    /**
     * Prepare the framebuffer by clearing it and binding any textures required
     */
    @Override
    public void prepare() {
        Graphics.background(Graphics.defaultBackground);
        texture.bindToSlot(0);
    }

    /**
     * Upload uniforms to the shader
     */
    @Override
    protected void uploadUniforms(Shader shader) {
        shader.uploadTexture("uTexture", 0);
    }

    /**
     * Set the input texture
     *
     * @param texture input texture
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
