package graphics.renderer;

import ecs.GameObject;
import ecs.PointLight;
import graphics.Color;
import graphics.Framebuffer;
import graphics.Graphics;
import graphics.shaders.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.Assets;
import util.Engine;

import java.util.ArrayList;
import java.util.List;

public class LightmapRenderer extends Renderer<QuadRenderBatch> {
	private static final int MAX_BATCH_SIZE = 1000;

	// The light data
	private final List<PointLight> lights;

	private QuadRenderBatch quadBatch;

	public LightmapRenderer() {
		lights = new ArrayList<>();
	}

	@Override
	public void init() {
		super.init();
		quadBatch = new QuadRenderBatch();
		quadBatch.setRenderer(this);
		quadBatch.start();
		quadBatch.loadQuad();
		batches.add(quadBatch);
	}

	/**
	 * Create a shader
	 *
	 * @return the created shader
	 */
	@Override
	protected ShaderProgram createShader() {
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
	protected void uploadUniforms(ShaderProgram shader) {
		// This is here so that all renderers can have different cameras OR no cameras at all
		shader.uploadMat4f("uProjection", Engine.window().currentScene().camera().getProjectionMatrix());
		shader.uploadVec2f("uCameraOffset", Engine.window().currentScene().camera().getPosition());

		// Set lighting uniforms
		Vector2f[] lightPositions = new Vector2f[lights.size()];
		Vector3f[] lightColors = new Vector3f[lights.size()];
		float[] lightIntensities = new float[lights.size()];

		for (int i = 0; i < lights.size(); i++) {
			PointLight light = lights.get(i);
			lightPositions[i] = light.lastTransform.getPosition();
			lightColors[i] = light.color;
			lightIntensities[i] = light.intensity;
		}

		shader.uploadVec2fArray("uLightPosition", lightPositions);
		shader.uploadVec3fArray("uLightColor", lightColors);
		shader.uploadFloatArray("uIntensity", lightIntensities);
		shader.uploadFloat("uMinLighting", Engine.scenes().getMinSceneLight());
		shader.uploadInt("uNumLights", lights.size());
	}

	/**
	 * Add a gameObject to this renderer
	 * @param gameObject the GameObject with renderable components
	 */
	@Override
	public void add(GameObject gameObject) {
		PointLight l = gameObject.getComponent(PointLight.class);
		if (l != null) {
			if (lights.contains(l)) return;
			lights.add(l);
			l.setLocation(quadBatch, -1);
			assert lights.size() <= 10 : "NO MORE THAN 10 LIGHTS";
		}
	}

	/**
	 * Remove a gameObject from this renderer
	 * @param gameObject the GameObject with renderable components
	 */
	@Override
	public void remove(GameObject gameObject) {
		PointLight l = gameObject.getComponent(PointLight.class);
		if (l != null) {
			lights.remove(l);
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
		framebuffer.getColorAttachment(0).bindToSlot(8);
	}
}
