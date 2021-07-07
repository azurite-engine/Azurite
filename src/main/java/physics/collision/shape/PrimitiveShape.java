package physics.collision.shape;

import org.joml.Vector2f;
import physics.collision.CollisionUtil;
import util.Utils;

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
    protected final int vertices;
    private final ShapeType type;

    protected Vector2f relativeCentroid;
    protected Vector2f absoluteCentroid;

    protected Circle boundingSphere;

    private Vector2f position = new Vector2f(0, 0);

    protected PrimitiveShape(Vector2f... relatives) {
        this(ShapeType.POLYGON, relatives);
    }

    protected PrimitiveShape(ShapeType type, Vector2f... relatives) {
        this.type = type;
        if (type == ShapeType.CIRCLE && (relatives == null || relatives.length == 0)) {
            //implicit circles don't have any explicit points
            this.vertices = 0;
            this.relatives = new Vector2f[0];
            this.absolutes = new Vector2f[0];
            this.faces = new Face[0];
        } else {
            //ensures that all relative coords are sorted and dereferenced from the original ones
            this.relatives = Utils.copy(CollisionUtil.convexHull(relatives));
            this.vertices = relatives.length;
            this.absolutes = new Vector2f[this.vertices];
            this.faces = new Face[this.vertices];
        }
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

    public final int vertices() {
        return vertices;
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
        for (int i = 0; i < this.vertices; i++) {
            Vector2f line = relatives[(i + 1) % this.vertices].sub(relatives[i], new Vector2f());
            //define faces
            faces[i] = new Face(this, relatives[i], line);
        }
    }

    /**
     * Will be called by {@link this#setPosition(Vector2f)} after the new position was set.
     * Used to recalculate the absolute coordinates.
     */
    public void adjust() {
        for (int i = 0; i < this.vertices; i++) {
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
     * A clean description for this shape.
     *
     * @return the {@link ShapeType} matching this {@link PrimitiveShape}
     */
    public final ShapeType type() {
        return type;
    }

}