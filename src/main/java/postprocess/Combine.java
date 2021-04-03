package postprocess;

import graphics.Graphics;
import graphics.Shader;
import util.Assets;

public class Combine extends PostProcessStep {
	private int textureA;
	private int textureB;
	private float weightA;
	private float weightB;

	public Combine(Target target) {
		super(target);
		weightA = 1.0f;
		weightB = 1.0f;
	}

	@Override
	public Shader createShader() {
		return Assets.getShader("src/assets/shaders/combine.glsl");
	}

	@Override
	public void prepare() {
		Graphics.background(Graphics.defaultBackground);

		bindTexture(textureA, 0);
		bindTexture(textureB, 1);
	}

	@Override
	protected void uploadUniforms(Shader shader) {
		shader.uploadTexture("uTextureA", 0);
		shader.uploadTexture("uTextureB", 1);
		shader.uploadFloat("weightA", weightA);
		shader.uploadFloat("weightB", weightB);
	}

	public void setTextureA(int textureID) {
		this.textureA = textureID;
	}

	public void setTextureB(int textureID) {
		this.textureB = textureID;
	}

	public void setWeightA(float weightA) {
		this.weightA = weightA;
	}

	public void setWeightB(float weightB) {
		this.weightB = weightB;
	}
}
