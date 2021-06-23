package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * A basic polygon shape.
 * It does not ensure the points to be part of the convex hull, so this object may contain points for a concave shape.
 * If that is the case, this shape won't work correctly for GJKSM collision detection.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class BasicPolygon extends Shape {

    private final Vector2f[] relativePoints;
    private final Vector2f[] absolutePoints;

    private final Vector2f relativeCentroid;
    private Vector2f absoluteCentroid;

    private final Circle boundingSphere;

    public BasicPolygon(Vector2f... relativePoints) {
        this.relativePoints = new Vector2f[relativePoints.length];
        for (int i = 0; i < relativePoints.length; i++)
            this.relativePoints[i] = new Vector2f(relativePoints[i]);
        absolutePoints = new Vector2f[relativePoints.length];
        relativeCentroid = ConvexGJKSM.polygonCentroid(relativePoints);
        boundingSphere = new Circle(relativeCentroid, ConvexGJKSM.boundingSphere(relativeCentroid, this.relativePoints));
    }

    public Vector2f[] getRelativePoints() {
        return relativePoints;
    }

    public Vector2f[] getAbsolutePoints() {
        return absolutePoints;
    }

    @Override
    public void adjust() {
        for (int i = 0; i < absolutePoints.length; i++)
            absolutePoints[i] = position().add(relativePoints[i], new Vector2f());
        absoluteCentroid = position().add(relativeCentroid, new Vector2f());
        boundingSphere.setPosition(position());
    }

    @Override
    public Vector2f centroid() {
        return absoluteCentroid;
    }

    @Override
    public Circle boundingSphere() {
        return boundingSphere;
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return ConvexGJKSM.maxDotPoint(absolutePoints, v);
    }

}