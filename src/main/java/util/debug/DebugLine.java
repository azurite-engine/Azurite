package util.debug;

import graphics.Color;
import graphics.RenderableComponent;
import graphics.renderer.DebugRenderBatch;
import org.joml.Vector2f;

// NOTE: Even though this is not a component, it extends RenderableComponent. This was the easiest
// way I found to make these debug lines work with the removal system.

/**
 * Represents a Line which can be rendered by DebugRenderer
 * The most basic primitive for Debug Rendering
 */
public class DebugLine extends RenderableComponent<DebugRenderBatch> {
    /**
     * Start point for the line
     */
    public Vector2f start;
    /**
     * End point of the line
     */
    public Vector2f end;
    /**
     * Color of the line
     */
    public Color color;

    /**
     * Were any values of the above changed.
     */
    public boolean dirty;

    /**
     * Creates a line with a given start and end point with a White color
     *
     * @param start start point of the line
     * @param end   end point of the line
     */
    public DebugLine(Vector2f start, Vector2f end) {
        this(start, end, Color.WHITE);
    }

    /**
     * Creates a line with a given start and end point and the specified color
     *
     * @param start start point of the line
     * @param end   end point of the line
     * @param color color of the line
     */
    public DebugLine(Vector2f start, Vector2f end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
        markDirty();
    }

    /**
     * Were any values changed.
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Mark this line dirty so renderer can reflect changed values
     */
    public void markDirty() {
        this.dirty = true;
    }

    /**
     * Mark this line clean so renderer doesn't update the values of this line
     */
    public void markClean() {
        this.dirty = false;
    }
}
