package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 21.06.2021
 * @since 21.06.2021
 */
public class RayCastResult {

    private Vector2f point;
    private Vector2f normal;
    private Vector2f strike;
    private float strikeLength;
    private boolean hit;

    public RayCastResult() {
        this.point = null;
        this.normal = null;
        this.strike = null;
        this.strikeLength = 0;
        this.hit = false;
    }

    public RayCastResult(Vector2f point, Vector2f normal, Vector2f strike, float strikeLength, boolean hit) {
        this.point = point;
        this.normal = normal;
        this.strike = strike;
        this.strikeLength = strikeLength;
        this.hit = hit;
    }

    public float getStrikeLength() {
        return strikeLength;
    }

    public Vector2f getNormal() {
        return normal;
    }

    public Vector2f getPoint() {
        return point;
    }

    public Vector2f getStrike() {
        return strike;
    }

    public boolean didHit() {
        return hit;
    }

    @Override
    public String toString() {
        return "RayCastResult{" +
                "point=" + point +
                ", normal=" + normal +
                ", strike=" + strike +
                ", strikeLength=" + strikeLength +
                ", hit=" + hit +
                '}';
    }
}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/