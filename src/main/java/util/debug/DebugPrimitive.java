package util.debug;

/**
 * A Primitive that consists of a bunch of lines.
 * These lines are rendered by the DebugRenderer
 */
public class DebugPrimitive {
    /**
     * The lines making up this primitive
     */
    protected DebugLine[] lines;

    /**
     * Create a primitive with the given lines
     *
     * @param lines the lines making up the primitive
     */
    public DebugPrimitive(DebugLine[] lines) {
        this.lines = lines;
    }

    /**
     * Get the lines making up the primitive
     */
    public DebugLine[] getLines() {
        return lines;
    }
}
