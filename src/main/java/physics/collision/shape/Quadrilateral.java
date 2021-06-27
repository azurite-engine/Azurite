package physics.collision.shape;

import org.joml.Vector2f;
import physics.collision.CollisionUtil;
import util.Pair;

/**
 * <h1>Azurite</h1>
 * <p>
 * The GJKSM shape implementation of a rectangle.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Quadrilateral extends PrimitiveShape {

    public Quadrilateral(Vector2f a, Vector2f b, Vector2f c, Vector2f d) {
        super(new Vector2f(a), new Vector2f(b), new Vector2f(c), new Vector2f(d));
        initSphere();
        init();
    }

    @Override
    public Vector2f reflect(Vector2f centroid, Vector2f collisionRay) {
        Pair<Vector2f, Vector2f> normals = CollisionUtil.collisionEdgeNormals(this.absolutes, this.absoluteCentroid, centroid);
        if (normals.getLeft().dot(collisionRay) >= 0) {
            System.out.println("chose: " + normals.getLeft() + " for " + collisionRay);
            return CollisionUtil.planeReflection(normals.getLeft(), collisionRay);
        } else {
            System.out.println("chose: " + normals.getRight() + " for " + collisionRay);
            return CollisionUtil.planeReflection(normals.getRight(), collisionRay);
        }
    }

    @Override
    public Shape shape() {
        return Shape.QUADRILATERAL;
    }
}