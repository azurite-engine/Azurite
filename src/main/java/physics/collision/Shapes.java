package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 20.06.2021
 * @since 20.06.2021
 */
public class Shapes {

    public static Rectangle rectangle(float... coordPairs) {
        if (coordPairs.length != 8) return null;
        Vector2f[] coords = new Vector2f[coordPairs.length / 2];
        for (int i = 0; i < 4; i++)
            coords[i] = new Vector2f(coordPairs[i * 2], coordPairs[i * 2 + 1]);
        return new Rectangle(coords[0], coords[1], coords[2], coords[3]);
    }

    public static Rectangle notRotatedRectangle(float ax, float ay, float bx, float by) {
        Vector2f max, min, topMin, botMax;
        max = new Vector2f(Math.max(ax, bx), Math.max(ay, by));
        min = new Vector2f(Math.min(ax, bx), Math.min(ay, by));
        topMin = new Vector2f(min.x, max.y);
        botMax = new Vector2f(max.x, min.y);
        return rectangle(min, botMax, max, topMin);
    }

    public static Rectangle rectangle(Vector2f a, Vector2f b, Vector2f c, Vector2f d) {
        return new Rectangle(a, b, c, d);
    }

    public static Rectangle notRotatedRectangle(Vector2f a, Vector2f b) {
        return notRotatedRectangle(a.x, a.y, b.x, b.y);
    }

}