package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Julius Korweck
 * @version 18.06.2021
 * @since 18.06.2021
 */
public interface Shape {

    /**
     * This method is supposed to give all points on the shapes convex hull.
     * If this method cannot produce a finite number of points due to the shape (e.g. circle),
     * it is required to overwrite the method {@link this#supportPoint(Vector2f)} aswell.
     *
     * @return all points on the convex hull
     */
    Vector2f[] pointsArray();

    /**
     * @return the amount of points this shape consists of
     */
    default int points() {
        return pointsArray().length;
    }

    /**
     * According to GJK this method is supposed to calculate the point of the shape, that is most in direction of v.
     * The general rule is, the more primitive the shape is, the more efficient this method can be.
     * This method may be described as max{v*x,x element of Shape} for any complex shape.
     *
     * @param v the direction
     * @return the point of the shape that is most in the direction of v or null if and only if {@link this#pointsArray()} is empty
     */
    default Vector2f supportPoint(Vector2f v) {
        return ConvexGJK.maxDotPoint(this, v);
    }

}