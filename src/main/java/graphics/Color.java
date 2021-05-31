package graphics;

import org.joml.Vector3f;
import util.Utils;
import org.joml.Vector4f;
import java.util.Random;

public class Color {
	public static Random random = new Random();

	/*
	 * Color class used throughout the engine to represent red, green, blue and alpha
	 * Contains static predefined colors that can easily be used during prototyping.
	 */
	public static Color WHITE = new Color(255, 255, 255, 255);
	public static Color BLACK = new Color(0, 0, 0, 255);
	public static Color RED = new Color(255, 0, 0, 255);
	public static Color DARK_RED = new Color(127, 0, 0, 255);
	public static Color GREEN = new Color(0, 255, 0, 255);
	public static Color BLUE = new Color(0, 0, 255, 255);
	public static Color DARK_BLUE = new Color(0, 0, 127, 255);
	public static Color DIRTY_BLUE = new Color(0, 127, 127, 255);
	public static Color PINK = new Color(255, 0, 255, 255);
	public static Color CYAN = new Color(0, 255, 255, 255);
	public static Color YELLOW = new Color(255, 255, 0, 255);
	public static Color PURPLE = new Color(127, 0, 127, 255);
	public static Color SILVER = new Color(192, 192, 192, 255);
	public static Color GRAY = new Color(128, 128, 128, 255);
	public static Color MAROON = new Color(128, 0, 0, 255);
	public static Color OLIVE = new Color(120, 128, 0, 255);
	public static Color DARK_GREEN = new Color(0, 128, 0, 255);
	public static Color TEAL = new Color(0, 128, 128, 255);
	public static Color NAVY_BLUE = new Color(0, 0, 128, 255);
	public static Color BROWN = new Color(165, 42, 42, 255);
	public static Color FIREBRICK = new Color(178, 34, 34, 255);
	public static Color CRIMSON = new Color(220, 20, 60, 255);

	/**
	* @return random Color
	*/
	public static Color randomColor () {
		return new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255),255);
	}

	/** Red component for this color. Range: 0-255 */
	public float r;
	/** Green component for this color. Range: 0-255 */
	public float g;
	/** Blue component for this color. Range: 0-255 */
	public float b;
	/** Alpha component for this color. Range: 0-255 */
	public float a;

	/**
	 * Creates new color with specified rgba values
	 * @param pr initial red value
	 * @param pg initial green value
	 * @param pb initial blue value
	 * @param pa initial alpha value
	 */
	public Color (float pr, float pg, float pb, float pa) {
		r = pr;
		g = pg;
		b = pb;
		a = pa;
	}

	/**
	 * Creates new color with specified rgb values
	 * @param pr initial red value
	 * @param pg initial green value
	 * @param pb initial blue value
	 */
	public Color (float pr, float pg, float pb) {
		this(pr, pg, pb, 255);
	}

	/**
	 * Creates new color with specified value for rgb
	 * @param c value for red, green and blue
	 */
	public Color (float c) {
		this(c, c, c, 255);
	}

	/** Returns a Vector4f with rgba as xyzw */
	public Vector4f toVec4f () {
		return new Vector4f(r, g, b, a);
	}

	/** Utility function to Map a color value from range 0-255 to range 0-1 */
	private float m (float p) {
		return Utils.map(p, 0, 255, 0, 1);
	}

	/** Get the Normalized Vector4f for this color. Used mostly in OpenGL */
	public Vector4f toNormalizedVec4f () {
		return new Vector4f(m(r), m(g), m(b), m(a));
	}

	/** Get the Normalized Vector3f for this color. Used mostly in OpenGL */
	public Vector3f toNormalizedVec3f () {
		return new Vector3f(m(r), m(g), m(b));
	}

	/** Turn a Color with normalised values to a color with values from 0-255 */
	public Color fromNormalized () {
		return new Color(Utils.map(r, 0, 1, 0, 255), Utils.map(g, 0, 1, 0, 255), Utils.map(b, 0, 1, 0, 255),
				Utils.map(a, 0, 1, 0, 255));
	}

	/** Check if colors are equal */
	@Override
	public boolean equals (Object c) {
		if (c == null) {
			return false;
		} if (!(c instanceof Color)) {
			return false;
		}
		Color otherColor = (Color)c;
		return otherColor.r == this.r && otherColor.g == this.g && otherColor.b == this.b && otherColor.a == this.a;
	}

	/** Set Alpha for this color */
	public void setAlpha (float value) {
		a = value;
	}

	/**
	 * Set a component of this color to a value
	 * @param type can be one of r, g, b, a
	 * @param value value to set the component to
	 */
	public void setValue (char type, float value) {
		switch (type) {
		case 'r':
			r = value;
			break;
		case 'g':
			g = value;
			break;
		case 'b':
			b = value;
			break;
		case 'a':
			a = value;
			break;
		}
	}
}

