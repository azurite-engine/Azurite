package graphics; 

import static org.lwjgl.opengl.GL11.*;
import static util.MathUtils.map;

/**
 * 
 * A utility class for changing background colors. More functionality will probably added to this
 * class as the engine starts to grow.
 */
public class Graphics {
    public static Color defaultBackground;

    /**
     * Set the GL clear color
     *
     * @param r Red (0-255)
     * @param g Green (0-255)
     * @param b Blue (0-255)
     */
    public static void background(float r, float g, float b) {
        // map the values from 0.0-255.0 to 0.0-1.0
        r = map(r, 0, 255, 0, 1);
        g = map(g, 0, 255, 0, 1);
        b = map(b, 0, 255, 0, 1);

        glClearColor(r, g, b, 1);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    /**
     * Set the GL clear color on a grayscale.
     *
     * @param rgb Single value affecting red, green, and blue (0-255)
     */
    public static void background(float rgb) {
        background(rgb, rgb, rgb);
    }

    /**
     * Set the GL clear color
     *
     * @param color to set the clear color. Alpha is ignored
     */
    public static void background(Color color) {
        background(color.r, color.g, color.b);
    }

    /**
     * Set the GL clear color for the DefaultRenderer on a greyscale.
     *
     * @param rgb Single value affecting red, green, and blue (0-255)
     */
    public static void setDefaultBackground(float rgb) {
        Graphics.defaultBackground = new Color(rgb, rgb, rgb);
    }

    /**
     * Set the GL clear color for the DefaultRenderer
     *
     * @param r Red (0-255)
     * @param g Green (0-255)
     * @param b Blue (0-255)
     */
    public static void setDefaultBackground(float r, float g, float b) {
        Graphics.defaultBackground = new Color(r, g, b);
    }

    /**
     * Set the GL clear color for the DefaultRenderer
     *
     * @param color to set the clear color. Alpha is ignored
     */
    public static void setDefaultBackground(Color color) {
        Graphics.defaultBackground = color;
    }
}
