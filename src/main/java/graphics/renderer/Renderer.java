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

public abstract class Renderer<T extends RenderBatch> {
	private final int[] textureSlots = {0, 1, 2, 3, 4, 5, 6, 7};

	protected final List<T> batches;

	private Shader shader;

	public Renderer () {
		this.batches = new ArrayList<>();
	}

	protected abstract Shader getShader();
	protected abstract void uploadUniforms(Shader shader);
	public abstract T createBatch(int zIndex);

	/**
	 * Add a gameObject to the renderer, and if it contains a component that affects rendering, like a sprite or light, those are added to the batch.
	 * @param gameObject the GameObject with renderable components
	 */
	public void add(GameObject gameObject) {

	}

	public void init() {
		shader = getShader();
	}

	/**
	 * Loop through all render batches and render them
	 */
	public void render () {
		shader.attach();

		_uploadUniforms();

		for (T batch : batches) {
			batch.updateBuffer();
			batch.bind();
			glDrawElements(GL_TRIANGLES, batch.getVertexCount(), GL_UNSIGNED_INT, 0);
			batch.unbind();
		}
		shader.detach();
	}

	private void _uploadUniforms() {
		shader.uploadMat4f("uProjection", Window.currentScene.camera().getProjectionMatrix());
		shader.uploadMat4f("uView", Window.currentScene.camera().getViewMatrix());
		shader.uploadIntArray("uTextures", textureSlots);

		uploadUniforms(shader);
	}

	public void clean() {
		batches.forEach(RenderBatch::delete);
	}
}
