package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * The GJKSM shape implementation of a rectangle.
 *
 * @author Julius Korweck
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Rectangle extends GJKSMShape {

    //the corner closest to (0,0)
    private final Vector2f relativeMinCorner;
    //the corner furthest away from (0,0)
    private final Vector2f relativeMaxCorner;
    //the corner above minCorner
    private final Vector2f relativeMinXCorner;
    //the corner below maxCorner
    private final Vector2f relativeMinYCorner;

    private Vector2f absoluteMinCorner;
    private Vector2f absoluteMaxCorner;
    private Vector2f absoluteMinXCorner;
    private Vector2f absoluteMinYCorner;

    public Vector2f getRelativeMaxCorner() {
        return relativeMaxCorner;
    }

    public Vector2f getRelativeMinCorner() {
        return relativeMinCorner;
    }

    public Vector2f getRelativeMinXCorner() {
        return relativeMinXCorner;
    }

    public Vector2f getRelativeMinYCorner() {
        return relativeMinYCorner;
    }

    public Vector2f getAbsoluteMaxCorner() {
        return absoluteMaxCorner;
    }

    public Vector2f getAbsoluteMinCorner() {
        return absoluteMinCorner;
    }

    public Vector2f getAbsoluteMinXCorner() {
        return absoluteMinXCorner;
    }

    public Vector2f getAbsoluteMinYCorner() {
        return absoluteMinYCorner;
    }

    @Override
    public void adjust() {
        absoluteMinCorner = position().add(relativeMinCorner, new Vector2f());
        absoluteMaxCorner = position().add(relativeMaxCorner, new Vector2f());
        absoluteMinXCorner = position().add(relativeMinXCorner, new Vector2f());
        absoluteMinYCorner = position().add(relativeMinYCorner, new Vector2f());
    }

    /**
     * The constructor take two points of the rectangle assuming they are apart diagonally in the rectangle.
     * Therefore if the two points make a line that's parallel to one of the axis, it will assume it is just a line.
     *
     * @param cornerA the first corner
     * @param cornerB the second corner
     */
    public Rectangle(Vector2f cornerA, Vector2f cornerB) {
        relativeMinCorner = new Vector2f(Math.min(cornerA.x, cornerB.x), Math.min(cornerA.y, cornerB.y));
        relativeMaxCorner = new Vector2f(Math.max(cornerA.x, cornerB.x), Math.max(cornerA.y, cornerB.y));
        relativeMinXCorner = new Vector2f(relativeMinCorner.x, relativeMaxCorner.y);
        relativeMinYCorner = new Vector2f(relativeMaxCorner.x, relativeMinCorner.y);
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        float minDot = absoluteMinCorner.dot(v);
        float maxDot = absoluteMaxCorner.dot(v);
        if (minDot > maxDot) {
            return absoluteMinXCorner.dot(v) > minDot ? absoluteMinXCorner : absoluteMinCorner;
        } else return absoluteMinYCorner.dot(v) > maxDot ? absoluteMinYCorner : absoluteMaxCorner;
    }
}