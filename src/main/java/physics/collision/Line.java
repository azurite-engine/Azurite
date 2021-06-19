package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * The GJKSM shape implementation of a line.
 *
 * @author Julius Korweck
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Line implements GJKSMShape {

    private final Vector2f a, b;

    public Line(Vector2f a, Vector2f b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return a.dot(v) > b.dot(v) ? a : b;
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/