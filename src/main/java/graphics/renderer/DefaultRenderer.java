package graphics.renderer;

import ecs.GameObject;
import ecs.PointLight;
import ecs.SpriteRenderer;
import graphics.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.Assets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultRenderer extends Renderer<DefaultRenderBatch> {
	private static final int MAX_BATCH_SIZE = 1000;

	// The light data
	private final List<PointLight> lights;
	private int numberOfLights;



	public DefaultRenderer() {
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
		return Framebuffer.createDefault();
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
		shader.uploadInt("uLightmap", 8);
	}

	/**
	 * Add a gameObject to this renderer
	 *
	 * @param gameObject the GameObject with renderable components
	 */
	@Override
	public void add(GameObject gameObject) {
		SpriteRenderer spr = gameObject.getComponent(SpriteRenderer.class);
		if (spr != null) {
			addSpriteRenderer(spr);
		}
	}

	/**
	 * Prepare for rendering. Do anything like setting background here.
	 */
	@Override
	protected void prepare() {
		Graphics.background(Graphics.defaultBackground);
	}
	/**
	 * Adds the SpriteRenderer to a single batch, and creates a new batch if their is no space.
	 * @param sprite SpriteRenderer: The SpriteRenderer component to be added
	 */
	protected void addSpriteRenderer (SpriteRenderer sprite) {
		for (DefaultRenderBatch batch : batches) {
			if (batch.addSprite(sprite)) {
				return;
			}
		}
		// If unable to add to previous batch, create a new one
		DefaultRenderBatch newBatch = new DefaultRenderBatch(MAX_BATCH_SIZE, sprite.gameObject.zIndex());
		newBatch.start();
		batches.add(newBatch);
		newBatch.addSprite(sprite);
		Collections.sort(batches);
	}
}
