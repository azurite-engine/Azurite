package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * The GJKSM shape implementation of a rectangle.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Rectangle extends Shape {

    private final Vector2f[] relatives;
    private Vector2f[] absolutes;

    private final Vector2f relativeCentroid;
    private Vector2f absoluteCentroid;

    private Circle boundingSphere;

    public Rectangle(Vector2f a, Vector2f b, Vector2f c, Vector2f d) {
        this.relatives = new Vector2f[]{new Vector2f(a), new Vector2f(b), new Vector2f(c), new Vector2f(d)};
        this.absolutes = new Vector2f[4];
        this.relativeCentroid = ConvexGJKSM.polygonCentroid(this.relatives);
        this.boundingSphere = new Circle(relativeCentroid, ConvexGJKSM.boundingSphere(relativeCentroid, relatives));
    }

    @Override
    public void adjust() {
        for (int i = 0; i < 4; i++) {
            absolutes[i] = position().add(relatives[i], new Vector2f());
        }
        absoluteCentroid = position().add(relativeCentroid, new Vector2f());
        this.boundingSphere.setPosition(position());
    }

    public Vector2f[] getAbsolutePoints() {
        return absolutes;
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
        return ConvexGJKSM.maxDotPoint(absolutes, v);
    }
}