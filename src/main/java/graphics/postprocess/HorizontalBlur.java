package graphics.postprocess;

import graphics.Graphics;
import graphics.Shader;
import graphics.Texture;
import graphics.Window;
import util.Assets;

/**
 * A Post Processing Step that renders the texture with a Horizontal Blur.
 * <p>
 * NOTE: If you want a more blurry texture, consider using Target.ONE_COLOR_HALF_SIZE_TEXTURE_FRAMEBUFFER
 * to get a smaller framebuffer size and in turn, a more blurrier texture
 */
public class HorizontalBlur extends PostProcessStep {
    /**
     * Id of input texture
     */
    private Texture texture;

    /**
     * Default Constructor
     *
     * @param target Where the final texture is to be rendered
     */
    public HorizontalBlur(Target target) {
        super(target);
    }

    /**
     * Create the shader to be used for this step
     *
     * @return the created shader
     */
    @Override
    public Shader createShader() {
        return Assets.getShader("src/assets/shaders/hblur.glsl");
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

        shader.uploadFloat("uPixelSize", framebuffer.isDefault() ? 1.0f / Window.getWidth() : 1.0f / framebuffer.getWidth());
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
