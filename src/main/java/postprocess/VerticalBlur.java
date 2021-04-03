package postprocess;

import graphics.Framebuffer;
import graphics.Graphics;
import graphics.Shader;
import graphics.Window;
import util.Assets;

public class VerticalBlur extends PostProcessStep {
	private int textureID;

	public VerticalBlur(Target target) {
		super(target);
	}

	@Override
	public Shader createShader() {
		return Assets.getShader("src/assets/shaders/vblur.glsl");
	}

	@Override
	public void prepare() {
		Graphics.background(Graphics.defaultBackground);

		bindTexture(textureID, 0);
	}

	@Override
	protected void uploadUniforms(Shader shader) {
		shader.uploadTexture("uTexture", 0);

		shader.uploadFloat("uPixelSize", framebuffer.isDefault() ? 1.0f / Window.getHeight() : 1.0f / framebuffer.getHeight());
	}

	public void setTexture(int textureID) {
		this.textureID = textureID;
	}
}
