package graphics.renderer;

import graphics.Primitive;
import graphics.ShaderDatatype;
import graphics.Window;
import org.joml.Vector2f;

public class QuadRenderBatch extends RenderBatch {
	private Vector2f offset;

	public QuadRenderBatch() {
		super(1, 1, Primitive.QUAD, ShaderDatatype.FLOAT2);
		offset = new Vector2f(0);
	}

	/**
	 * Load up a primitive to the data array
	 *
	 * @param index  index of the primitive to be loaded
	 * @param offset offset of where the primitive should start being added to the array
	 */
	@Override
	protected void loadVertexProperties(int index, int offset) {
		data[offset]     = 0 + this.offset.x;
		data[offset + 1] = 0 + this.offset.y;
		data[offset + 2] = Window.getWidth() + this.offset.x;
		data[offset + 3] = 0 + this.offset.y;
		data[offset + 4] = Window.getWidth() + this.offset.x;
		data[offset + 5] = Window.getHeight() + this.offset.y;
		data[offset + 6] = 0 + this.offset.x;
		data[offset + 7] = Window.getHeight() + this.offset.y;
	}

	public void loadQuad() {
		load(0);
	}

	public void setOffset(Vector2f offset) {
		this.offset.set(offset);
		load(0);
	}
}
