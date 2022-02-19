package util.specs;

/**
 * 
 * Specification for a framebuffer
 *
 * @author VoxelRifts
 */
public class FramebufferSpec {
    public TextureSpec[] attachments;

    /**
     * @param textureSpecs FramebufferTextureSpec...: what kind of attachments do you want?
     */
    public FramebufferSpec(TextureSpec... textureSpecs) {
        this.attachments = textureSpecs;
    }
}
