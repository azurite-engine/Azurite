package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * The GJKSM shape is described by all its support vectors.
 * A support vector is a point on the convex hull of a shape, that is furthest in the direction of a specified vector v.
 *
 * @author Julius Korweck
 * @version 18.06.2021
 * @since 18.06.2021
 */
public abstract class GJKSMShape {

    private Vector2f position;

    public GJKSMShape(Vector2f position) {
        this.position = position;
    }

    public GJKSMShape() {
        this.position = new Vector2f(0, 0);
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public final Vector2f getPosition() {
        return position;
    }

    /**
     * According to GJKSM this method is supposed to calculate the point of the shape, that is most in direction of v.
     * The general rule is, the more primitive the shape is, the more efficient this method can be.
     * This method may be described as max{v*x,x element of Shape} for any complex shape.
     *
     * @param v the direction
     * @return the point of the shape that is most in the direction of v
     */
    public abstract Vector2f supportPoint(Vector2f v);

}