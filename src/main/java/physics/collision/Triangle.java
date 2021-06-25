package physics.collision;

import org.joml.Vector2f;
import util.Pair;

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
        Vector2f[] sorted = CollisionUtil.convexHull(new Vector2f[]{relativeA, relativeB, relativeC});
        this.relativeA = new Vector2f(sorted[0]);
        this.relativeB = new Vector2f(sorted[1]);
        this.relativeC = new Vector2f(sorted[2]);
        this.relativeCentroid = CollisionUtil.polygonCentroid(this.relativeA, this.relativeB, this.relativeC);
        this.boundingSphere = new Circle(relativeCentroid, CollisionUtil.boundingSphere(relativeCentroid, this.relativeA, this.relativeB, this.relativeC));
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

    public Vector2f[] getAbsolutePoints() {
        return new Vector2f[]{absoluteA, absoluteB, absoluteC};
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
    public Vector2f reflect(Vector2f centroid, Vector2f collisionRay) {
        Pair<Vector2f, Vector2f> normals = CollisionUtil.collisionEdgeNormals(this.getAbsolutePoints(), this.absoluteCentroid, centroid);
        if (normals.getLeft().dot(collisionRay) >= 0)
            return CollisionUtil.planeReflection(normals.getLeft(), collisionRay);
        else return CollisionUtil.planeReflection(normals.getRight(), collisionRay);
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        float ma = v.dot(absoluteA), mb = v.dot(absoluteB), mc = v.dot(absoluteC);
        return ma > mb ? ma > mc ? absoluteA : absoluteC : mb > mc ? absoluteB : absoluteC;
    }

}