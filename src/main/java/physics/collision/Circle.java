package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * The GJKSM shape implementation of a circle.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Circle extends GJKSMShape {

    private final float radius;
    private final Vector2f relativeCenter;
    private Vector2f absoluteCenter;

    public Circle(Vector2f relativeCenter, float radius) {
        this.relativeCenter = new Vector2f(relativeCenter);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public Vector2f getRelativeCenter() {
        return relativeCenter;
    }

    public Vector2f getAbsoluteCenter() {
        return absoluteCenter;
    }

    @Override
    public void adjust() {
        this.absoluteCenter = position().add(this.relativeCenter, new Vector2f());
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        Vector2f normalized = v.normalize(new Vector2f());
        return absoluteCenter.add(normalized.mul(radius), new Vector2f());
    }

}