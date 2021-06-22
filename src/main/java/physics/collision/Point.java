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
public class Point extends GJKSMShape {

    private final Vector2f relativePoint;
    private Vector2f absolutePoint;

    public Point(Vector2f relativePoint) {
        this.relativePoint = new Vector2f(relativePoint);
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
    }

    @Override
    public Vector2f centroid() {
        return absolutePoint;
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return absolutePoint;
    }
}