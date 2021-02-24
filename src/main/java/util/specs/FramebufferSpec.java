package util.specs;

/**
 * Specification for a framebuffer
 *
 * @author VoxelRifts
 */
public class FramebufferSpec {
	public FramebufferTextureSpec[] attachments;

	/**
	 * @param textureSpecs FramebufferTextureSpec...: what kind of attachments do you want?
	 */
	public FramebufferSpec(FramebufferTextureSpec... textureSpecs) {
		this.attachments = textureSpecs;
	}
}
