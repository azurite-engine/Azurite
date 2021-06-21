package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * The GJKSM shape implementation of a circle.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Circle extends GJKSMShape {

    private final float radius;
    private final Vector2f relativeCenter;
    private Vector2f absoluteCenter;

    public Circle(Vector2f relativeCenter, float radius) {
        this.relativeCenter = new Vector2f(relativeCenter);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public Vector2f getRelativeCenter() {
        return relativeCenter;
    }

    public Vector2f getAbsoluteCenter() {
        return absoluteCenter;
    }

    @Override
    public void adjust() {
        this.absoluteCenter = position().add(this.relativeCenter, new Vector2f());
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        Vector2f normalized = v.normalize(new Vector2f());
        return absoluteCenter.add(normalized.mul(radius), new Vector2f());
    }

    /**
     * Calculate a raycast against this circle.
     *
     * @param start            the starting point of the raycast
     * @param rayDirection     the direction of the ray, it doesnt have to be normalized, it will be by the method
     * @param maxLength        the max length of ray, if it can't hit within this range, it doesnt hit
     * @param onAbsoluteCoords whether your input is relative to the circle or absolute to the world origin
     * @return the result of the raycast
     */
    public RayCastResult rayCast(Vector2f start, Vector2f rayDirection, float maxLength, boolean onAbsoluteCoords) {
        Vector2f normalizedRay = rayDirection.normalize(new Vector2f());
        Vector2f center = new Vector2f(onAbsoluteCoords ? absoluteCenter : relativeCenter);
        Vector2f toCenter = center.sub(start, new Vector2f());
        //length of the raycast to a point X alligned with the center of the circle
        float lengthA = normalizedRay.dot(toCenter);
        //distance to the point X were is measured lengthA to as well, so that lengthA, lengthB and toCenter make up a triangle
        float lengthBSquared = toCenter.lengthSquared() - lengthA * lengthA;
        float d = radius * radius - lengthBSquared;
        //doesn't work for negative numbers, therefore its not hitting the circle
        if (d < 0) return new RayCastResult();
        //the length of the point X to the border of the circle
        float lengthF = (float) Math.sqrt(d);
        //distance of the ray start to the border of the circle
        float lengthT = lengthA - lengthF;
        //the point on the circle is too far away
        if (lengthT > maxLength || lengthT < 0) return new RayCastResult();
        Vector2f strike = normalizedRay.mul(lengthT);
        Vector2f targetPoint = start.add(strike, new Vector2f());
        return new RayCastResult(targetPoint, targetPoint.sub(center, new Vector2f()).normalize(), strike, lengthT, true);
    }

}