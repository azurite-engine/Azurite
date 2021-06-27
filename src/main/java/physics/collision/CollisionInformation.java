package physics.collision;

import org.joml.Vector2f;
import physics.collision.shape.PrimitiveShape;

import java.util.Optional;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 27.06.2021
 * @since 27.06.2021
 */
public class CollisionInformation {

    private final PrimitiveShape a;
    private final PrimitiveShape b;
    private final Optional<Vector2f> estimatedDirection;

    private Vector2f collisionPoint;
    private Vector2f collisionDiagonal;
    private Vector2f collisionDepth;

    public CollisionInformation(PrimitiveShape a, PrimitiveShape b) {
        this(a, b, null);
    }

    public CollisionInformation(PrimitiveShape a, PrimitiveShape b, Vector2f estimatedDirection) {
        this.a = a;
        this.b = b;
        this.estimatedDirection = Optional.ofNullable(estimatedDirection);
    }

    public void setCollisionDepth(Vector2f collisionDepth) {
        this.collisionDepth = collisionDepth;
    }

    public void setCollisionDiagonal(Vector2f collisionDiagonal) {
        this.collisionDiagonal = collisionDiagonal;
    }

    public void setCollisionPoint(Vector2f collisionPoint) {
        this.collisionPoint = collisionPoint;
    }

    public Optional<Vector2f> getEstimatedDirection() {
        return estimatedDirection;
    }

    public PrimitiveShape getA() {
        return a;
    }

    public PrimitiveShape getB() {
        return b;
    }

    public Vector2f getCollisionDepth() {
        return collisionDepth;
    }

    public Vector2f getCollisionDiagonal() {
        return collisionDiagonal;
    }

    public Vector2f getCollisionPoint() {
        return collisionPoint;
    }

    @Override
    public String toString() {
        return "CollisionInformation{" +
                "a=" + a +
                ", b=" + b +
                ", collisionPoint=" + collisionPoint +
                ", collisionDiagonal=" + collisionDiagonal +
                ", collisionDepth=" + collisionDepth +
                '}';
    }
}