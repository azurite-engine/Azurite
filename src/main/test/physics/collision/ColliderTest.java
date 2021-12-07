package physics.collision;

import ecs.PolygonCollider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import physics.collision.shape.ShapeType;

/**
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class ColliderTest {

    PolygonCollider rigidBody;
    PolygonCollider staticCollider;

    @Before
    public void setUp() throws Exception {
        rigidBody = new PolygonCollider(Shapes.axisAlignedRectangle(0, 0, 100, 100));
        rigidBody.setLayer(0, true);
        rigidBody.setMask(5, true);
        staticCollider = new PolygonCollider(Shapes.axisAlignedRectangle(0, 0, 10, 10));
        staticCollider.setLayer(5, true);
    }

    @Test
    public void getCollisionShape() {
        Assert.assertEquals(ShapeType.QUADRILATERAL, rigidBody.getShape().type());
        Assert.assertEquals(ShapeType.QUADRILATERAL, staticCollider.getShape().type());
    }

    @Test
    public void canCollideWith() {
        Assert.assertTrue(rigidBody.canCollideWith(staticCollider));
        Assert.assertTrue(staticCollider.canCollideWith(rigidBody));
    }

    @Test
    public void layers() {
        Assert.assertEquals(0b100000000000000, rigidBody.layers());
        Assert.assertEquals(0b000001000000000, staticCollider.layers());
    }

    @Test
    public void mask() {
        Assert.assertEquals(0b000001000000000, rigidBody.mask());
        Assert.assertEquals(0b000000000000000, staticCollider.mask());
    }

    @Test
    public void hasMask() {
        for (int i = 0; i < 14; i++) {
            Assert.assertEquals(i == 5, rigidBody.hasMask(i));
            Assert.assertFalse(staticCollider.hasMask(i));
        }
    }

    @Test
    public void isOnLayer() {
        for (int i = 0; i < 14; i++) {
            Assert.assertEquals(i == 0, rigidBody.isOnLayer(i));
            Assert.assertEquals(i == 5, staticCollider.isOnLayer(i));
        }
    }

    @Test
    public void setLayer() {
        Assert.assertFalse(rigidBody.isOnLayer(6));
        rigidBody.setLayer(6, true);
        Assert.assertTrue(rigidBody.isOnLayer(6));
        rigidBody.setLayer(6, false);
        Assert.assertFalse(rigidBody.isOnLayer(6));

        Assert.assertFalse(staticCollider.isOnLayer(6));
        staticCollider.setLayer(6, true);
        Assert.assertTrue(staticCollider.isOnLayer(6));
        staticCollider.setLayer(6, false);
        Assert.assertFalse(staticCollider.isOnLayer(6));
    }

    @Test
    public void setMask() {
        Assert.assertFalse(rigidBody.hasMask(6));
        rigidBody.setMask(6, true);
        Assert.assertTrue(rigidBody.hasMask(6));
        rigidBody.setMask(6, false);
        Assert.assertFalse(rigidBody.hasMask(6));

        Assert.assertFalse(staticCollider.hasMask(6));
        staticCollider.setMask(6, true);
        Assert.assertTrue(staticCollider.hasMask(6));
        staticCollider.setMask(6, false);
        Assert.assertFalse(staticCollider.hasMask(6));
    }

}