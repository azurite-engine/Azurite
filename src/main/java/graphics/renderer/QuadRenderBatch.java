package graphics.renderer;

import event.Events;
import graphics.Primitive;
import graphics.ShaderDatatype;
import graphics.Window;
import org.joml.Vector2f;

public class QuadRenderBatch extends RenderBatch {
	public QuadRenderBatch() {
		super(1, 1, Primitive.QUAD, ShaderDatatype.FLOAT2);
		Events.windowResizeEvent.subscribe(d -> loadQuad());
	}

	/**
	 * Load up a primitive to the data array
	 *
	 * @param index  index of the primitive to be loaded
	 * @param offset offset of where the primitive should start being added to the array
	 */
	@Override
	protected void loadVertexProperties(int index, int offset) {
		data[offset]     = 0;
		data[offset + 1] = 0;
		data[offset + 2] = Window.getWidth();
		data[offset + 3] = 0;
		data[offset + 4] = Window.getWidth();
		data[offset + 5] = Window.getHeight();
		data[offset + 6] = 0;
		data[offset + 7] = Window.getHeight();
	}

	public void loadQuad() {
		load(0);
	}
}
