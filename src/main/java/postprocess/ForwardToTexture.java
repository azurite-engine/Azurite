package postprocess;

import graphics.Framebuffer;
import graphics.Graphics;
import graphics.Shader;
import util.Assets;

public class ForwardToTexture extends PostProcessStep {
	private int textureID;

	public ForwardToTexture(Target target) {
		super(target);
	}

	@Override
	public Shader createShader() {
		return Assets.getShader("src/assets/shaders/forward.glsl");
	}

	@Override
	public void prepare() {
		Graphics.background(Graphics.defaultBackground);
		bindTexture(textureID, 0);
	}

	@Override
	protected void uploadUniforms(Shader shader) {
		shader.uploadTexture("uTexture", 0);
	}

	public void setTexture(int textureID) {
		this.textureID = textureID;
	}
}
