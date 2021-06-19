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
public class BasicPolygon extends GJKSMShape {

    private final Vector2f[] points;
    private Vector2f[] positioned;

    public BasicPolygon(Vector2f... points) {
        super();
        this.points = new Vector2f[points.length];
        for (int i = 0; i < points.length; i++)
            this.points[i] = new Vector2f(points[i]);
    }

    public Vector2f[] getRelativePoints() {
        return points;
    }

    public Vector2f[] getAbsolutePoints() {
        return positioned;
    }

    @Override
    public void setPosition(Vector2f position) {
        super.setPosition(position);
        positioned = new Vector2f[points.length];
        for (int i = 0; i < points.length; i++)
            positioned[i] = position.add(points[i], new Vector2f());
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return ConvexGJKSM.maxDotPoint(positioned, v);
    }

}