package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Triangle extends Shape {

    private final Vector2f relativeA, relativeB, relativeC;
    private Vector2f absoluteA, absoluteB, absoluteC;

    private final Vector2f relativeCentroid;
    private Vector2f absoluteCentroid;

    private final Circle boundingSphere;

    public Triangle(Vector2f relativeA, Vector2f relativeB, Vector2f relativeC) {
        this.relativeA = new Vector2f(relativeA);
        this.relativeB = new Vector2f(relativeB);
        this.relativeC = new Vector2f(relativeC);
        this.relativeCentroid = ConvexGJKSM.polygonCentroid(relativeA, relativeB, relativeC);
        this.boundingSphere = new Circle(relativeCentroid, ConvexGJKSM.boundingSphere(relativeCentroid, this.relativeA, this.relativeB, this.relativeC));
    }

    public Vector2f getAbsoluteA() {
        return absoluteA;
    }

    public Vector2f getAbsoluteB() {
        return absoluteB;
    }

    public Vector2f getAbsoluteC() {
        return absoluteC;
    }

    public Vector2f getRelativeA() {
        return relativeA;
    }

    public Vector2f getRelativeB() {
        return relativeB;
    }

    public Vector2f getRelativeC() {
        return relativeC;
    }

    @Override
    public void adjust() {
        this.absoluteA = position().add(relativeA, new Vector2f());
        this.absoluteB = position().add(relativeB, new Vector2f());
        this.absoluteC = position().add(relativeC, new Vector2f());
        this.absoluteCentroid = position().add(relativeCentroid, new Vector2f());
        this.boundingSphere.setPosition(position());
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
        float ma = v.dot(absoluteA), mb = v.dot(absoluteB), mc = v.dot(absoluteC);
        return ma > mb ? ma > mc ? absoluteA : absoluteC : mb > mc ? absoluteB : absoluteC;
    }

}