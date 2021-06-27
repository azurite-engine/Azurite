package physics.collision.shape;

import org.joml.Vector2f;
import physics.collision.CollisionUtil;
import util.Pair;
import util.Triple;

import java.util.Optional;

/**
 * <h1>Azurite</h1>
 * <p>
 * The {@link PrimitiveShape} is implicitly described by all its support vectors.
 * A support vector is a point on the convex hull of a shape, that is furthest in the direction of a specified vector v.
 * This class may contain information about explicit points on the edge too.
 *
 * @author Juyas
 * @version 18.06.2021
 * @since 18.06.2021
 */
public abstract class PrimitiveShape {

    protected final Vector2f[] relatives;
    protected final Vector2f[] absolutes;
    protected final Face[] faces;
    protected final Vector2f[] diagonals;

    protected Vector2f relativeCentroid;
    protected Vector2f absoluteCentroid;

    protected Circle boundingSphere;

    private Vector2f position = new Vector2f(0, 0);

    protected PrimitiveShape(Vector2f... relatives) {
        this.relatives = CollisionUtil.convexHull(relatives);
        this.absolutes = new Vector2f[relatives.length];
        this.faces = new Face[relatives.length];
        this.diagonals = new Vector2f[relatives.length];
    }

    /**
     * Update the shapes position due to external changes, e.g. the related object has moved
     *
     * @param position the new position of the related object
     */
    public final void setPosition(Vector2f position) {
        this.position = position;
        adjust();
    }

    public final Vector2f position() {
        return position;
    }

    public final Vector2f[] getAbsolutePoints() {
        return absolutes;
    }

    public final Vector2f getAbsoluteCentroid() {
        return absoluteCentroid;
    }

    public final Face[] faces() {
        return faces;
    }

    /**
     * Calculates a relative centroid and the boundingSphere for the shape.
     * In some special cases this might be done quicker in a different way.
     */
    protected final void initSphere() {
        this.relativeCentroid = CollisionUtil.polygonCentroid(this.relatives);
        this.boundingSphere = new Circle(relativeCentroid, CollisionUtil.boundingSphere(relativeCentroid, this.relatives));
    }

    /**
     * Requires the relativeCentroid to be set.
     * Calculates all relative faces and relative diagonals for a defined shape
     */
    protected final void init() {
        for (int i = 0; i < relatives.length; i++) {
            Vector2f line = relatives[(i + 1) % relatives.length].sub(relatives[i], new Vector2f());
            //define faces
            faces[i] = new Face(this, relatives[i], line);
            //define diagonals
            diagonals[i] = relatives[i].sub(relativeCentroid, new Vector2f());
        }
    }

    /**
     * This method will only accept shapes, that are not circles.
     * Will perform a rayCast Face-Diagonal intersection for the given shape with the current one.
     * The diagonals of the current shape will be checked against the faces of the other shape.
     *
     * @param other the other shape that intersects with the current one
     * @return a pair containing the intersection point and the factors to calculate the intersection point
     */
    protected final Optional<Triple<Vector2f, Vector2f, Vector2f>> nonCircleCollision(PrimitiveShape other) {
        if (other.shape() == Shape.CIRCLE)
            return Optional.empty();
        Face[] faces = other.faces();
        //room for improvement: choose a good starting point, better diagonals might be guess by impact direction
        for (Vector2f diagonal : diagonals) {
            for (Face face : faces) {
                Optional<Pair<Vector2f, Vector2f>> intersection =
                        CollisionUtil.rayCastIntersection(absoluteCentroid, diagonal, face.getAbsoluteFixPoint(), face.getRelativeFace());
                if (intersection.isPresent())
                    return intersection.map(pair -> pair.extend(diagonal));
            }
        }
        return Optional.empty();
    }

    /**
     * Will be called by {@link this#setPosition(Vector2f)} after the new position was set.
     * Used to recalculate the absolute coordinates.
     */
    public void adjust() {
        for (int i = 0; i < absolutes.length; i++) {
            absolutes[i] = position().add(relatives[i], new Vector2f());
        }
        absoluteCentroid = position().add(relativeCentroid, new Vector2f());
        this.boundingSphere.setPosition(position());
    }

    /**
     * The center point or weight point of the shape.
     *
     * @return centroid of the shape
     */
    public final Vector2f centroid() {
        return absoluteCentroid;
    }

    /**
     * A minimal sphere that contains the original shape.
     * The inner shape is supposed to be fixed inside the sphere and share the same centroid.
     *
     * @return the minimal sphere containing this shape
     */
    public final Circle boundingSphere() {
        return boundingSphere;
    }

    /**
     * According to GJKSM this method is supposed to calculate the point of the shape, that is most in direction of v.
     * The general rule is, the more primitive the shape is, the more efficient this method can be.
     * This method may be described as max{v*x,x element of Shape} for any complex shape.
     *
     * @param v the direction
     * @return the point of the shape that is most in the direction of v
     */
    public Vector2f supportPoint(Vector2f v) {
        return CollisionUtil.maxDotPoint(absolutes, v);
    }

    /**
     * This method will be default only accept shapes, that are not circles.
     * Should be overwritten by any shape, that are circles or cannot be defined with a finite number of points.
     *
     * @param other the other shape
     * @return a pair containing the intersection point and the factors to calculate the intersection point
     * @see #nonCircleCollision(PrimitiveShape)
     */
    public Optional<Triple<Vector2f, Vector2f, Vector2f>> collision(PrimitiveShape other) {
        return nonCircleCollision(other);
    }

    //------------------------------------ Abstract methods ------------------------------------------------------

    /**
     * Calculates the reflection direction of a given collision ray
     *
     * @param centroid     origin of the collisionRay
     * @param collisionRay the incoming ray to reflect
     * @return the reflection vector of the collision ray considering the current shape
     */
    //FIXME does work correctly
    public abstract Vector2f reflect(Vector2f centroid, Vector2f collisionRay);

    /**
     * A clean description for this shape.
     *
     * @return the {@link Shape} matching this {@link PrimitiveShape}
     */
    public abstract Shape shape();

}