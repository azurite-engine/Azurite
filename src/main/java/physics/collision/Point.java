package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * The GJKSM shape implementation of a single point.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Point extends Shape {

    private final Vector2f relativePoint;
    private Vector2f absolutePoint;

    private final Circle boundingSphere;

    public Point(Vector2f relativePoint) {
        this.relativePoint = new Vector2f(relativePoint);
        this.boundingSphere = new Circle(relativePoint, Float.MIN_VALUE);
    }

    public Vector2f getRelativePoint() {
        return relativePoint;
    }

    public Vector2f getAbsolutePoint() {
        return absolutePoint;
    }

    @Override
    public void adjust() {
        this.absolutePoint = position().add(relativePoint, new Vector2f());
        this.boundingSphere.setPosition(position());
    }

    @Override
    public Vector2f reflect(Vector2f centroid, Vector2f collisionRay) {
        return collisionRay.mul(-1, new Vector2f());
    }

    @Override
    public Vector2f centroid() {
        return absolutePoint;
    }

    @Override
    public Circle boundingSphere() {
        return boundingSphere;
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return absolutePoint;
    }
}