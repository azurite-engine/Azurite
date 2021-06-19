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
public class Circle extends GJKSMShape {

    private final float radius;
    private final Vector2f center;
    private Vector2f positionedCenter;

    public Circle(Vector2f center, float radius) {
        super();
        this.center = new Vector2f(center);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public Vector2f getRelativeCenter() {
        return center;
    }

    public Vector2f getAbsoluteCenter() {
        return positionedCenter;
    }

    @Override
    public void setPosition(Vector2f position) {
        super.setPosition(position);
        this.positionedCenter = position.add(this.center, new Vector2f());
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        Vector2f normalized = v.normalize(new Vector2f());
        return positionedCenter.add(normalized.mul(radius), new Vector2f());
    }

}