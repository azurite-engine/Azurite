package graphics;

import static util.Utils.map;
import static org.lwjgl.opengl.GL11.glClearColor;

public class Graphics {

	/**
	 * Set the GL clear color
	 * @param Red (0-255)
	 * @param Green (0-255)
	 * @param Blue (0-255)
	 */
	public static void background(float r, float g, float b) {
		// map the values from 0.0-255.0 to 0.0-1.0
		r = map(r, 0, 255, 0, 1);
		g = map(g, 0, 255, 0, 1);
		b = map(b, 0, 255, 0, 1);

		glClearColor(r, g, b, 1);
	}

	/**
	 * Set the GL clear color on a grayscale
	 * @param rgb Single value affecting red, green, and blue (0-255)
	 */
	public static void background(float rgb) {
		background(rgb, rgb, rgb);
	}

	/**
	 * Set the GL clear color
	 * @param Color to set the clear color. Alpha is ignored
	 */
	public static void background(Color color) {
		background(color.r, color.g, color.b);
	}

}
