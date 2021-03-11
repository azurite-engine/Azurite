package graphics.renderer;

import ecs.Component;
import ecs.GameObject;
import graphics.Framebuffer;
import graphics.Shader;
import graphics.Window;
import util.Assets;
import util.debug.DebugLine;
import util.debug.DebugPrimitive;

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
			DebugPrimitive[] primitives = c.debugLines();
			if (primitives != null)
				for (DebugPrimitive primitive : primitives) {
					for (DebugLine line : primitive.getLines())
						addLine(line);
			}
		}
	}

	private void addLine(DebugLine l) {
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
