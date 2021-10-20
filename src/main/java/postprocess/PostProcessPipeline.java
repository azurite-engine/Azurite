package postprocess;

import graphics.Texture;

/**
 * This class should be used if you want to chain multiple steps
 * //TODO: we should describe what post-processing does and why we have it.
 */
public abstract class PostProcessPipeline {
    /**
     * Initialize all steps in this pipeline
     */
    public abstract void init();

    /**
     * Apply the effect and return the final texture if not rendering to the default framebuffer
     *
     * @param input input texture to bee processed
     * @return the final texture if not rendering to the default framebuffer
     */
    public abstract Texture apply(Texture input);
}
