package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * The GJKSM shape implementation of a circle.
 *
 * @author Julius Korweck
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Circle implements GJKSMShape {

    private final Vector2f center;
    private final float radius;

    public Circle(Vector2f center, float radius) {
        this.center = new Vector2f(center);
        this.radius = radius;
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        Vector2f normalized = v.normalize(new Vector2f());
        return center.add(normalized.mul(radius), new Vector2f());
    }
}