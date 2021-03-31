package postprocess;

import graphics.Framebuffer;
import graphics.Shader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.function.Supplier;

public abstract class PostProcessStep {
	protected Shader shader;
	public Framebuffer fbo;
	private Supplier<Framebuffer> fboSupplier;

	public PostProcessStep(Supplier<Framebuffer> fboSupplier) {
		this.fboSupplier = fboSupplier;
	}

	public abstract Shader getShader();
	public abstract void prepare();

	public void init() {
		shader = getShader();
		fbo = fboSupplier.get();
	}

	public int apply() {
		fbo.bind();
		shader.attach();
		prepare();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
		shader.detach();
		Framebuffer.unbind();
		return fbo.isDefault() ? -1 : fbo.fetchColorAttachment(0);
	}

	protected void bindTexture(int texture, int slot) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
	}
}
