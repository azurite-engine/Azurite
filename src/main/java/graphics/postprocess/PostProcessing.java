package graphics.postprocess;

/**
 * Some Utility methods
 */
public class PostProcessing {
    public static void prepare() {
        PostProcessQuad.getInstance()._bindQuad();
    }

    public static void finish() {
        PostProcessQuad.getInstance()._unbindQuad();
    }
}
