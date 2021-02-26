package graphics.renderer;

import ecs.GameObject;
import ecs.PointLight;
import ecs.SpriteRenderer;
import graphics.Shader;
import graphics.Texture;
import graphics.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.Assets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
	private final int[] textureSlots = {0, 1, 2, 3, 4, 5, 6, 7};

	private static final int MAX_BATCH_SIZE = 1000;

	private final List<DefaultRenderBatch> batches;
	private final List<PointLight> lights;

	private Shader shader;

	private int numberOfLights;

	public Renderer () {
		this.batches = new ArrayList<>();

		lights = new ArrayList<>();
		this.numberOfLights = 0;
	}

	public void init() {
		shader = Assets.getShader("src/assets/shaders/default.glsl");
	}

	/**
	 * Loop through all render batches and render them
	 */
	public void render () {
		shader.attach();

		uploadUniforms();

		for (DefaultRenderBatch batch : batches) {
			batch.updateBuffer();
			batch.bind();
			glDrawElements(GL_TRIANGLES, batch.getVertexCount(), GL_UNSIGNED_INT, 0);
			batch.unbind();
		}
		shader.detach();
	}

	private void uploadUniforms() {
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
		shader.uploadIntArray("uTextures", textureSlots);
	}

	/**
	 * Add a gameObject to the renderer, and if it contains a component that affects rendering, like a sprite or light, those are added to the batch.
	 * @param gameObject the GameObject with renderable components
	 */
	public void add (GameObject gameObject) {
		SpriteRenderer spr = gameObject.getComponent(SpriteRenderer.class);
		if (spr != null) {
			addSpriteRenderer(spr);
		}

		PointLight light = gameObject.getComponent(PointLight.class);
		if (light != null) {
			addPointLight(light);
		}
	}

	/**
	 * Add A Point Light to the scene.
	 * If you want to change max number of lights in the scene, change all the 10 values to something else
	 * Make sure to change it in shader code as well
	 * @param light the light
	 */
	private void addPointLight(PointLight light) {
		numberOfLights++;
		assert numberOfLights <= 10 : "NO MORE THAN 10 LIGHTS";
		lights.add(light);
	}

	/**
	 * Adds the SpriteRenderer to a single batch, and creates a new batch if their is no space.
	 * @param sprite SpriteRenderer: The SpriteRenderer component to be added
	 */
	private void addSpriteRenderer (SpriteRenderer sprite) {
		boolean added = false;
		for (DefaultRenderBatch batch : batches) {
			// If the batch still has room, and is at the same z index as the sprite, then add it to the batch and break
			if (batch.hasRoomLeft() && batch.zIndex() == sprite.gameObject.zIndex()) {
				Texture tex = sprite.getTexture();
				if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
					batch.addSprite(sprite);
					added = true;
					break;
				}
			}
		}

		// If the conditions for all of the above batches weren't met, create a new one and add to it
		if (!added) {
			// If unable to add to previous batch, create a new one
			DefaultRenderBatch newBatch = new DefaultRenderBatch(MAX_BATCH_SIZE, sprite.gameObject.zIndex());
			newBatch.start();
			batches.add(newBatch);
			newBatch.addSprite(sprite);
			Collections.sort(batches);
		}
	}

	public void clean() {
		batches.forEach(RenderBatch::delete);
	}
}
