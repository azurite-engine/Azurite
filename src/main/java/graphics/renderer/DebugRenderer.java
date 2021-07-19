package graphics.renderer;

import ecs.Component;
import ecs.GameObject;
import graphics.Framebuffer;
import graphics.Shader;
import util.Assets;
import util.Engine;
import util.debug.DebugLine;
import util.debug.DebugPrimitive;

import static org.lwjgl.opengl.GL11.glLineWidth;

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
		shader.uploadMat4f("uProjection", Engine.window().currentScene().camera().getProjectionMatrix());
		shader.uploadMat4f("uView", Engine.window().currentScene().camera().getViewMatrix());
	}

	/**
	 * Prepare for rendering. Do anything like setting background here.
	 */
	@Override
	protected void prepare() {
		glLineWidth(3);
	}

	/**
	 * Add a gameObject to this renderer
	 *
	 * @param gameObject the gameObject
	 */
	@Override
	public void add(GameObject gameObject) {
		for (Component c : gameObject.getComponents()) {
			DebugPrimitive[] primitives = c.debug();
			if (primitives != null)
				for (DebugPrimitive primitive : primitives) {
					for (DebugLine line : primitive.getLines())
						addLine(line);
				}
		}
	}

	/**
	 * Remove a gameObject from this renderer
	 *
	 * @param gameObject the gameObject
	 */
	@Override
	public void remove(GameObject gameObject) {
		for (Component c : gameObject.getComponents()) {
			DebugPrimitive[] primitives = c.debug();
			if (primitives != null)
				for (DebugPrimitive primitive : primitives) {
					for (DebugLine line : primitive.getLines())
						removeLine(line);
				}
		}
	}

	/**
	 * Add a line to an available batch
	 *
	 * @param line the line to be added
	 */
	private void addLine(DebugLine line) {
		for (DebugRenderBatch batch : batches) {
			if (batch.addLine(line)) {
				return;
			}
		}

		// If unable to add to previous batch, create a new one
		DebugRenderBatch newBatch = new DebugRenderBatch(50, -10);
		newBatch.start();
		batches.add(newBatch);
		newBatch.addLine(line);
	}

	/**
	 * Remove the line from the batch it had been added to
	 *
	 * @param line the line to be removed
	 */
	private void removeLine(DebugLine line) {
		line.getBatch().removeLine(line);
	}
}
