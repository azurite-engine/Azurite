package physics.collision;

import org.joml.Vector2f;

import java.util.Arrays;

/**
 * <h1>Azurite</h1>
 *
 * @author Julius Korweck
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class ConvexGJK {


    //TODO: NOT DONE YET
    public static boolean gjkCollision(Shape shapeA, Shape shapeB) {
        //create the "super shape" of a and b by using the minkDiff
        Shape combinedShape = minkDiff(shapeA, shapeB);
        Vector2f[] combinedShapePoints = combinedShape.pointsArray();
        //sorting the array by an approx calculation of how close they are to the origin
        Arrays.sort(combinedShapePoints, (o1, o2) -> (int) (o1.x + o1.y - (o2.x + o2.y)));
        Vector2f startPoint = combinedShapePoints[0];
        Vector2f direction = new Vector2f(-startPoint.x, -startPoint.y);

        boolean repeat = true;
        boolean firstPointConfirmed = false;

        while (repeat) {
            //only repeat if necessary
            repeat = false;

            //the point furthest in the direction
            Vector2f pointA = combinedShape.supportPoint(direction);
            //
            if (pointA.dot(direction) < 0)
                return false; //no intersection

            //TODO: decide if the second point is correct and if the intersection was found
            if(firstPointConfirmed)
                return true;

            //do Simplex -> two points needed for that
            {
                //vector AB
                Vector2f ab = aToB(pointA, startPoint);
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
                    //where the new point
                    firstPointConfirmed = true;

                } else {
                    startPoint = pointA;
                    direction.set(ao);
                    repeat = true;
                }
            }
        }

        //does not intersect, if there isnt an intersection found
        return false;
    }

    private static Vector2f aToB(Vector2f a, Vector2f b) {
        return new Vector2f(b.x - a.x, b.y - a.y);
    }

    private static boolean rightDirection(Vector2f vector, Vector2f towardsOrigin) {
        return vector.dot(towardsOrigin) > 0;
    }

    /**
     * Calculate the minkowski sum of two sets of points in 2-dimensional space.
     * This method is not optimized at all and simple adds up all point combinations there are.
     *
     * @param shapeA shape a
     * @param shapeB shape b
     * @return the minkowski sum of both shapes
     */
    public static Vector2f[] minkSum(Shape shapeA, Shape shapeB) {
        Vector2f[] sum = new Vector2f[shapeA.points() + shapeB.points()];
        int pos = 0;
        for (Vector2f a : shapeA.pointsArray()) {
            for (Vector2f b : shapeB.pointsArray()) {
                sum[pos++] = new Vector2f(a.x + b.x, a.y + b.y);
            }
        }
        return sum;
    }

    /**
     * Calculate the minkowski difference of two sets of points in 2-dimensional space.
     * This method is using the same approach as {@link ConvexGJK#minkSum(Shape, Shape)} to do that.
     *
     * @param shapeA shape a
     * @param shapeB shape b is being subtracted
     * @return the minkowski difference of both shapes
     */
    public static Shape minkDiff(Shape shapeA, Shape shapeB) {
        Vector2f[] sum = new Vector2f[shapeA.points() + shapeB.points()];
        int pos = 0;
        for (Vector2f a : shapeA.pointsArray()) {
            for (Vector2f b : shapeB.pointsArray()) {
                sum[pos++] = new Vector2f(a.x - b.x, a.y - b.y);
            }
        }
        return new BasicPolygon(sum);
    }

    /**
     * Calculates the maximum point of a convex shape C in a specific direction, where C is considered the minkDiff(A,B).
     *
     * @return the maximum point in a specific direction
     */
    public static Vector2f maxDotPointMinkDiff(Shape shapeA, Shape shapeB, Vector2f direction) {
        Vector2f pointA = shapeA.supportPoint(direction);
        Vector2f pointB = shapeB.supportPoint(new Vector2f(-direction.x, -direction.y));
        return new Vector2f(pointA.x - pointB.x, pointA.y - pointB.y);
    }

    /**
     * Finds the point with the highest dot product in a shape A to a given direction d,
     * by doing a simple max search over all dot products dot(a,d) where A element of A.
     *
     * @param shape     the shape
     * @param direction the direction for the dot product calculation
     * @return the point with the highest dot product with the given direction
     */
    public static Vector2f maxDotPoint(Shape shape, Vector2f direction) {
        float maxDot = Float.NEGATIVE_INFINITY;
        Vector2f point = null;
        for (Vector2f p : shape.pointsArray()) {
            float dot = direction.dot(p);
            if (dot > maxDot) {
                maxDot = dot;
                point = p;
            }
        }
        return point;
    }

}