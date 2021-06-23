package physics.collision;

import org.joml.Matrix3x2f;
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
    public static boolean gjksmCollision(Shape shapeA, Shape shapeB) {
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
    private static Vector2f maxDotPointMinkDiff(Shape shapeA, Shape shapeB, Vector2f direction) {
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

    /**
     * Calculates the convex hull of a given set of points using Jarvis March.
     * The result will have the same length as the input, but not all of them have to be filled in depending on the input.
     * If the input set is just a set of points already defining a convex polygon, the points are just sorted.
     *
     * @param points the set of points forming the convex shape
     * @return a sorted array of points forming the convex hull of all input points
     */
    public static Vector2f[] convexHull(Vector2f[] points) {
        int n = points.length;
        //less than 3 points would not be necessary
        if (n < 3) return points;

        Vector2f[] ordered = new Vector2f[points.length];
        int last = 0;

        // find the leftmost point
        int leftMost = 0;
        for (int i = 1; i < n; i++)
            if (points[i].x < points[leftMost].x)
                leftMost = i;
        int start = leftMost, curr;

        ordered[last++] = points[start];
        // iterate till start becomes leftMost
        do {
            curr = (start + 1) % n;
            for (int i = 0; i < n; i++) {
                Vector2f p = points[start];
                Vector2f q = points[i];
                Vector2f r = points[curr];
                if ((q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y) < 0)
                    curr = i;
            }
            ordered[last++] = points[curr];
            start = curr;
        } while (start != leftMost);
        return ordered;
    }

    /**
     * A universal linear system solver for two dimensional linear systems.
     * Works usually by shifting the left 2x2 matrix to an identity matrix and read the results on the right side.
     * This method here is using a mathematical approach and is designed to find only one result,
     * if there is none, it may result in some kind of infinity. For more than 1 result the behaviour is undefined.
     * Primarily used to find the intersection point of two lines or rays.
     *
     * @param linearSystem the two dimensional linear system
     * @return the solution of linear system, {@link Matrix3x2f#m20} will be x and {@link Matrix3x2f#m21} will be y in the result vector
     */
    public static Vector2f solveSimultaneousEquations(Matrix3x2f linearSystem) {
        return solveSimultaneousEquations(linearSystem.m00, linearSystem.m10, linearSystem.m01, linearSystem.m11, linearSystem.m20, linearSystem.m21);
    }

    /**
     * Cast two rays and see if they intersect.
     *
     * @param pointA the starting point of ray 1
     * @param rayA   the direction and length of ray 1
     * @param pointB the starting point of ray 2
     * @param rayB   the direction and length of ray 2
     * @return true if both rays intersect within their given length and direction
     */
    public static Vector2f rayCastIntersectionPoint(Vector2f pointA, Vector2f rayA, Vector2f pointB, Vector2f rayB) { //x,a,y,b
        //solve the linear equation
        Vector2f factors = solveSimultaneousEquations(rayA.x, -rayB.x, rayA.y, -rayB.y, pointB.x - pointA.x, pointB.y - pointA.y);
        //if the lines are crossing, but factors have to be between 0 and 1 to ensure, that the given vector is sufficient to reach
        if (factors.x > 1 || factors.x < 0 || factors.y > 1 || factors.y < 0) return null;
        //calculate the point where the intersection happened and return
        Vector2f dest = rayA.mul(factors.x, new Vector2f());
        return dest.add(pointA);
    }

    /**
     * Cast a ray against a fixed line and see if they intersect.
     *
     * @param rayStart              the starting point of the ray
     * @param rayDirectionAndLength the direction and length of the ray
     * @param linePointA            the start point of the line
     * @param linePointB            the end point of the line
     * @return true if the ray cast intersects with the given line
     */
    public static Vector2f rayCastToLineIntersectionPoint(Vector2f rayStart, Vector2f rayDirectionAndLength, Vector2f linePointA, Vector2f linePointB) {
        return rayCastIntersectionPoint(rayStart, rayDirectionAndLength, linePointA, linePointB.sub(linePointA, new Vector2f()));
    }

    //helper function to solve 2d linear system
    private static Vector2f solveSimultaneousEquations(float a, float b, float c, float d, float e, float f) {
        float det = a * d - b * c;  //instead of 1/
        float x = (d * e - b * f) / det;
        float y = (a * f - c * e) / det;
        return new Vector2f(x, y);
    }

    /**
     * Calculates the radius of a minimum sphere that contains the given polygon and shares the same centroid.
     *
     * @param centroid the centroid of the polygon
     * @param vertices the vertices of the polygon
     * @return the minimum sphere containing the polygon
     */
    public static float boundingSphere(Vector2f centroid, Vector2f... vertices) {
        if (vertices.length < 1) return 0;
        if (vertices.length == 1) return centroid.sub(vertices[0]).length();
        float max = centroid.sub(vertices[0]).lengthSquared();
        float currDist;
        int current = 1;
        do {
            currDist = centroid.sub(vertices[current]).lengthSquared();
            if (currDist > max)
                max = currDist;
            current++;
        }
        while (vertices.length > current);
        return (float) Math.sqrt(max);
    }

    /**
     * Calculate the centroid of a polygon.
     *
     * @param vertices the vertices of the polygon
     * @return the centroid of a polygon
     */
    public static Vector2f polygonCentroid(Vector2f... vertices) {
        if (vertices.length == 1) return vertices[0];
        if (vertices.length < 1) return null;
        Vector2f centroid = new Vector2f();
        double signedArea = 0;
        Vector2f prev = vertices[vertices.length - 1];

        //for all vertices in a loop
        for (Vector2f next : vertices) {
            //partial area
            double a = prev.x * next.y - next.x * prev.y;
            //move centroid towards edge with weight relative to partial area a
            centroid.x += (prev.x + next.x) * a;
            centroid.y += (prev.y + next.y) * a;
            // sum up area
            signedArea += a;
            prev = next;
        }

        //final summary according to polygon centroid calculation
        //by using gauss's area formula
        signedArea *= 0.5;
        centroid.x /= (6.0 * signedArea);
        centroid.y /= (6.0 * signedArea);

        return centroid;
    }

}