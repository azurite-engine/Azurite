package graphics;

import ecs.GameObject;
import org.joml.Vector4f;
import java.util.ArrayList;
import java.util.List;

import static util.Utils.map;
import static org.lwjgl.opengl.GL11.glClearColor;

public class Graphics {
	
	public static Color globalFill = new Color(0, 0, 0, 255);
	public static Vector4f glGlobalFill = globalFill.toVec4f();
	// Background
	public static void background(float r, float g, float b, float a) {
		// map the values from 0.0-255.0 to 0.0-1.0
		r = map(r, 0, 255, 0, 1);
		g = map(g, 0, 255, 0, 1);
		b = map(b, 0, 255, 0, 1);

		glClearColor(r, g, b, 1);
	}
	public static void background(float r, float g, float b) {
		background(r, g, b, 255);
	}
	public static void background(float rgb) {
		background(rgb, rgb, rgb, 255);
	}
	public static void background(Color color) {
		background(color.r, color.g, color.b, color.a);
	}

	// Fill
	public static void fill(float r, float g, float b, float a) {
		// map the values from 0.0-255.0 to 0.0-1.0
		r = map(r, 0, 255, 0, 1);
		g = map(g, 0, 255, 0, 1);
		b = map(b, 0, 255, 0, 1);
		a = map(a, 0, 255, 0, 1);

		globalFill = new Color(r, g, b, a);
		glGlobalFill = globalFill.toVec4f();
	}
	public static void fill(float r, float g, float b) {
		fill(r, g, b, 255);
	}
	public static void fill(float rgb) {
		fill(rgb, rgb, rgb, 255);
	}
	public static void fill(Color color) {
		fill(color.r, color.g, color.b, color.a);
	}
	
	List<GameObject> rectangles = new ArrayList<>();

	// Shape Primitives
//	public static void rect(Rectangle reference, float x, float y, float w, float h) {
//		reference.setPosition(x, y, w, h);
//	}
//	public static void rect(Rectangle reference, RectangleBounds r) {
//		rect(reference, r.x, r.y, r.width, r.height);
//	}
}
