package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Julius Korweck
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Triangle implements GJKSMShape {

    private final Vector2f[] points;

    public Triangle(Vector2f a, Vector2f b, Vector2f c) {
        this.points = new Vector2f[]{new Vector2f(a), new Vector2f(b), new Vector2f(c)};
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return ConvexGJKSM.maxDotPoint(points, v);
    }
}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/