package postprocess;

import graphics.Framebuffer;
import graphics.Shader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public abstract class PostProcessStep {
	private Shader shader;
	public Framebuffer framebuffer;
	private Target target;

	public PostProcessStep(Target target) {
		this.target = target;
	}

	public abstract Shader createShader();
	public abstract void prepare();
	protected abstract void uploadUniforms(Shader shader);

	protected Framebuffer createFramebuffer() {
		switch (target) {
			case DEFAULT_FRAMEBUFFER: return Framebuffer.createDefault();
			case ONE_COLOR_TEXTURE_FRAMEBUFFER: return Framebuffer.createWithColorAttachment();
			default: return null;
		}
	}

	public void init() {
		shader = createShader();
		framebuffer = createFramebuffer();
	}

	public int apply() {
		framebuffer.bind();
		shader.attach();
		prepare();
		uploadUniforms(shader);

		PostProcessQuad.bindQuad();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
		PostProcessQuad.unbindQuad();

		shader.detach();
		Framebuffer.unbind();
		return framebuffer.isDefault() ? -1 : framebuffer.fetchColorAttachment(0);
	}

	protected void bindTexture(int texture, int slot) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
	}

	public void blit() {
		framebuffer.blitColorBuffersToScreen();
	}

	public enum Target {
		DEFAULT_FRAMEBUFFER,
		ONE_COLOR_TEXTURE_FRAMEBUFFER
	}
}
