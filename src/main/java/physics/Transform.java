package physics;

import org.joml.Vector2f;

/**
 * Represents the position in X and Y coordinates,rotation and the scale (width and height) of a gameObject
 * TODO: change to a component? - no don't do that
 */
public class Transform {

    public static final Vector2f ZERO = new Vector2f(0, 0);
    public Vector2f scale;
    private Vector2f position;
    private Vector2f positionBuffer;
    private float rotation;

    /**
     * Creates a new empty transform.
     */
    public Transform() {
        init(new Vector2f(), 0, new Vector2f());
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
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
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
     * @param o physics.Transform to be checked for equality against this instance of physics.Transform.
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

    public void resetPositionBuffer() {
        this.positionBuffer = new Vector2f();
    }

    public boolean applyPositionBuffer() {
        if (!positionBuffer.isFinite() || positionBuffer.equals(ZERO)) return false;
        this.position.add(positionBuffer);
        return true;
    }

    public Vector2f positionBuffer() {
        return positionBuffer;
    }

    /*
     * I don't think that these need any explanation...
     */
    public float getX() {
        return this.position.x;
    }

    public float getY() {
        return this.position.y;
    }

    public void addY(float y) {
        positionBuffer.add(0, y);
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

    public float getRotationRadians() {
        return (float) Math.toRadians(this.rotation);
    }

    public void addRotation(float r) {
        this.rotation += r;
    }

    public Vector2f getPosition() {
        return position;
    }

    //this method is dangerous, use it with caution. it can break physics when called at the wrong time
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
