package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class ConvexGJKSM {


    /**
     * Calculates whether a shape collides/overlaps with another shape using their support function only. <p>
     * The support function of a shape calculates the furthest point in a set direction.<p>
     * <p>
     * By GJKSM it is required, that both given shapes are convex shapes.
     * If you wanna test concave shapes, you will have to split them into convex shapes first. <p>
     * <p>
     * This method is based on the GJKSM invariant named by and after
     * GJK the basic underlying principle
     * Stelly a gamedev working at valve (at least at the time)
     * and Muratori reducing its compexity down to testing instead of heavy math.
     * <p>
     * This algorithm below is adjusted to 2d space instead of 3d space and therefore some checks arent necessary,
     * since an unfinished triangle (3 points, 2 vectors) already determines collision.
     * <p>
     * This algorithm was implemented and alternated on 19th of June by Julius K.
     *
     * @param shapeA shape a
     * @param shapeB shape b
     * @return whether shape a and shape b intersect
     */
    public static boolean gjksmCollision(GJKSMShape shapeA, GJKSMShape shapeB) {
        Vector2f anyDirectionTowardsOrigin = new Vector2f(1, 1);
        Vector2f startPoint = maxDotPointMinkDiff(shapeA, shapeB, anyDirectionTowardsOrigin);
        Vector2f direction = new Vector2f(-startPoint.x, -startPoint.y);

        int pointsConfirmed = 0;

        while (pointsConfirmed < 3) {

            //the point furthest in the direction
            // Shape combinedShape = minkDiff(shapeA, shapeB); combinedShape.supportPoint(direction);
            Vector2f pointA = maxDotPointMinkDiff(shapeA, shapeB, direction);

            if (pointA.dot(direction) < 0)
                return false; //no intersection

            //do Simplex -> two points needed for that
            {
                //vector AB
                Vector2f ab = startPoint.sub(pointA, new Vector2f()); //aToB(pointA, startPoint);
                //vector towards the origin of A
                Vector2f ao = new Vector2f(-pointA.x, -pointA.y);

                //the origin is between a and b considering perpendicular lines of ab through a and b
                if (rightDirection(ab, ao)) {

                    //one of the perpendicular vectors
                    Vector2f perpendicular = ab.perpendicular();

                    //decide if negating is necessary - it should point towards AO, therefore dot product has to be > 0
                    if (!rightDirection(perpendicular, ao))
                        perpendicular.mul(-1);

                    direction.set(perpendicular);

                    //triangular check not necessary
                    //if the next point derived from the perpendicular direction can find a point
                    //where the new point is enclosing the origin, we got it
                    pointsConfirmed++;

                } else {

                    //should only happen to search the startpoint,
                    //any second point should be cancelled out
                    startPoint = pointA;
                    direction.set(ao);

                    pointsConfirmed = 1;

                }
            }
        }

        //3 points are confirmed, there is intersection
        return true;

    }

    //helper function, just to define whether a certain point goes in the right direction
    private static boolean rightDirection(Vector2f vector, Vector2f towardsOrigin) {
        return vector.dot(towardsOrigin) > 0;
    }

    /**
     * Calculates the maximum point of a convex shape C in a specific direction, where C is considered the minkowskiDiff(A,B).
     *
     * @return the maximum point in a specific direction
     */
    private static Vector2f maxDotPointMinkDiff(GJKSMShape shapeA, GJKSMShape shapeB, Vector2f direction) {
        Vector2f pointA = shapeA.supportPoint(direction);
        Vector2f pointB = shapeB.supportPoint(new Vector2f(-direction.x, -direction.y));
        return new Vector2f(pointA.x - pointB.x, pointA.y - pointB.y);
    }

    /**
     * Finds the point with the highest dot product in a shape to a given direction d,
     * by doing a simple max search over all dot products dot(a,d) where a element of A.
     * Can be used as support function for all convex polygons defined by a finite number of points.
     *
     * @param convexShapePoints all points on the convex shape
     * @param direction         the direction for the dot product calculation
     * @return the point with the highest dot product with the given direction
     */
    public static Vector2f maxDotPoint(Vector2f[] convexShapePoints, Vector2f direction) {
        float maxDot = Float.NEGATIVE_INFINITY;
        Vector2f point = null;
        for (Vector2f p : convexShapePoints) {
            float dot = direction.dot(p);
            if (dot > maxDot) {
                maxDot = dot;
                point = p;
            }
        }
        return point;
    }

    //helper function to solve 2d linear system
    public static Vector2f solveSimultaneousEquations(float a, float b, float c, float d, float e, float f) {
        float det = ((a) * (d) - (b) * (c));  //instead of 1/
        float x = ((d) * (e) - (b) * (f)) / det;
        float y = ((a) * (f) - (c) * (e)) / det;
        return new Vector2f(x, y);
    }

    //two ray cast intersection point calculation. ray length is limited to their own length
    public static Vector2f rayCastIntersectionPoint(Vector2f pointA, Vector2f rayA, Vector2f pointB, Vector2f rayB) { //x,a,y,b
        //solve the linear equation
        Vector2f factors = solveSimultaneousEquations(rayA.x, -rayB.x, rayA.y, -rayB.y, pointB.x - pointA.x, pointB.y - pointA.y);
        //if the lines are crossing, but factors have to be between 0 and 1 to ensure, that the given vector is sufficient to reach
        if (factors.x > 1 || factors.x < 0 || factors.y > 1 || factors.y < 0) return null;
        //calculate the point where the intersection happened and return
        Vector2f dest = rayA.mul(factors.x, new Vector2f());
        return dest.add(pointA);
    }

    //ray cast intersection point with a fixed length line
    public static Vector2f rayCastToLineIntersectionPoint(Vector2f rayStart, Vector2f rayDirectionAndLength, Vector2f linePointA, Vector2f linePointB) {
        return rayCastIntersectionPoint(rayStart, rayDirectionAndLength, linePointA, linePointB.sub(linePointA, new Vector2f()));
    }

}