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
    private final Vector2f minCorner;
    //the corner furthest away from (0,0)
    private final Vector2f maxCorner;
    //the corner above minCorner
    private final Vector2f minXCorner;
    //the corner below maxCorner
    private final Vector2f minYCorner;

    private Vector2f pMinCorner;
    private Vector2f pMaxCorner;
    private Vector2f pMinXCorner;
    private Vector2f pMinYCorner;

    public Vector2f getMaxCorner() {
        return maxCorner;
    }

    public Vector2f getMinCorner() {
        return minCorner;
    }

    public Vector2f getMinXCorner() {
        return minXCorner;
    }

    public Vector2f getMinYCorner() {
        return minYCorner;
    }

    @Override
    public void setPosition(Vector2f position) {
        super.setPosition(position);
        pMinCorner = position.add(minCorner, new Vector2f());
        pMaxCorner = position.add(maxCorner, new Vector2f());
        pMinXCorner = position.add(minXCorner, new Vector2f());
        pMinYCorner = position.add(minYCorner, new Vector2f());
    }

    /**
     * The constructor take two points of the rectangle assuming they are apart diagonally in the rectangle.
     * Therefore if the two points make a line that's parallel to one of the axis, it will assume it is just a line.
     *
     * @param cornerA the first corner
     * @param cornerB the second corner
     */
    public Rectangle(Vector2f cornerA, Vector2f cornerB) {
        minCorner = new Vector2f(Math.min(cornerA.x, cornerB.x), Math.min(cornerA.y, cornerB.y));
        maxCorner = new Vector2f(Math.max(cornerA.x, cornerB.x), Math.max(cornerA.y, cornerB.y));
        minXCorner = new Vector2f(minCorner.x, maxCorner.y);
        minYCorner = new Vector2f(maxCorner.x, minCorner.y);
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        float minDot = pMinCorner.dot(v);
        float maxDot = pMaxCorner.dot(v);
        if (minDot > maxDot) {
            return pMinXCorner.dot(v) > minDot ? pMinXCorner : pMinCorner;
        } else return pMinYCorner.dot(v) > maxDot ? pMinYCorner : pMaxCorner;
    }
}