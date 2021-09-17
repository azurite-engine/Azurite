package graphics.renderer;

import graphics.Primitive;
import graphics.ShaderDatatype;
import org.joml.Vector4f;
import util.debug.DebugLine;

import java.util.ArrayList;
import java.util.List;

public class DebugRenderBatch extends RenderBatch {
    private final List<DebugLine> lines;

    /**
     * @param maxBatchSize the maximum number of primitives in a batch
     * @param zIndex       the zIndex of the batch. Used to sort the batches in order of which sprites appear above others.
     */
    public DebugRenderBatch(int maxBatchSize, int zIndex) {
        super(maxBatchSize, zIndex, Primitive.LINE, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4);
        this.lines = new ArrayList<>();
    }

    /**
     * Load up a primitive to the data array
     *
     * @param index  index of the primitive to be loaded
     * @param offset offset of where the primitive should start being added to the array
     */
    @Override
    protected void loadVertexProperties(int index, int offset) {
        DebugLine line = lines.get(index);
        Vector4f color = line.color.toNormalizedVec4f();

        data[offset] = line.start.x;
        data[offset + 1] = line.start.y;
        data[offset + 2] = color.x;
        data[offset + 3] = color.y;
        data[offset + 4] = color.z;
        data[offset + 5] = color.w;

        primitiveVertices[0] = data[offset];
        primitiveVertices[1] = data[offset + 1];
        primitiveVertices[2] = data[offset + 2];
        primitiveVertices[3] = data[offset + 3];
        primitiveVertices[4] = data[offset + 4];
        primitiveVertices[5] = data[offset + 5];

        data[offset + 6] = line.end.x;
        data[offset + 7] = line.end.y;
        data[offset + 8] = color.x;
        data[offset + 9] = color.y;
        data[offset + 10] = color.z;
        data[offset + 11] = color.w;

        primitiveVertices[6] = data[offset + 6];
        primitiveVertices[7] = data[offset + 7];
        primitiveVertices[8] = data[offset + 8];
        primitiveVertices[9] = data[offset + 9];
        primitiveVertices[10] = data[offset + 10];
        primitiveVertices[11] = data[offset + 11];
    }

    /**
     * Update the buffer on the GPU but only if it is necessary
     */
    @Override
    public void updateBuffer() {
        boolean update = false;
        for (int i = 0; i < lines.size(); i++) {
            DebugLine line = lines.get(i);
            if (line.isDirty()) {
                super.updateBuffer(i);
                update = true;
                line.markClean();
            }
        }
//		if (update) super.updateBufferFull();
    }

    /**
     * Add a DebugLine to the batch
     *
     * @param line the line to be added
     * @return whether the line has successfully been added
     */
    public boolean addLine(DebugLine line) {
        // If the line has already been added, make sure it doesn't get added to any other batch
        if (lines.contains(line))
            return true;

        if (hasRoomLeft()) {
            // Get the index and add the renderObject
            lines.add(line);
            line.setLocation(this, lines.size() - 1);

            // Add properties to local vertices array
            load(lines.size() - 1);

            if (lines.size() >= this.maxBatchSize) {
                this.hasRoom = false;
            }
            return true;
        }
        return false;
    }

    /**
     * Remove a DebugLine from the batch
     *
     * @param line the line to be removed
     */
    public void removeLine(DebugLine line) {
        if (line.getBatch() == this) {
            lines.remove(line);

            remove(line.getIndex());
        }
    }
}
