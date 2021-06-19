package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Julius Korweck
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Square implements Shape {

    private final Vector2f[] points;

    public Square(Vector2f cornerA, Vector2f cornerB) {
        if (cornerA.x == cornerB.x || cornerA.y == cornerB.y)
            points = new Vector2f[]{cornerA, cornerB};
        else
            points = new Vector2f[]{cornerA, cornerB,
                    new Vector2f(cornerA.x, cornerB.y), new Vector2f(cornerB.x, cornerA.y)};
    }

    @Override
    public Vector2f[] pointsArray() {
        return points;
    }

}