package physics.collision;

import org.joml.Vector2f;
import physics.collision.shape.Circle;
import physics.collision.shape.ConvexPolygon;
import physics.collision.shape.Quadrilateral;
import physics.collision.shape.Triangle;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 20.06.2021
 * @since 20.06.2021
 */
public class Shapes {

    public static Circle circle(Vector2f center, Vector2f circlePosition, float radius) {
        Circle circle = new Circle(center.sub(circlePosition, new Vector2f()), radius);
        circle.setPosition(circlePosition);
        return circle;
    }

    public static Circle circle(Vector2f center, float radius) {
        return new Circle(center, radius);
    }

    public static Circle circle(float x, float y, float r) {
        return circle(new Vector2f(x, y), r);
    }

    public static ConvexPolygon convexPolygon(float... coordPairs) {
        if (coordPairs.length % 2 == 1) return null;
        Vector2f[] coords = new Vector2f[coordPairs.length / 2];
        for (int i = 0; i < coords.length; i++)
            coords[i] = new Vector2f(coordPairs[i * 2], coordPairs[i * 2 + 1]);
        return new ConvexPolygon(coords);
    }

    public static Triangle triangle(float ax, float ay, float bx, float by, float cx, float cy) {
        return triangle(new Vector2f(ax, ay), new Vector2f(bx, by), new Vector2f(cx, cy));
    }

    public static Triangle triangle(Vector2f a, Vector2f b, Vector2f c) {
        return new Triangle(a, b, c);
    }

    public static Quadrilateral rectangle(float... coordPairs) {
        if (coordPairs.length != 8) return null;
        Vector2f[] coords = new Vector2f[coordPairs.length / 2];
        for (int i = 0; i < 4; i++)
            coords[i] = new Vector2f(coordPairs[i * 2], coordPairs[i * 2 + 1]);
        return new Quadrilateral(coords[0], coords[1], coords[2], coords[3]);
    }

    public static Quadrilateral axisAlignedRectangle(float ax, float ay, float bx, float by) {
        Vector2f max, min, topMin, botMax;
        max = new Vector2f(Math.max(ax, bx), Math.max(ay, by));
        min = new Vector2f(Math.min(ax, bx), Math.min(ay, by));
        topMin = new Vector2f(min.x, max.y);
        botMax = new Vector2f(max.x, min.y);
        return rectangle(min, botMax, max, topMin);
    }

    public static Quadrilateral rectangle(Vector2f a, Vector2f b, Vector2f c, Vector2f d) {
        return new Quadrilateral(a, b, c, d);
    }

    public static Quadrilateral axisAlignedRectangle(Vector2f a, Vector2f b) {
        return axisAlignedRectangle(a.x, a.y, b.x, b.y);
    }

}