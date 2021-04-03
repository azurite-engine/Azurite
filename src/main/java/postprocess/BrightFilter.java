package postprocess;

import graphics.Graphics;
import graphics.Shader;
import util.Assets;

public class BrightFilter extends PostProcessStep {
	private int textureID;

	public BrightFilter(Target target) {
		super(target);
	}

	@Override
	public Shader createShader() {
		return Assets.getShader("src/assets/shaders/brights.glsl");
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
