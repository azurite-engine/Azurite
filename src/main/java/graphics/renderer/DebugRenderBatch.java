package graphics.renderer;

import graphics.Primitive;
import graphics.ShaderDatatype;
import org.joml.Vector4f;
import util.debug.DebugLine;

public class DebugRenderBatch extends RenderBatch {
	private final DebugLine[] lines;
	private int numberOfLines;

	/**
	 * @param maxBatchSize the maximum number of primitives in a batch
	 * @param zIndex       the zIndex of the batch. Used to sort the batches in order of which sprites appear above others.
	 */
	public DebugRenderBatch(int maxBatchSize, int zIndex) {
		super(maxBatchSize, zIndex, Primitive.LINE, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4);
		this.lines = new DebugLine[maxBatchSize];
		numberOfLines = 0;
	}

	/**
	 * Load up a primitive to the data array
	 *
	 * @param index  index of the primitive to be loaded
	 * @param offset offset of where the primitive should start being added to the array
	 */
	@Override
	protected void loadVertexProperties(int index, int offset) {
		DebugLine line = lines[index];
		Vector4f color = line.color.toNormalizedVec4f();

		data[offset     ] = line.start.x;
		data[offset + 1 ] = line.start.y;
		data[offset + 2 ] = color.x;
		data[offset + 3 ] = color.y;
		data[offset + 4 ] = color.z;
		data[offset + 5 ] = color.w;

		data[offset + 6 ] = line.end.x;
		data[offset + 7 ] = line.end.y;
		data[offset + 8 ] = color.x;
		data[offset + 9 ] = color.y;
		data[offset + 10] = color.z;
		data[offset + 11] = color.w;
	}

	/**
	 * Update the buffer on the GPU but only if it is necessary
	 */
	@Override
	public void updateBuffer() {
		for (int i = 0; i < numberOfLines; i ++) {
			DebugLine line = lines[i];
			if (line.isDirty()) {
				load(i);
				line.markClean();
			}
		}
		super.updateBuffer();
	}

	public boolean addLine(DebugLine line) {
		if (hasRoomLeft()) {
			// Get the index and add the renderObject
			int index = this.numberOfLines++;
			this.lines[index] = line;

			// Add properties to local vertices array
			load(index);

			if (this.numberOfLines >= this.maxBatchSize) {
				this.hasRoom = false;
			}
			return true;
		}
		return false;
	}
}
