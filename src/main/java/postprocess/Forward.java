package postprocess;

import graphics.Framebuffer;
import graphics.Graphics;
import graphics.Shader;
import util.Assets;

import java.util.function.Supplier;

public class Forward extends PostProcessStep {
	private int textureID;

	public Forward(Supplier<Framebuffer> fboSupplier) {
		super(fboSupplier);
	}

	@Override
	public Shader getShader() {
		return Assets.getShader("src/assets/shaders/forward.glsl");
	}

	@Override
	public void prepare() {
		bindTexture(textureID, 0);
		shader.uploadTexture("uTexture", 0);

		Graphics.background(0);
	}

	public void blit() {
		fbo.blitColorBuffersToScreen();
	}

	public void setTexture(int textureID) {
		this.textureID = textureID;
	}
}
