package util;

import org.joml.Matrix3x2f;
import org.joml.Vector2f;
import physics.collision.CollisionInformation;
import physics.collision.shape.PrimitiveShape;
import physics.collision.shape.ShapeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An util class containing a lot of useful methods to do maths and physics calculations.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 07.12.2021
 */
public class MathUtils {


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
    public static CollisionInformation gjksmCollision(PrimitiveShape shapeA, PrimitiveShape shapeB) {
        Vector2f startPoint = maxDotPointMinkDiff(shapeA, shapeB, shapeA.centroid().mul(-1, new Vector2f()));
        Vector2f direction = new Vector2f(-startPoint.x, -startPoint.y);

        //will contain the triangle containing the origin
        Triple<Vector2f, Vector2f, Vector2f> simplex = new Triple<>();

        simplex.setLeft(startPoint);

        int pointsConfirmed = 1;
        int loop = 0;
        int maxLoop = shapeA.vertices() + shapeB.vertices();
        if (shapeA.type() == ShapeType.CIRCLE || shapeB.type() == ShapeType.CIRCLE) maxLoop += 20;

        while (pointsConfirmed < 3) {

            loop++;
            //loop at max 20 times plus the amount of both shapes faces, so that there is enough room to find collision.
            //this is obviously not a good solution, but it should do fine for now.
            if (loop > maxLoop) return new CollisionInformation(null, false);

            //the point furthest in the direction
            //Shape combinedShape = minkDiff(shapeA, shapeB); combinedShape.supportPoint(direction);
            Vector2f pointA = maxDotPointMinkDiff(shapeA, shapeB, direction);

            if (pointA.dot(direction) < 0) return new CollisionInformation(null, false); //no intersection

            //do Simplex -> two points needed for that
            {
                //vector AB
                Vector2f ab = startPoint.sub(pointA, new Vector2f()); //aToB(pointA, startPoint);
                //vector towards the origin of A
                Vector2f ao = new Vector2f(-pointA.x, -pointA.y);

                //the origin is between a and b considering perpendicular lines of ab through a and b
                if (rightDirection(ab, ao)) {

                    //one of the perpendicular vectors
                    Vector2f perpendicular = new Vector2f(ab).perpendicular();

                    //decide if negating is necessary - it should point towards AO, therefore dot product has to be > 0
                    if (!rightDirection(perpendicular, ao)) perpendicular.mul(-1);

                    direction.set(perpendicular);

                    //points.add(ao);
                    if (pointsConfirmed == 1) simplex.setMiddle(pointA);
                    else if (pointsConfirmed == 2) simplex.setRight(pointA);

                    //triangular check not necessary
                    //if the next point derived from the perpendicular direction can find a point
                    //where the new point is enclosing the origin, we got it
                    pointsConfirmed++;

                } else {

                    //should only happen to search the startpoint,
                    //any second point should be cancelled out
                    startPoint = pointA;
                    direction.set(ao);
                    simplex.setLeft(pointA);

                    pointsConfirmed = 1;

                }
            }
        }

        //3 points are confirmed, there is intersection
        Vector2f[] vector2fs = Arrays.asList(simplex.getLeft(), simplex.getMiddle(), simplex.getRight()).toArray(new Vector2f[0]);
        return new CollisionInformation(vector2fs, true);

    }

    //helper function, just to define whether a certain point goes in the right direction
    private static boolean rightDirection(Vector2f vector, Vector2f towardsOrigin) {
        return vector.dot(towardsOrigin) > 0;
    }

