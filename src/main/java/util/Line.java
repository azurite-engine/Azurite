package util;

import graphics.Color;
import org.joml.Vector2f;

public class Line {
	public Vector2f start;
	public Vector2f end;
	public Color color;

	public boolean dirty;

	public Line(Vector2f start, Vector2f end) {
		this.start = start;
		this.end = end;
		this.color = Color.WHITE;
	}

	public Line(Vector2f start, Vector2f end, Color color) {
		this.start = start;
		this.end = end;
		this.color = color;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void markDirty() {
		this.dirty = true;
	}

	public void markClean() {
		this.dirty = false;
	}
}
