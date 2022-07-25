package graphics.postprocess;

import graphics.Texture;

/**
 * Post Processing Pipeline to apply a bloom effect to a Texture.
 */
public class BloomEffect extends PostProcessPipeline {
    /* The Post Processing Steps required */
    private HorizontalBlur hblur;
    private VerticalBlur vblur;
    private BrightFilter brightFilter;
    private WeightedCombine combine;

    /**
     * Where the final texture is to be rendered
     */
    private PostProcessStep.Target dest;
    /**
     * Amount of bloom to be applied. Default is 0.8
     */
    private float bloomAmt;

    /**
     * To construct a Bloom Effect Pipeline with a bloom of 0.8
     *
     * @param dest Where the final texture is to be rendered
     */
    public BloomEffect(PostProcessStep.Target dest) {
        this(dest, 0.8f);
    }

    /**
     * To construct a Bloom Effect Pipeline with a bloom of 0.8
     *
     * @param dest     Where the final texture is to be rendered
     * @param bloomAmt amount of bloom to be applied
     */
    public BloomEffect(PostProcessStep.Target dest, float bloomAmt) {
        this.dest = dest;
        this.bloomAmt = bloomAmt;
        init();
    }

    /**
     * Initializes all steps in this pipeline
     */
    @Override
    public void init() {
        brightFilter = new BrightFilter(PostProcessStep.Target.ONE_COLOR_TEXTURE_FRAMEBUFFER);
        brightFilter.init();
        hblur = new HorizontalBlur(PostProcessStep.Target.ONE_COLOR_HALF_SIZE_TEXTURE_FRAMEBUFFER);
        hblur.init();
        vblur = new VerticalBlur(PostProcessStep.Target.ONE_COLOR_HALF_SIZE_TEXTURE_FRAMEBUFFER);
        vblur.init();
        combine = new WeightedCombine(dest);
        combine.init();
    }

    /**
     * Apply the bloom effect and return the final texture if not rendering to the default framebuffer
     *
     * @param input input texture to bee processed
     * @return the final texture if not rendering to the default framebuffer
     */
    @Override
    public Texture apply(Texture input) {
        brightFilter.setTexture(input);
        Texture brights = brightFilter.apply();
        hblur.setTexture(brights);
        Texture hBlurred = hblur.apply();
        vblur.setTexture(hBlurred);
        Texture blurred = vblur.apply();
        combine.setTextureA(input);
        combine.setTextureB(blurred);
        combine.setWeightB(bloomAmt);
        return combine.apply();
    }
}
