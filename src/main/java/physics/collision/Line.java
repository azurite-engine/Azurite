package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * The GJKSM shape implementation of a line.
 *
 * @author Julius Korweck
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Line extends GJKSMShape {

    private final Vector2f a, b;
    //positioned vectors
    private Vector2f pA, pB;

    public Line(Vector2f a, Vector2f b) {
        this.a = new Vector2f(a);
        this.b = new Vector2f(b);
    }

    @Override
    public void setPosition(Vector2f position) {
        super.setPosition(position);
        this.pA = position.add(a, new Vector2f());
        this.pB = position.add(b, new Vector2f());
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return pA.dot(v) > pB.dot(v) ? pA : pB;
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/