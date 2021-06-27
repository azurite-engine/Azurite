package physics.collision.shape;

import org.joml.Vector2f;
import physics.collision.CollisionUtil;
import util.Pair;
import util.Utils;

/**
 * <h1>Azurite</h1>
 * <p>
 * A basic polygon shape.
 * It does not ensure the points to be part of the convex hull, so this object may contain points for a concave shape.
 * If that is the case, this shape won't work correctly for GJKSM collision detection.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class BasicPolygon extends PrimitiveShape {


    public BasicPolygon(Vector2f... relatives) {
        super(Utils.copy(relatives));
        initSphere();
        init();
    }

    @Override
    public Vector2f reflect(Vector2f centroid, Vector2f collisionRay) {
        Pair<Vector2f, Vector2f> normals = CollisionUtil.collisionEdgeNormals(this.absolutes, this.absoluteCentroid, centroid);
        if (normals.getLeft().dot(collisionRay) >= 0)
            return CollisionUtil.planeReflection(normals.getLeft(), collisionRay);
        else return CollisionUtil.planeReflection(normals.getRight(), collisionRay);
    }

    @Override
    public Shape shape() {
        return Shape.POLYGON;
    }

}