package util.debug;

public class DebugPrimitive {
	protected DebugLine[] lines;

	public DebugPrimitive(DebugLine[] lines) {
		this.lines = lines;
	}

	public DebugLine[] getLines() {
		return lines;
	}
}
