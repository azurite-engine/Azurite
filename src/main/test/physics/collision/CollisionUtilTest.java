package physics.collision;

import org.joml.Random;
import org.joml.Vector2f;
import org.junit.Assert;
import org.junit.Test;
import util.Transform;
import util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class CollisionUtilTest {

    @Test
    public void gjksmCollision() {
        //TODO hard to test
    }

    @Test
    public void expandingPolytopeAlgorithm() {
        //TODO hard to test
    }

    @Test
    public void maxDotPointMinkDiff() {
        //TODO hard to test
    }

    @Test
    public void maxDotPoint() {
        //TODO hard to test
    }

    @Test
    public void solveSimultaneousEquations() {
        //TODO hard to test
    }

    @Test
    public void rayCastIntersectionPoint() {
        //TODO hard to test
    }

    @Test
    public void rayCastIntersection() {
        //TODO hard to test
    }

    @Test
    public void rayCastToLineIntersectionPoint() {
        //TODO hard to test
    }

    @Test
    public void centroidAndSphere() {
        List<Vector2f> points = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            //chance to have less points
            if (Math.random() > 0.3) {
                points.add(new Vector2f(Utils.random(-100, 100), Utils.random(-100, 100)));
            }
        }
        Vector2f[] convexHull = CollisionUtil.convexHull(points.toArray(new Vector2f[0]));
        //no real checks for convexHull
        Assert.assertTrue(convexHull.length <= points.size());
        Vector2f centroid = CollisionUtil.polygonCentroid(convexHull);
        float radius = CollisionUtil.boundingSphere(centroid, convexHull);
        //checks for boundingSphere
        for (Vector2f point : convexHull) {
            Assert.assertTrue(radius >= centroid.distance(point));
        }
    }

    @Test
    public void inCircle() {
        Random random = new Random(123456789);
        for (int i = 0; i < 100; i++) {
            float x1 = random.nextFloat() * 100 - 50;
            float y1 = random.nextFloat() * 100 - 50;
            float px1 = random.nextFloat() * 100 - 50;
            float py1 = random.nextFloat() * 100 - 50;
            float r = random.nextFloat() * 10;
            Vector2f p = new Vector2f(px1, py1);
            Vector2f c = new Vector2f(x1, y1);
            boolean inCircle = c.distanceSquared(p) <= r * r;
            Assert.assertEquals(inCircle, CollisionUtil.inCircle(p, c, r));
            Assert.assertEquals(inCircle, CollisionUtil.inCircle(p, x1, y1, r));
            Assert.assertEquals(inCircle, CollisionUtil.inCircle(px1, py1, x1, y1, r));
        }
    }

    @Test
    public void inRect() {
        Vector2f in = new Vector2f(4, 4);
        Assert.assertTrue(CollisionUtil.inRect(in, 0, 0, 10, 10));
        Assert.assertFalse(CollisionUtil.inRect(in, 0, 5, 10, 10));
        Assert.assertTrue(CollisionUtil.inRect(in.x, in.y, 0, 0, 10, 10));
        Assert.assertFalse(CollisionUtil.inRect(in.x, in.y, 0, 5, 10, 10));
    }

    @Test
    public void rectInRect() {
        Transform transform = new Transform(0, 0, 10, 10);
        Transform inside = new Transform(1, 1, 7, 9);
        Transform notInside = new Transform(1, 1, 7, 19);
        Assert.assertTrue(CollisionUtil.rectInRect(inside, transform));
        Assert.assertFalse(CollisionUtil.rectInRect(notInside, transform));
    }

}