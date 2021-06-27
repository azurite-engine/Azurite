package physics.collision.shape;

import org.joml.Vector2f;
import physics.collision.CollisionUtil;

/**
 * <h1>Azurite</h1>
 * <p>
 * The GJKSM shape implementation of a line.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Line extends PrimitiveShape {

    public Line(Vector2f relativeA, Vector2f relativeB) {
        super(relativeA, relativeB);
        initSphere();
        init();
    }

    @Override
    public Vector2f reflect(Vector2f centroid, Vector2f collisionRay) {
        if (faces[0].getNormal().dot(collisionRay) >= 0)
            return CollisionUtil.planeReflection(faces[0].getNormal(), collisionRay);
        else return CollisionUtil.planeReflection(faces[1].getNormal(), collisionRay);
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return absolutes[0].dot(v) > absolutes[1].dot(v) ? absolutes[0] : absolutes[1];
    }

    @Override
    public Shape shape() {
        return Shape.LINE;
    }
}