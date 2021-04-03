package postprocess;

public class BloomEffect extends PostProcessPipeline {
	private HorizontalBlur hblur;
	private VerticalBlur vblur;
	private BrightFilter brightFilter;
	private Combine combine;

	private PostProcessStep.Target dest;

	public BloomEffect(PostProcessStep.Target dest) {
		this.dest = dest;
	}

	@Override
	public void init() {
		brightFilter = new BrightFilter(PostProcessStep.Target.ONE_COLOR_TEXTURE_FRAMEBUFFER);
		brightFilter.init();
		hblur = new HorizontalBlur(PostProcessStep.Target.ONE_COLOR_HALF_SIZE_TEXTURE_FRAMEBUFFER);
		hblur.init();
		vblur = new VerticalBlur(PostProcessStep.Target.ONE_COLOR_HALF_SIZE_TEXTURE_FRAMEBUFFER);
		vblur.init();
		combine = new Combine(dest);
		combine.init();
	}

	@Override
	public int apply(int texture) {
		brightFilter.setTexture(texture);
		int brights = brightFilter.apply();
		hblur.setTexture(brights);
		int hBlurred = hblur.apply();
		vblur.setTexture(hBlurred);
		int blurred = vblur.apply();
		combine.setTextureA(texture);
		combine.setTextureB(blurred);
		return combine.apply();
	}
}
