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
public class Rectangle implements GJKSMShape {

    private final Vector2f[] points;

    /**
     * The constructor take two points of the rectangle assuming they are apart diagonally in the rectangle.
     * Therefore if the two points make a line that's parallel to one of the axis, it will assume it is just a line.
     *
     * @param cornerA the first corner
     * @param cornerB the second corner
     */
    public Rectangle(Vector2f cornerA, Vector2f cornerB) {
        //if they are parallel to one of the axis, its just a line
        if (cornerA.x == cornerB.x || cornerA.y == cornerB.y)
            points = new Vector2f[]{new Vector2f(cornerA), new Vector2f(cornerB)};
        else
            points = new Vector2f[]{new Vector2f(cornerA), new Vector2f(cornerB),
                    new Vector2f(cornerA.x, cornerB.y), new Vector2f(cornerB.x, cornerA.y)};
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return ConvexGJKSM.maxDotPoint(points, v);
    }
}