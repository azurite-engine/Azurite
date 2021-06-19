package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * Used to represent a combined shape consisting of 2 different shapes of any kind,
 * they might even be CombinedShapes themself.
 * Using the class will erase the points function;
 *
 * @author Julius Korweck
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class CombinedShape implements GJKSMShape {

    private GJKSMShape shapeA, shapeB;

    public CombinedShape(GJKSMShape shapeA, GJKSMShape shapeB) {
        this.shapeA = shapeA;
        this.shapeB = shapeB;
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        //calculate for both shapes
        Vector2f supA = shapeA.supportPoint(v);
        Vector2f supB = shapeB.supportPoint(v);
        //decide with point is furthest in direction v by using dot product
        return supA.dot(v) > supB.dot(v) ? supA : supB;
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/