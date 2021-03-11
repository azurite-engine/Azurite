package util.debug;

import graphics.Color;
import org.joml.Vector2f;

public class DebugRect extends DebugPrimitive {
	public DebugRect(float x, float y, float width, float height, Color color) {
		super(createLines(x, y, width, height, color));
	}

	private static DebugLine[] createLines(float x, float y, float width, float height, Color color) {
		DebugLine[] lines = new DebugLine[4];
		lines[0] = new DebugLine(new Vector2f(x, y), new Vector2f(x + width, y));
		lines[1] = new DebugLine(new Vector2f(x + width, y), new Vector2f(x + width, y + height));
		lines[2] = new DebugLine(new Vector2f(x + width, y + height), new Vector2f(x, y + height));
		lines[3] = new DebugLine(new Vector2f(x, y + height), new Vector2f(x, y));
		return lines;
	}

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
