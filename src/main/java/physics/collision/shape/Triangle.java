package physics.collision.shape;

import org.joml.Vector2f;
import physics.collision.CollisionUtil;
import util.Pair;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Triangle extends PrimitiveShape {

    public Triangle(Vector2f relativeA, Vector2f relativeB, Vector2f relativeC) {
        super(relativeA, relativeB, relativeC);
        initSphere();
        init();
    }

    @Override
    public Vector2f reflect(Vector2f centroid, Vector2f collisionRay) {
        Pair<Vector2f, Vector2f> normals = CollisionUtil.collisionEdgeNormals(this.getAbsolutePoints(), this.absoluteCentroid, centroid);
        if (normals.getLeft().dot(collisionRay) >= 0)
            return CollisionUtil.planeReflection(normals.getLeft(), collisionRay);
        else return CollisionUtil.planeReflection(normals.getRight(), collisionRay);
    }

    @Override
    public Shape shape() {
        return Shape.TRIANGLE;
    }

}