package scenes;

import ecs.GameObject;
import ecs.PointLight;
import ecs.SpriteRenderer;
import graphics.Framebuffer;
import graphics.Graphics;
import graphics.Shader;
import graphics.Window;
import graphics.renderer.DefaultRenderBatch;
import graphics.renderer.Renderer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.Assets;
import util.specs.FramebufferSpec;
import util.specs.FramebufferTextureSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL11.glClearColor;

public class TestRenderer extends Renderer<DefaultRenderBatch> {
	private static final int MAX_BATCH_SIZE = 1000;

	// The light data
	private final List<PointLight> lights;
	private int numberOfLights;

	public TestRenderer() {
		lights = new ArrayList<>();
		this.numberOfLights = 0;
	}

	/**
	 * Create a shader
	 *
	 * @return the created shader
	 */
	@Override
	protected Shader createShader() {
		return Assets.getShader("src/assets/shaders/default.glsl");
	}

	/**
	 * Create a framebuffer
	 *
	 * @return the created Framebuffer
	 */
	@Override
	protected Framebuffer createFramebuffer() {
		return new Framebuffer(Window.getWidth(), Window.getHeight(), new FramebufferSpec(new FramebufferTextureSpec(FramebufferTextureSpec.FramebufferTextureFormat.RGBA8)));
	}

	/**
	 * Upload uniforms to the shader
	 *
	 * @param shader the shader
	 */
	@Override
	protected void uploadUniforms(Shader shader) {
		shader.uploadIntArray("uTextures", textureSlots);

		// This is here so that all renderers can have different cameras OR no cameras at all
		shader.uploadMat4f("uProjection", Window.currentScene.camera().getProjectionMatrix());
		shader.uploadMat4f("uView", Window.currentScene.camera().getViewMatrix());

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
	protected void prepare() {
		Graphics.background(Graphics.defaultBackground);
	}
}
