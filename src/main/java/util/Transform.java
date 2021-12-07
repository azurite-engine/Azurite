package util;

import org.joml.Vector2f;

/**
 * Represents the position in X and Y coordinates, rotation and the scale (width and height)
 */
public class Transform {

    public Vector2f scale;
    private Vector2f position;
    /**
     * Rotation is stored in degree
     *
     * @see #getRotation()
     * @see #getRotationRadians()
     */

    private float rotation;

    /**
     * Creates a new empty transform.
     */
    public Transform() {
        init(new Vector2f(), 0, new Vector2f());
    }

    /**
     * Create a copy of a transform instance
     *
     * @param transform the transform instance
     */
    public Transform(Transform transform) {
        init(new Vector2f(transform.position), transform.rotation, new Vector2f(transform.scale));
    }

    /**
     * If only the position is passed, the scale is not, scale is created as an empty Vector2f.
     *
     * @param position X and Y coordinates as a Vector2f
     */
    public Transform(Vector2f position) {
        init(position, 0, new Vector2f());
    }

    /**
     * If only the position is passed, the scale is not, scale is created as an empty Vector2f.
     *
     * @param position X and Y coordinates as a Vector2f
     * @param rotation Rotation of the object in degrees
     */
    public Transform(Vector2f position, float rotation) {
        init(position, rotation, new Vector2f());
    }

    /**
     * @param position X and Y coordinates as a Vector2f
     * @param scale    scale (width and height) of the object as a Vector2f
     */
    public Transform(Vector2f position, Vector2f scale) {
        init(position, 0, scale);
    }

    /**
     * @param position X and Y coordinates as a Vector2f
     * @param rotation Rotation of the object in degrees
     * @param scale    scale (width and height) of the object as a Vector2f
     */
    public Transform(Vector2f position, float rotation, Vector2f scale) {
        init(position, rotation, scale);
    }

    /**
     * @param x X coordinate of the object
     * @param y Y coordinate of the object
     * @param w width of the object
     * @param h height of the object
     */
    public Transform(float x, float y, float w, float h) {
        init(new Vector2f(x, y), 0, new Vector2f(w, h));
    }

    /**
     * @param x X coordinate of the object
     * @param y Y coordinate of the object
     * @param r Rotation of the object in degrees
     * @param w width of the object
     * @param h height of the object
     */
    public Transform(float x, float y, float r, float w, float h) {
        init(new Vector2f(x, y), r, new Vector2f(w, h));
    }

    /**
     * Used in constructors to initialize the object.
     *
     * @param position
     * @param rotation
     * @param scale
     */
    private void init(Vector2f position, float rotation, Vector2f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    /**
     * @return Returns a new transform which is identical to this object, can be used to copy into a new physics.Transform object.
     */
    public Transform copy() {
        return new Transform(new Vector2f(this.position.x(), this.position.y()), new Vector2f(this.scale.x(), this.scale.y()));
    }

    /**
     * Takes a reference to an external transform, and copies this physics.Transform to it.
     *
     * @param to The physics.Transform to be changed.
     */
    public void copy(Transform to) {
        to.position.set(this.position);
        to.rotation = this.rotation;
        to.scale.set(this.scale);
    }

    /**
     * Checks to see if to Transforms are equal.
     *
     * @param o Transform to be checked for equality against this instance of util.Transform.
     * @return Returns true if instances are the same, otherwise returns false.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Transform)) {
            return false;
        }
        Transform t = (Transform) o;
        return t.position.equals(this.position) && t.rotation == this.rotation && t.scale.equals(this.scale);
    }

    /*
     * Modification methods - I don't think that these need any explanation...
     */
    public float getX() {
        return this.position.x;
    }

    public float getY() {
        return this.position.y;
    }

    public void setY (float y) { this.position.set(this.position.x(), y); }

    public void setX (float x) { this.position.set(x, this.position.y()); }

    public void addY (float y) {
        this.position.add(0, y);
    }

    public void addX (float x) {
        this.position.add(x, 0);
    }

    public float getWidth() {
        return this.scale.x;
    }

    public float getHeight() {
        return this.scale.y;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float r) {
        this.rotation = r;
    }

    public void setRotationRadians(float r) {
        this.rotation = (float) Math.toDegrees(r);
    }

    public float getRotationRadians() {
        return (float) Math.toRadians(this.rotation);
    }

    public void addRotationDegree(float r) {
        this.rotation += r;
    }

    public void addRotationRadians(float r) {
        this.rotation += Math.toDegrees(r);
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

}