    /**
     * The EPA algorithm to find a penetration vector of two given shapes and the result of the GJK algorithm.
     * Since it depends on the GJK algorithm, this algorithm will fail in an undefined way,
     * if the shapes do not actually collide.
     *
     * @param shapeA  the first shape
     * @param shapeB  the second shape, that collides with the first one
     * @param simplex the simplex returned by GJK or any simplex inside both shapes borders enclosing the overlapping area of both shapes
     * @return the penetration vector of both shapes, if there is one found within a series of steps
     */
    public static Optional<Vector2f> expandingPolytopeAlgorithm(PrimitiveShape shapeA, PrimitiveShape shapeB, Vector2f[] simplex) {
        int faceSize = shapeA.vertices() + shapeB.vertices();
        List<Vector2f> polygon = new ArrayList<>(faceSize);
        polygon.addAll(Arrays.asList(simplex));

        Vector2f normal;
        for (int i = 0; i < faceSize + 1; i++) {
            Triple<Integer, Float, Vector2f> closestFace = closestFaceToOrigin(polygon); //index, dist, norm
            float squareLength = closestFace.getMiddle();
            //vector from origin to face
            normal = closestFace.getRight();
            Vector2f vector2f = maxDotPointMinkDiff(shapeA, shapeB, normal);
            //if the vector*normal is close to normal*normal, its the point we seek
            if (Math.abs(normal.dot(vector2f) - squareLength) < 0.001f) return Optional.of(normal.mul(-1));
            polygon.add(1 + closestFace.getLeft(), vector2f);
        }
        return Optional.empty();
    }

    //helper method for EPA - calculates the the closest face to the origin of a given simplex representing a polygon
    private static Triple<Integer, Float, Vector2f> closestFaceToOrigin(List<Vector2f> simplex) {
        int index = 0;
        //ensures, that the first calculation will be less then this
        float dist = Float.POSITIVE_INFINITY;
        Vector2f hitPoint = null;
        //calculate for all faces of the simplex its distance using the helper method
        //and determine if the next face is closer then all faces before
        for (int i = 0; i < simplex.size(); i++) {
            Vector2f pointA = simplex.get(i);
            Vector2f pointB = simplex.get((i + 1) % simplex.size());
            Vector2f hit = faceDistanceToOriginVector(pointA, pointB);
            float v = hit.lengthSquared();
            if (dist > v) {
                dist = v;
                index = i;
                hitPoint = hit;
            }
        }
        //return the index of the start point of the face inside the simplex,
        //the distance squared to the origin
        //and the point on the face, thats closed to the origin and in line with the normal of the face
        return new Triple<>(index, dist, hitPoint);
    }

    //helper method for EPA - calculates the distance of a face to the origin using linear algebra instead of expensive square roots
    private static Vector2f faceDistanceToOriginVector(Vector2f pointA, Vector2f secondPoint) {
        Vector2f rayA = secondPoint.sub(pointA, new Vector2f());
        Vector2f rayB = new Vector2f(rayA).perpendicular();
        Vector2f pointB = new Vector2f(0, 0);
        //solve the linear equation
        Vector2f factors = solveSimultaneousEquations(rayA.x, -rayB.x, rayA.y, -rayB.y, pointB.x - pointA.x, pointB.y - pointA.y);
        //calculate the point where the intersection happened and return
        return rayB.mul(factors.y); //+pointB is not necessary, its the origin
    }

