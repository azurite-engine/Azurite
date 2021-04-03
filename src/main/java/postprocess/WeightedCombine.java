package postprocess;

import graphics.Graphics;
import graphics.Shader;
import util.Assets;

/**
 * A Post Processing Step that combines two textures multiplying them
 *  with corresponding weights and then adding them
 */
public class WeightedCombine extends PostProcessStep {
	/** Id of input texture A */
	private int textureA;
	/** Id of input texture B */
	private int textureB;
	/** Weight of input texture A */
	private float weightA;
	/** Weight of input texture B */
	private float weightB;

	/**
	 * Default Constructor
	 * @param target Where the final texture is to be rendered
	 */
	public WeightedCombine(Target target) {
		super(target);
		weightA = 1.0f;
		weightB = 1.0f;
	}

	/**
	 * Create the shader to be used for this step
	 * @return the created shader
	 */
	@Override
	public Shader createShader() {
		return Assets.getShader("src/assets/shaders/combine.glsl");
	}

	/**
	 * Prepare the framebuffer by clearing it and binding any textures required
	 */
	@Override
	public void prepare() {
		Graphics.background(Graphics.defaultBackground);

		bindTexture(textureA, 0);
		bindTexture(textureB, 1);
	}

	/**
	 * Upload uniforms to the shader
	 */
	@Override
	protected void uploadUniforms(Shader shader) {
		shader.uploadTexture("uTextureA", 0);
		shader.uploadTexture("uTextureB", 1);
		shader.uploadFloat("weightA", weightA);
		shader.uploadFloat("weightB", weightB);
	}

	/**
	 * Set the input texture A's id
	 * @param textureID input texture id
	 */
	public void setTextureA(int textureID) {
		this.textureA = textureID;
	}

	/**
	 * Set the input texture B's id
	 * @param textureID input texture id
	 */
	public void setTextureB(int textureID) {
		this.textureB = textureID;
	}

	/**
	 * Set the input texture A's weight
	 * @param weightA input weight
	 */
	public void setWeightA(float weightA) {
		this.weightA = weightA;
	}

	/**
	 * Set the input texture B's weight
	 * @param weightB input weight
	 */
	public void setWeightB(float weightB) {
		this.weightB = weightB;
	}
}
