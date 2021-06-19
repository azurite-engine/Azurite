package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Julius Korweck
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class BasicPolygon implements Shape {

    private final Vector2f[] points;

    public BasicPolygon(Vector2f... points) {
        this.points = points;
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return ConvexGJKSM.maxDotPoint(points, v);
    }

}