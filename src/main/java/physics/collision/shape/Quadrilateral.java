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

//    public void testCollisionDiagonal(Rectangle other) {
//        for (Vector2f diagonal : diagonals) {
//            for (int j = 0; j < other.faces.length; j++) {
//                Vector2f face = other.faces[j];
//                Pair<Vector2f, Vector2f> intersect = CollisionUtil.rayCastIntersection(absoluteCentroid, diagonal, other.absolutes[j], face);
//                if (intersect != null) {
//                    System.out.println("on others face: " + intersect.getLeft() + " on " + face + " from " + other.absolutes[j]);
//                    System.out.println("depth: " + diagonal.length());
//                    System.out.println("distance " + absoluteCentroid.distance(intersect.getLeft()));
//                    System.out.println("fac: " + intersect.getRight());
//                    float v = diagonal.length() * intersect.getRight().x;
//                    System.out.println("res: " + Math.max(v, diagonal.length() - v));
//                }
//            }
//        }
//        for (Vector2f diagonal : other.diagonals) {
//            for (int j = 0, facesLength = faces.length; j < facesLength; j++) {
//                Vector2f face = faces[j];
//                Pair<Vector2f, Vector2f> intersect = CollisionUtil.rayCastIntersection(other.absoluteCentroid, diagonal, absolutes[j], face);
//                if (intersect != null) {
//                    System.out.println("on this face: " + intersect.getLeft() + " on " + face + " from " + absolutes[j]);
//                    System.out.println("depth: " + diagonal.length());
//                    System.out.println("distance " + other.absoluteCentroid.distance(intersect.getLeft()));
//                    System.out.println("fac: " + intersect.getRight());
//                    float v = diagonal.length() * intersect.getRight().x;
//                    System.out.println("res: " + Math.max(v, diagonal.length() - v));
//                }
//            }
//        }
//    }

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