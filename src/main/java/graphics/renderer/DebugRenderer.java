package graphics.renderer;

import ecs.Component;
import ecs.GameObject;
import graphics.Color;
import graphics.Framebuffer;
import graphics.Shader;
import graphics.Window;
import org.joml.Vector2f;
import util.Assets;
import util.Line;

import java.util.Collections;

public class DebugRenderer extends Renderer<DebugRenderBatch> {
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
	 * @return the created fbo
	 */
	@Override
	protected Framebuffer createFramebuffer() {
		return Framebuffer.createDefault();
	}

	/**
	 * Upload the required uniforms
	 *
	 * @param shader the shader
	 */
	@Override
	protected void uploadUniforms(Shader shader) {
		shader.uploadMat4f("uProjection", Window.currentScene.camera().getProjectionMatrix());
		shader.uploadMat4f("uView", Window.currentScene.camera().getViewMatrix());
	}

	/**
	 * Prepare for rendering. Do anything like setting background here.
	 */
	@Override
	protected void prepare() {
		// Nothing to do :D
	}

	@Override
	public void add(GameObject gameObject) {
		for (Component c : gameObject.getComponents()) {
			Line[] lines = c.debugLines();
			if (lines != null) for (Line line : lines) addLine(line);
		}
	}

	private void addLine(Line l) {
		for (DebugRenderBatch batch : batches) {
			if (batch.addLine(l)) {
				return;
			}
		}

		// If unable to add to previous batch, create a new one
		DebugRenderBatch newBatch = new DebugRenderBatch(10, -10);
		newBatch.start();
		batches.add(newBatch);
		newBatch.addLine(l);
	}
}
