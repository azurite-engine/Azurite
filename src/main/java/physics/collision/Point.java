package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * The GJKSM shape implementation of a single point.
 *
 * @author Julius Korweck
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Point implements GJKSMShape {

    private final Vector2f point;

    public Point(Vector2f point) {
        this.point = point;
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return point;
    }
}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/