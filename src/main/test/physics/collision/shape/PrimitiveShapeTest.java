package physics.collision.shape;

import org.joml.Vector2f;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.MathUtils;

/**
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class PrimitiveShapeTest {

    final float deltaAcceptance = 0.001f;
    PrimitiveShape shape1;
    PrimitiveShape shape2;
    PrimitiveShape shape3;

    @Before
    public void setUp() {
        //a square
        shape1 = new Quadrilateral(new Vector2f(0, 0), new Vector2f(2, 0), new Vector2f(2, 2), new Vector2f(0, 2));
        //a triangle with equal sides
        shape2 = new Triangle(new Vector2f(-3, 0), new Vector2f(0, 3), new Vector2f(3, 0));
        //a polygon with 5 edges
        shape3 = new ConvexPolygon(new Vector2f(-3, 2), new Vector2f(0, 0), new Vector2f(5, 2), new Vector2f(3, 6), new Vector2f(-1, 5));
        shape1.setPosition(0, 0);
        shape2.setPosition(0, 0);
        shape3.setPosition(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rotationError() {
        //if rotation around point is chosen, there has to be a point
        shape1.rotateShape(MathUtils.radian(90), RotationType.AROUND_POINT, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rotationError2() {
        //the type of rotation has to be specified
        shape1.rotateShape(MathUtils.radian(90), null, null);
    }

    @Test
    public void rotation() {

        for (int i = 1; i < 360; i++) {
            Vector2f[] absolutes = shape1.absolutes;
            shape1.rotateShape(MathUtils.radian(i), RotationType.AROUND_CENTER, null);
            shape1.rotateShape(MathUtils.radian(-i), RotationType.AROUND_CENTER, null);
            Assert.assertArrayEquals(absolutes, shape1.absolutes);

            absolutes = shape2.absolutes;
            shape2.rotateShape(MathUtils.radian(i), RotationType.AROUND_CENTER, null);
            shape2.rotateShape(MathUtils.radian(-i), RotationType.AROUND_CENTER, null);
            Assert.assertArrayEquals(absolutes, shape2.absolutes);

            absolutes = shape3.absolutes;
            shape3.rotateShape(MathUtils.radian(i), RotationType.AROUND_CENTER, null);
            shape3.rotateShape(MathUtils.radian(-i), RotationType.AROUND_CENTER, null);
            Assert.assertArrayEquals(absolutes, shape3.absolutes);
        }

        for (int i = 1; i < 360; i++) {
            Vector2f[] absolutes = shape1.absolutes;
            shape1.rotateShape(MathUtils.radian(i), RotationType.AROUND_ORIGIN, null);
            shape1.rotateShape(MathUtils.radian(-i), RotationType.AROUND_ORIGIN, null);
            Assert.assertArrayEquals(absolutes, shape1.absolutes);

            absolutes = shape2.absolutes;
            shape2.rotateShape(MathUtils.radian(i), RotationType.AROUND_ORIGIN, null);
            shape2.rotateShape(MathUtils.radian(-i), RotationType.AROUND_ORIGIN, null);
            Assert.assertArrayEquals(absolutes, shape2.absolutes);

            absolutes = shape3.absolutes;
            shape3.rotateShape(MathUtils.radian(i), RotationType.AROUND_ORIGIN, null);
            shape3.rotateShape(MathUtils.radian(-i), RotationType.AROUND_ORIGIN, null);
            Assert.assertArrayEquals(absolutes, shape3.absolutes);
        }

        //any artificial point
        Vector2f point = new Vector2f(8, 8);

        for (int i = 1; i < 360; i++) {
            Vector2f[] absolutes = shape1.absolutes;
            shape1.rotateShape(MathUtils.radian(i), RotationType.AROUND_POINT, point);
            shape1.rotateShape(MathUtils.radian(-i), RotationType.AROUND_POINT, point);
            Assert.assertArrayEquals(absolutes, shape1.absolutes);

            absolutes = shape2.absolutes;
            shape2.rotateShape(MathUtils.radian(i), RotationType.AROUND_POINT, point);
            shape2.rotateShape(MathUtils.radian(-i), RotationType.AROUND_POINT, point);
            Assert.assertArrayEquals(absolutes, shape2.absolutes);

            absolutes = shape3.absolutes;
            shape3.rotateShape(MathUtils.radian(i), RotationType.AROUND_POINT, point);
            shape3.rotateShape(MathUtils.radian(-i), RotationType.AROUND_POINT, point);
            Assert.assertArrayEquals(absolutes, shape3.absolutes);
        }

    }

    @Test
    public void setPosition() {
        Vector2f pos = new Vector2f(5, -2);
        shape1.setPosition(pos);
        shape2.setPosition(pos);
        shape3.setPosition(pos);
        for (int i = 0; i < shape1.vertices(); i++) {
            Assert.assertEquals(shape1.relatives[i].add(pos, new Vector2f()), shape1.getAbsolutePoints()[i]);
        }
        for (int i = 0; i < shape2.vertices(); i++) {
            Assert.assertEquals(shape2.relatives[i].add(pos, new Vector2f()), shape2.getAbsolutePoints()[i]);
        }
        for (int i = 0; i < shape3.vertices(); i++) {
            Assert.assertEquals(shape3.relatives[i].add(pos, new Vector2f()), shape3.getAbsolutePoints()[i]);
        }
        shape1.setPosition(0, 0);
        shape2.setPosition(0, 0);
        shape3.setPosition(0, 0);
    }

    @Test
    public void position() {
        Assert.assertEquals(new Vector2f(0, 0), shape1.position());
        Assert.assertEquals(new Vector2f(0, 0), shape2.position());
        Assert.assertEquals(new Vector2f(0, 0), shape3.position());
    }

    @Test
    public void faces() {
        Assert.assertEquals(4, shape1.faces().length);
        Assert.assertEquals(3, shape2.faces().length);
        Assert.assertEquals(5, shape3.faces().length);
    }

    @Test
    public void vertices() {
        Assert.assertEquals(4, shape1.vertices());
        Assert.assertEquals(3, shape2.vertices());
        Assert.assertEquals(5, shape3.vertices());
    }

    @Test
    public void centroid() {
        Vector2f centroid1 = shape1.centroid();
        Vector2f centroid2 = shape2.centroid();
        Vector2f centroid3 = shape3.centroid();
        Assert.assertEquals(1, centroid1.x, deltaAcceptance);
        Assert.assertEquals(1, centroid1.y, deltaAcceptance);
        Assert.assertEquals(0, centroid2.x, deltaAcceptance);
        Assert.assertEquals(1, centroid2.y, deltaAcceptance);
        Assert.assertEquals(1.046f, centroid3.x, deltaAcceptance);
        Assert.assertEquals(2.954f, centroid3.y, deltaAcceptance);
    }

    @Test
    public void boundingSphere() {
        Circle circle1 = shape1.boundingSphere();
        Circle circle2 = shape2.boundingSphere();
        Circle circle3 = shape3.boundingSphere();

        Vector2f centroid1 = shape1.centroid();
        Vector2f max1 = null;
        for (Vector2f v : shape1.getAbsolutePoints()) {
            if (max1 == null || max1.distanceSquared(centroid1) < v.distanceSquared(centroid1))
                max1 = v;
        }

        Vector2f centroid2 = shape2.centroid();
        Vector2f max2 = null;
        for (Vector2f v : shape2.getAbsolutePoints()) {
            if (max2 == null || max2.distanceSquared(centroid2) < v.distanceSquared(centroid2))
                max2 = v;
        }

        Vector2f centroid3 = shape3.centroid();
        Vector2f max3 = null;
        for (Vector2f v : shape3.getAbsolutePoints()) {
            if (max3 == null || max3.distanceSquared(centroid3) < v.distanceSquared(centroid3))
                max3 = v;
        }

        //TODO insert circle radius checks here

        Assert.assertEquals(circle1.centroid(), centroid1);
        Assert.assertEquals(circle2.centroid(), centroid2);
        Assert.assertEquals(circle3.centroid(), centroid3);
    }

    @Test
    public void supportPoint() {
        Assert.assertEquals(new Vector2f(0, 0), shape1.supportPoint(new Vector2f(-1, -1)));
        Assert.assertEquals(new Vector2f(2, 2), shape1.supportPoint(new Vector2f(1, 1)));
        Assert.assertEquals(new Vector2f(2, 0), shape1.supportPoint(new Vector2f(1, 0)));
        Assert.assertEquals(new Vector2f(0, 2), shape1.supportPoint(new Vector2f(-1, 1)));

        Assert.assertEquals(new Vector2f(-3, 0), shape2.supportPoint(new Vector2f(-1, 0)));
        Assert.assertEquals(new Vector2f(0, 3), shape2.supportPoint(new Vector2f(0, 1)));
        Assert.assertEquals(new Vector2f(3, 0), shape2.supportPoint(new Vector2f(1, 0)));

        Assert.assertEquals(new Vector2f(-3, 2), shape3.supportPoint(new Vector2f(-1, 0)));
        Assert.assertEquals(new Vector2f(0, 0), shape3.supportPoint(new Vector2f(0, -1)));
        Assert.assertEquals(new Vector2f(5, 2), shape3.supportPoint(new Vector2f(1, 0)));
        Assert.assertEquals(new Vector2f(3, 6), shape3.supportPoint(new Vector2f(0, 1)));
        Assert.assertEquals(new Vector2f(-1, 5), shape3.supportPoint(new Vector2f(-1, 1)));
    }

    @Test
    public void type() {
        Assert.assertEquals(ShapeType.QUADRILATERAL, shape1.type());
        Assert.assertEquals(ShapeType.TRIANGLE, shape2.type());
        Assert.assertEquals(ShapeType.POLYGON, shape3.type());
    }

}