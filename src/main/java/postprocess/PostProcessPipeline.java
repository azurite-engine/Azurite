package postprocess;

public abstract class PostProcessPipeline {
	public abstract void init();
	public abstract int apply(int texture);
}
