package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Julius Korweck
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Triangle extends GJKSMShape {

    private final Vector2f a, b, c;
    private Vector2f pA, pB, pC;

    public Triangle(Vector2f a, Vector2f b, Vector2f c) {
        this.a = new Vector2f(a);
        this.b = new Vector2f(b);
        this.c = new Vector2f(c);
    }

    @Override
    public void setPosition(Vector2f position) {
        super.setPosition(position);
        this.pA = position.add(a, new Vector2f());
        this.pB = position.add(b, new Vector2f());
        this.pC = position.add(c, new Vector2f());
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        float ma = v.dot(pA), mb = v.dot(pB), mc = v.dot(pC);
        return ma > mb ? ma > mc ? pA : pC : mb > mc ? pB : pC;
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/