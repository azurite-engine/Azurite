package graphics.renderer;

import ecs.GameObject;
import ecs.PointLight;
import event.Events;
import graphics.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import util.Assets;
import util.specs.FramebufferSpec;
import util.specs.FramebufferTextureSpec;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class LightmapRenderer extends Renderer<QuadRenderBatch> {
	private static final int MAX_BATCH_SIZE = 1000;

	// The light data
	private final List<PointLight> lights;
	private int numberOfLights;

	public LightmapRenderer() {
		lights = new ArrayList<>();
		this.numberOfLights = 0;
	}

	@Override
	public void init() {
		super.init();
		QuadRenderBatch qb = new QuadRenderBatch();
		qb.start();
		qb.loadQuad();
		batches.add(qb);
	}

	/**
	 * Create a shader
	 *
	 * @return the created shader
	 */
	@Override
	protected Shader createShader() {
		return Assets.getShader("src/assets/shaders/lightmap.glsl");
	}

	/**
	 * Create a framebuffer
	 *
	 * @return the created Framebuffer
	 */
	@Override
	protected Framebuffer createFramebuffer() {
		return Framebuffer.createWithColorAttachment();
	}

	/**
	 * Upload uniforms to the shader
	 *
	 * @param shader the shader
	 */
	@Override
	protected void uploadUniforms(Shader shader) {
		// This is here so that all renderers can have different cameras OR no cameras at all
		shader.uploadMat4f("uProjection", Window.currentScene.camera().getProjectionMatrix());
		shader.uploadVec2f("uCameraOffset", Window.currentScene.camera().getPosition());

		// Set lighting uniforms
		Vector2f[] lightPositions = new Vector2f[numberOfLights];
		Vector3f[] lightColors = new Vector3f[numberOfLights];
		float[] lightIntensities = new float[numberOfLights];

		for (int i = 0; i < numberOfLights; i++) {
			PointLight light = lights.get(i);
			lightPositions[i] = light.lastTransform.getPosition();
			lightColors[i] = light.color;
			lightIntensities[i] = light.intensity;
		}

		shader.uploadVec2fArray("uLightPosition", lightPositions);
		shader.uploadVec3fArray("uLightColor", lightColors);
		shader.uploadFloatArray("uIntensity", lightIntensities);
		shader.uploadFloat("uMinLighting", Window.currentScene.minLighting);
		shader.uploadInt("uNumLights", numberOfLights);
	}

	@Override
	public void add(GameObject gameObject) {
		PointLight l = gameObject.getComponent(PointLight.class);
		if (l != null) {
			numberOfLights++;
			assert numberOfLights <= 10 : "NO MORE THAN 10 LIGHTS";
			lights.add(l);
		}
	}

	/**
	 * Prepare for rendering. Do anything like setting background here.
	 */
	@Override
	protected void prepare() {
		Graphics.background(Color.WHITE);
	}

	public void bindLightmap() {
		GL13.glActiveTexture(GL13.GL_TEXTURE8);
		GL11.glBindTexture(GL_TEXTURE_2D, framebuffer.fetchColorAttachment(0));
	}
}
