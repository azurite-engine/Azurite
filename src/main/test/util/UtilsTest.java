package util;

<<<<<<< HEAD
import graphics.Camera;
import org.joml.Vector2f;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

public class UtilsTest {

    @Test
    public void worldToScreenCoords() {
        Camera camera = new Camera();
        camera.getProjectionMatrix().identity();
        camera.getProjectionMatrix().ortho(0, 1920, 1020, 0, 0, 100);

        Vector2f v = new Vector2f(1920/2f, 0);

        /*                  right  left   top    bottom                     */
        boolean[] bounds = {false, false, false, false};

        bounds[0] = 1.0f == Utils.worldToScreenCoords(v, camera).x;
        v.x = -1920/2f;
        bounds[1] = -1.0f == Utils.worldToScreenCoords(v, camera).x;
        v.x = 0;

        v.y = 1020/2f;
        bounds[2] = -1.0f == Utils.worldToScreenCoords(v, camera).y;
        v.y = -1020/2f;
        bounds[3] = 1.0f == Utils.worldToScreenCoords(v, camera).y;

        int i = 0;
        for (boolean b : bounds) {
            assertTrue(String.valueOf(i), b);
            i++;
        }
    }
}
=======
import org.joml.Vector2f;
import org.junit.Assert;
import org.junit.Test;
import physics.collision.shape.Circle;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class UtilsTest {

    final float minimalDelta = Float.MIN_VALUE;
    final float smallDelta = 0.0001f;

    @Test
    public void radian() {
        for (int i = 0; i <= 360; i++) {
            Assert.assertEquals(Math.toRadians(i), Utils.radian(i), smallDelta);
        }
    }

    @Test
    public void rotateAroundOrigin() {
        for (int i = 0; i < 100; i++) {
            Vector2f point = new Vector2f((float) Math.random() * 10 - 5, (float) Math.random() * 10 - 5);
            //a circle with the radius as the distance from the origin to the point, where the origin of both is 0,0
            Circle circle = new Circle(new Vector2f(0, 0), point.length());
            circle.setPosition(0, 0);
            Assert.assertTrue(circle.supportPoint(point).equals(point, smallDelta));
            //rotate 360 different angles, should be sufficient
            for (int a = 1; i <= 360; i++) {
                Vector2f p = Utils.rotateAroundOrigin(point, Utils.radian(a));
                Assert.assertTrue(circle.supportPoint(p).equals(p, smallDelta));
            }
        }
    }

    @Test
    public void rotateAroundPoint() {
        for (int i = 0; i < 100; i++) {
            Vector2f pointToRotate = new Vector2f((float) Math.random() * 10 - 5, (float) Math.random() * 10 - 5);
            Vector2f pointToRotateAround = new Vector2f((float) Math.random() * 20 - 10, (float) Math.random() * 20 - 10);
            Vector2f diff = pointToRotate.sub(pointToRotateAround, new Vector2f());
            //a circle with the radius as the distance from the origin to the point, where the origin of both is 0,0
            Circle circle = new Circle(new Vector2f(0, 0), diff.length());
            circle.setPosition(pointToRotateAround);
            Assert.assertTrue(circle.supportPoint(diff).equals(pointToRotate, smallDelta));
            //rotate 360 different angles, should be sufficient
            for (int a = 1; i <= 360; i++) {
                Vector2f p = Utils.rotateAroundPoint(pointToRotate, pointToRotateAround, Utils.radian(a));
                diff = p.sub(pointToRotateAround, new Vector2f());
                Assert.assertTrue(circle.supportPoint(diff).equals(p, smallDelta));
            }
        }
    }

    @Test
    public void encode() {
        Assert.assertEquals(0b110100000000000, Utils.encode(new int[]{0, 1, 3}));
        Assert.assertEquals(0b000100000000000, Utils.encode(3));
        Assert.assertEquals(0b000000000000001, Utils.encode(14));
    }

    @Test
    public void copy() {
        Vector2f[] vectors = new Vector2f[3];
        vectors[0] = new Vector2f(1, 0);
        vectors[1] = new Vector2f(17, -4);
        vectors[2] = new Vector2f(-9, -5);
        Vector2f[] copy = Utils.copy(vectors);
        //not the same, but equals
        Assert.assertArrayEquals(vectors, copy);
        Assert.assertNotSame(vectors, copy);
        Assert.assertNotSame(vectors[0], copy[0]);
        Assert.assertNotSame(vectors[1], copy[1]);
        Assert.assertNotSame(vectors[2], copy[2]);
        Assert.assertEquals(vectors[0], copy[0]);
        Assert.assertEquals(vectors[1], copy[1]);
        Assert.assertEquals(vectors[2], copy[2]);
    }

    @Test
    public void map() {
        Assert.assertEquals(75, Utils.map(0.5f, 0, 1, 50, 100), 0.0001f);
        Assert.assertEquals(0, Utils.map(0.5f, 0, 1, -100, 100), 0.0001f);
    }

    @Test
    public void random() {
        double last = -1;
        for (int i = 0; i < 100; i++) {
            float random = Utils.random(0, 1);
            Assert.assertNotEquals(last, random, minimalDelta);
        }

        int lastInt = -1;
        //THIS TEST MIGHT FAIL TO RANDOMNESS - TRY AGAIN IN THIS CASE
        for (int i = 0; i < 100; i++) {
            int rand = Utils.randomInt(0, Integer.MAX_VALUE);
            Assert.assertNotEquals(lastInt, rand);
        }
    }

    @Test
    public void round() {
        for (int i = 0; i < 100; i++) {
            float random = Utils.random(0, 100);
            Assert.assertEquals(Math.round(random), Utils.round(random), minimalDelta);
        }
    }

    @Test
    public void dist() {
        for (int i = 0; i < 100; i++) {
            Vector2f a = new Vector2f(Utils.random(-100, 100), Utils.random(-100, 100));
            Vector2f b = new Vector2f(Utils.random(-100, 100), Utils.random(-100, 100));
            Assert.assertEquals(a.distance(b), Utils.dist(a, b), smallDelta);
            Assert.assertEquals(a.distance(b), Utils.dist(a.x, a.y, b.x, b.y), smallDelta);
        }
    }

    @Test
    public void constrain() {
        for (int i = 0; i < 10; i++) {
            int constrained = Utils.constrain(i, 3, 6);
            float constrainedF = Utils.constrain(i, 3.0f, 6.0f);
            if (i <= 3) {
                Assert.assertEquals(3, constrained);
                Assert.assertEquals(3.0f, constrainedF, minimalDelta);
            } else if (i < 6) {
                Assert.assertEquals(i, constrained);
                Assert.assertEquals(i, constrainedF, minimalDelta);
            } else {
                Assert.assertEquals(6, constrained);
                Assert.assertEquals(6.0f, constrainedF, minimalDelta);
            }
        }
    }

    @Test
    public void lerp() {
        Assert.assertEquals(0.5f, Utils.lerp(0, 1, 0.5f), minimalDelta);
    }

    @Test
    public void shiftOverwrite() {
        //error checks
        float[] arr1 = new float[]{1, 2, 3, 4, 5};
        float[] arr1Copy = new float[]{1, 2, 3, 4, 5};
        //without indices 1 and 2
        float[] reduced = new float[]{1, 4, 5, 4, 5};

        //case: given indices doesnt match size
        int newL = Utils.shiftOverwrite(arr1, 0, 6);
        Assert.assertEquals(arr1Copy.length, newL);
        Assert.assertArrayEquals(arr1Copy, arr1, Float.MIN_VALUE);

        //case: given indices doesnt match size
        int newL2 = Utils.shiftOverwrite(arr1, 5, 5);
        Assert.assertEquals(arr1Copy.length, newL2);
        Assert.assertArrayEquals(arr1Copy, arr1, Float.MIN_VALUE);

        //case: given to < from
        int newL3 = Utils.shiftOverwrite(arr1, 3, 2);
        Assert.assertEquals(arr1Copy.length, newL3);
        Assert.assertArrayEquals(arr1Copy, arr1, Float.MIN_VALUE);

        //remove from first and keep last 2
        int newL4 = Utils.shiftOverwrite(arr1, 1, arr1.length - 2);
        Assert.assertEquals(3, newL4);
        Assert.assertArrayEquals(reduced, arr1, 0);

    }

}
>>>>>>> 43c35baf2f4edb22cdcedb78c37d8caa77366450