    /**
     * Calculates the maximum point of a convex shape C in a specific direction, where C is considered the minkowskiDiff(A,B).
     *
     * @return the maximum point in a specific direction
     */
    public static Vector2f maxDotPointMinkDiff(PrimitiveShape shapeA, PrimitiveShape shapeB, Vector2f direction) {
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
     * @param direction         the direction/reach to look for a point
     * @return the point with the highest dot product by the given direction
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

        Vector2f[] ordered = new Vector2f[n];
        int last = 0;

        // find the leftmost point
        int leftMost = 0;
        for (int i = 1; i < n; i++)
            if (points[i].x < points[leftMost].x) leftMost = i;
        int start = leftMost, curr;

        ordered[last++] = points[start];
        // iterate till start becomes leftMost
        do {
            curr = (start + 1) % n;
            for (int i = 0; i < n; i++) {
                Vector2f p = points[start];
                Vector2f q = points[i];
                Vector2f r = points[curr];
                if ((q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y) < 0) curr = i;
            }
            ordered[last++ % n] = points[curr];
            start = curr;
        } while (start != leftMost);
        //are any points unused for the hull
        if (last >= n) return ordered;
        else return Arrays.copyOf(ordered, last);
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
     * @return the intersection point
     * @see MathUtils#rayCastIntersection(Vector2f, Vector2f, Vector2f, Vector2f)
     */
    public static Optional<Vector2f> rayCastIntersectionPoint(Vector2f pointA, Vector2f rayA, Vector2f pointB, Vector2f rayB) { //x,a,y,b
        return rayCastIntersection(pointA, rayA, pointB, rayB).map(Pair::getLeft);
    }

    /**
     * Cast two rays and see if they intersect.
     * The solution will be calculated based on endless rays with a fixpoint and a ray direction.
     * If a there is intersection point of both ray, they arent parallel and the factors x and y are calculated,
     * so that intersection = pointA+x*rayA = pointB+y*rayB is valid.
     * To ensure a finite ray, the conditions 0 <= x <= 1 and 0 <= y <= 1 are checked.
     *
     * @param pointA the starting point of ray 1
     * @param rayA   the direction and length of ray 1
     * @param pointB the starting point of ray 2
     * @param rayB   the direction and length of ray 2
     * @return a pair containing the intersection point first and a vector with factors x and y
     */
    public static Optional<Pair<Vector2f, Vector2f>> rayCastIntersection(Vector2f pointA, Vector2f rayA, Vector2f pointB, Vector2f rayB) { //x,a,y,b
        //solve the linear equation
        Vector2f factors = solveSimultaneousEquations(rayA.x, -rayB.x, rayA.y, -rayB.y, pointB.x - pointA.x, pointB.y - pointA.y);
        //if the lines are crossing, but factors have to be between 0 and 1 to ensure, that the given vector is sufficient to reach
        if (factors.x > 1 || factors.x < 0 || factors.y > 1 || factors.y < 0) return Optional.empty();
        //calculate the point where the intersection happened and return
        Vector2f dest = rayA.mul(factors.x, new Vector2f());
        return Optional.of(new Pair<>(dest.add(pointA), factors));
    }

    /**
     * Cast a ray against a fixed line and see if they intersect.
     *
     * @param rayStart              the starting point of the ray
     * @param rayDirectionAndLength the direction and length of the ray
     * @param linePointA            the start point of the line
     * @param linePointB            the end point of the line
     * @return the intersection point
     */
    public static Optional<Vector2f> rayCastToLineIntersectionPoint(Vector2f rayStart, Vector2f rayDirectionAndLength, Vector2f linePointA, Vector2f linePointB) {
        return rayCastIntersectionPoint(rayStart, rayDirectionAndLength, linePointA, linePointB.sub(linePointA, new Vector2f()));
    }

    //helper function to solve 2d linear system
    private static Vector2f solveSimultaneousEquations(float a, float b, float c, float d, float e, float f) {
        float det = 1 / (a * d - b * c);
        float x = (d * e - b * f);
        float y = (a * f - c * e);
        return new Vector2f(x, y).mul(det);
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
        if (vertices.length == 1) return centroid.sub(vertices[0], new Vector2f()).length();
        float max = centroid.sub(vertices[0], new Vector2f()).lengthSquared();
        float currDist;
        int current = 1;
        do {
            currDist = centroid.sub(vertices[current], new Vector2f()).lengthSquared();
            if (currDist > max) max = currDist;
            current++;
        } while (vertices.length > current);
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
            float a = prev.x * next.y - next.x * prev.y;
            //move centroid towards edge with weight relative to partial area a
            centroid.add((prev.x + next.x) * a, (prev.y + next.y) * a);
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

    /**
     * Checks if a set of X and Y coordinates are inside of a circle.
     *
     * @param inX     X position of point to check
     * @param inY     Y position of point to check
     * @param circleX X position of circle
     * @param circleY Y position of circle
     * @param radius  Radius of circle
     * @return Returns true if the point is inside the circle, otherwise returns false.
     */
    public static boolean inCircle(float inX, float inY, float circleX, float circleY, float radius) {
        return (inX - circleX) * (inX - circleX) + (inY - circleY) * (inY - circleY) <= radius * radius;
    }

    /**
     * Checks if a set of X and Y coordinates are inside of a circle.
     *
     * @param in      physics.org.joml.Vector2f containing coordinates of point to check
     * @param circleX X position of circle
     * @param circleY Y position of circle
     * @param radius  Radius of circle
     * @return Returns true if the point is inside the circle, otherwise returns false.
     */
    public static boolean inCircle(Vector2f in, float circleX, float circleY, float radius) {
        return inCircle(in.x, in.y, circleX, circleY, radius);
    }

    /**
     * Checks if a set of X and Y coordinates are inside of a circle.
     *
     * @param in     physics.Vector2f containing coordinates of point to check
     * @param circle physics.Vector2f containing coordinates of the center of the circle
     * @param radius Radius of circle
     * @return Returns true if the point is inside the circle, otherwise returns false.
     */
    public static boolean inCircle(Vector2f in, Vector2f circle, float radius) {
        return inCircle(in, circle.x, circle.y, radius);
    }

    /**
     * Checks if a set of X and Y coordinates are inside of a rectangle.
     *
     * @param inX        X position of point to check
     * @param inY        Y position of point to check
     * @param rectX      X position of rectangle
     * @param rectY      Y position of rectangle
     * @param rectWidth  Width of rectangle
     * @param rectHeight Height of rectangle
     * @return Returns true if the point is inside the rectangle, otherwise returns false.
     */
    public static boolean inRect(float inX, float inY, float rectX, float rectY, float rectWidth, float rectHeight) {
        return inX >= rectX && inX <= (rectY + rectWidth) && inY >= rectY && inY <= (rectY + rectHeight);
    }

    /**
     * Checks if a set of X and Y coordinates are inside of a rectangle.
     *
     * @param in         physics.Vector2f containing coordinates of point to check
     * @param rectX      X position of rectangle
     * @param rectY      Y position of rectangle
     * @param rectWidth  Width of rectangle
     * @param rectHeight Height of rectangle
     * @return Returns true if the point is inside the rectangle, otherwise returns false.
     */
    public static boolean inRect(Vector2f in, float rectX, float rectY, float rectWidth, float rectHeight) {
        return in.x >= rectX && in.x <= (rectX + rectWidth) && in.y >= rectY && in.y <= (rectY + rectHeight);
    }

    /**
     * Checks if a rectangle is completely inside of another rectangle.
     *
     * @param t1
     * @param t2
     * @return returns boolean true if t1 is inside of t2
     */
    public static boolean rectInRect(Transform t1, Transform t2) {
        return inRect(new Vector2f(t1.getX(), t1.getY()), t2.getX(), t2.getY(), t2.getWidth(), t2.getHeight()) && inRect(new Vector2f(t1.getX(), t1.getY() + t1.getHeight()), t2.getX(), t2.getY(), t2.getWidth(), t2.getHeight()) && inRect(new Vector2f(t1.getX() + t1.getWidth(), t1.getY()), t2.getX(), t2.getY(), t2.getWidth(), t2.getHeight()) && inRect(new Vector2f(t1.getX() + t1.getWidth(), t1.getY() + t1.getHeight()), t2.getX(), t2.getY(), t2.getWidth(), t2.getHeight());
    }

    /**
     * Transforms an angle from degree into radian.
     *
     * @param degree the angle in degree
     * @return the radian angle
     */
    public static float radian(float degree) {
        return (float) Math.toRadians(degree);
    }

    /**
     * Rotates a point around the origin of the system.
     *
     * @param point       the point to rotate
     * @param radianAngle the radian angle to rotate
     * @return a new point that is rotated by radianAngle starting from the given point
     */
    public static Vector2f rotateAroundOrigin(Vector2f point, float radianAngle) {
        float cos = (float) Math.cos(radianAngle);
        float sin = (float) Math.sin(radianAngle);
        return new Vector2f(point.x * cos - point.y * sin, point.y * cos + point.x * sin);
    }

    /**
     * Rotates a point around a given point in the same system.
     *
     * @param point          the point to rotate
     * @param relativeOrigin the point to rotate around
     * @param radianAngle    the radian angle to rotate
     * @return a new point that is rotated by radianAngle around the given relativeOrigin point starting from the given point
     */
    public static Vector2f rotateAroundPoint(Vector2f point, Vector2f relativeOrigin, float radianAngle) {
        point = point.sub(relativeOrigin, new Vector2f());
        float cos = (float) Math.cos(radianAngle);
        float sin = (float) Math.sin(radianAngle);
        return new Vector2f(point.x * cos - point.y * sin, point.y * cos + point.x * sin).add(relativeOrigin);
    }

    /**
     * Generates a random number from a range of floats.
     *
     * @param min Minimum possible output
     * @param max Maximum possible output
     * @return returns a random float from the range passed.
     */
    public static float random(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    /**
     * Generates a random number from a range of ints.
     *
     * @param min Minimum possible output
     * @param max Maximum possible output
     * @return returns a random int from the range passed.
     */
    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    /**
     * This is for people who are too lazy to even import Math, but do want to import util.Utils. ¯\_(ツ)_/¯
     *
     * @param x Number to be rounded
     * @return Returns the rounded number "x".
     */
    public static float round(float x) {
        return Math.round(x);
    }

    /**
     * Returns the distance between two sets of X and Y coordinates in the form of "physics.Vector2"s.
     *
     * @param pos1 First physics.Vector2 position
     * @param pos2 Second physics.Vector2 position
     * @return Returns the distance as a float.
     */
    public static float dist(Vector2f pos1, Vector2f pos2) {
        return (float) Math.hypot(pos1.x - pos2.x, pos1.y - pos2.y);
    }

    /**
     * Re-maps a number from one range to another.
     *
     * @param value  Number to me re-mapped
     * @param start1 Lowest number of first range
     * @param stop1  Highest number of first range
     * @param start2 Lowest number of second range
     * @param stop2  Highest number of second range
     * @return Returns the re-mapped value as a float.
     */
    public static float map(float value, float start1, float stop1, float start2, float stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    /**
     * Re-maps a number from one range to another.
     *
     * @param value  Number to me re-mapped
     * @param start1 Lowest number of first range
     * @param stop1  Highest number of first range
     * @param start2 Lowest number of second range
     * @param stop2  Highest number of second range
     * @return Returns the re-mapped value as a double.
     */
    public static double map(double value, double start1, double stop1, double start2, double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    /**
     * Returns the distance between two sets of X and Y coordinates.
     *
     * @param x1 First X coordinate
     * @param y1 First Y coordinate
     * @param x2 Second X coordinate
     * @param y2 Second Y coordinate
     * @return Returns the distance as a float.
     */
    public static float dist(float x1, float y1, float x2, float y2) {
        return (float) Math.hypot(x1 - x2, y1 - y2);
    }


    /**
     * Takes an integer value clamps/constrains it between a minimum and maximum.
     *
     * @param value Input to be constrained
     * @param min   Minimum possible value
     * @param max   Maximum possible value
     * @return Constrained value as an int.
     */
    public static int constrain(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Takes a float value clamps/constrains it between a minimum and maximum.
     *
     * @param value Input to be constrained
     * @param min   Minimum possible value
     * @param max   Maximum possible value
     * @return Constrained value as a float.
     */
    public static float constrain(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Takes a double value clamps/constrains it between a minimum and maximum.
     *
     * @param value Input to be constrained
     * @param min   Minimum possible value
     * @param max   Maximum possible value
     * @return Constrained value as a double.
     */
    public static double constrain(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Linearly interpolates between floats by a certain amount.
     *
     * @param start starting value
     * @param end   ending value
     * @param amt   amount to interpolate (0-1)
     * @return returns a float that is the lerp of the two values by the amount.
     */
    public static float lerp(float start, float end, float amt) {
        return (1 - amt) * start + amt * end;
    }

    /**
     * An alternative randomization method. <br>
     * Based on noise generation and hashing techniques - it generates a random value using a position and a seed. <br>
     * - Could also be used as one dimensional value noise. <br>
     * - Guarantees reproducible results for identical inputs. <br>
     * - The seed is less significant than the position. <br>
     *
     * @param pos  the position in the spectrum.
     * @param seed the seed used for fixing the spectrum
     * @return a reproducible randomized number between 0 and 1
     */
    public static float fastRandom(int pos, int seed) {
        pos *= 1610612741;
        pos += seed;
        pos ^= pos >> 8;
        pos += 402653189;
        pos ^= pos << 8;
        pos *= 201326611;
        pos ^= pos >> 1; //killing negative numbers
        return 1f * pos / Integer.MAX_VALUE;
    }

    /**
     * An alternative randomization method. <br>
     * Based on noise generation and hashing techniques - it generates a random value using a position and a seed. <br>
     * - Could also be used as one dimensional value noise. <br>
     * - Guarantees reproducible results for identical inputs. <br>
     * - The seed is less significant than the position. <br>
     *
     * @param pos  the position in the spectrum.
     * @param seed the seed used for fixing the spectrum
     * @return a reproducible randomized number between 0 and 1
     */
    public static double fastRandom(long pos, long seed) {
        pos *= 845120141862461849L;
        pos += seed;
        pos ^= pos >> 8;
        pos += 980103725416435007L;
        pos ^= pos << 8;
        pos *= 618222799641048809L;
        pos ^= pos >> 1; //killing negative numbers
        return 1d * pos / Long.MAX_VALUE;
    }

    /**
     * Flooring a value fast by using casting instead of actual calculation.
     *
     * @param x the value to floor
     * @return the floored integer value
     */
    public static int fastFloor(float x) {
        int xi = (int) x;
        return x < xi ? xi - 1 : xi;
    }

    /**
     * Flooring a value fast by using casting instead of actual calculation.
     *
     * @param x the value to floor
     * @return the floored integer value
     */
    public static int fastFloor(double x) {
        int xi = (int) x;
        return x < xi ? xi - 1 : xi;
    }

    /**
     * Encodes all given input into a short.
     * Note, that the range of Short is 16 bytes and therefore only 0-14 (15 different values) can be encoded.
     * Any value that is not within this range, will lead to undefined behaviour.
     *
     * @param data a list of integers in no particular order
     * @return the encoded data as a short
     */
    public static short encode(int[] data) {
        short allLayers = 0;
        for (int layer : data) {
            allLayers |= 0b100000000000000 >> layer;
        }
        return allLayers;
    }

    /**
     * Encodes a given input into a short.
     * Note, that the range of Short is 16 bytes and therefore only 0-14 (15 different values) can be encoded.
     * Any value that is not within this range, will lead to undefined behaviour.
     *
     * @param data the integer in range to encode
     * @return the encoded data as a short
     */
    public static short encode(int data) {
        return (short) (0b100000000000000 >> data);
    }

    /**
     * Make a dereferenced copy of a vector array
     *
     * @param original the original array
     * @return a array containing a copy of each vector
     */
    public static Vector2f[] copy(Vector2f[] original) {
        Vector2f[] copy = new Vector2f[original.length];
        for (int i = 0; i < copy.length; i++)
            copy[i] = new Vector2f(original[i]);
        return copy;
    }

    /**
     * Shifts a part of the array to overwrite the given region (fromIndex, toIndex)
     * FromIndex is INCLUSIVE, ToIndex is EXCLUSIVE
     *
     * @param array     the array from which the region has to be removed
     * @param fromIndex start of the region
     * @param toIndex   end of the region
     * @return the new effective length
     */
    public static int shiftOverwrite(float[] array, int fromIndex, int toIndex) {
        int length = array.length;
        if (fromIndex > toIndex || length <= fromIndex || length < toIndex) return length;
        System.arraycopy(array, toIndex, array, fromIndex, length - toIndex);
        return length - (toIndex - fromIndex);
    }

}