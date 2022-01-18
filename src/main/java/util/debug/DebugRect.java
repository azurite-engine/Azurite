package util.debug;

import graphics.Color;
import org.joml.Vector2f;

/**
 * A Rectangle Primitive for the Debug Renderer
 */
public class DebugRect extends DebugPrimitive {
    /**
     * Constructs the Rectangle at given x, y location with width and height and specified color
     *
     * @param x      x position of the rect
     * @param y      y position of the rect
     * @param width  width of the rect
     * @param height height of the rect
     * @param color  color of the rect
     */
    public DebugRect(float x, float y, float width, float height, Color color) {
        super(createLines(x, y, width, height, color));
    }

    /**
     * Creates lines based on given params
     *
     * @param x      x position of the rect
     * @param y      y position of the rect
     * @param width  width of the rect
     * @param height height of the rect
     * @param color  color of the rect
     * @return The Lines making up the primitive
     */
    private static DebugLine[] createLines(float x, float y, float width, float height, Color color) {
        DebugLine[] lines = new DebugLine[4];
        lines[0] = new DebugLine(new Vector2f(x, y), new Vector2f(x + width, y), color);
        lines[1] = new DebugLine(new Vector2f(x + width, y), new Vector2f(x + width, y + height), color);
        lines[2] = new DebugLine(new Vector2f(x + width, y + height), new Vector2f(x, y + height), color);
        lines[3] = new DebugLine(new Vector2f(x, y + height), new Vector2f(x, y), color);
        return lines;
    }

    /**
     * Recalculates start and end points of the lines
     *
     * @param x      x position of the rect
     * @param y      y position of the rect
     * @param width  width of the rect
     * @param height height of the rect
     */
    public void reset(float x, float y, float width, float height) {
        lines[0].start.set(x, y);
        lines[0].end.set(x + width, y);
        lines[1].start.set(x + width, y);
        lines[1].end.set(x + width, y + height);
        lines[2].start.set(x + width, y + height);
        lines[2].end.set(x, y + height);
        lines[3].start.set(x, y + height);
        lines[3].end.set(x, y);

        for (DebugLine line : lines) {
            line.markDirty();
        }
    }
}
