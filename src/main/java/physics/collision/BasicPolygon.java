package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * A basic polygon shape.
 * It does not ensure the points to be part of the convex hull, so this object may contain points for a concave shape.
 * If that is the case, this shape won't work correctly for GJKSM collision detection.
 *
 * @author Julius Korweck
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class BasicPolygon implements GJKSMShape {

    private final Vector2f[] points;

    public BasicPolygon(Vector2f... points) {
        this.points = new Vector2f[points.length];
        for (int i = 0; i < points.length; i++)
            this.points[i] = new Vector2f(points[i]);
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return ConvexGJKSM.maxDotPoint(points, v);
    }

}