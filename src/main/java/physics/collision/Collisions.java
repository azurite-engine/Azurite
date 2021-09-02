package physics.collision;

import ecs.RigidBody;
import org.joml.Vector2f;
import physics.collision.shape.PrimitiveShape;
import physics.force.DirectionalVectorFilter;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * <h1>Azurite</h1>
 * A util class for specific {@link CollisionHandler}'s
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 24.06.2021
 */
public class Collisions {

    /**
     * The ID for {@link physics.force.VectorFilter} if the collision applies a filter to prevent intersection after collision
     */
    public static final int COLLISION_FILTER = 601112109;

    /**
     * Standard collision, just prevents intersection and moves object according to EPA to seperate them.
     */
    public static CollisionHandler gjksmBlocking() {
        return new CollisionHandler() {
            @Override
            public void accept(RigidBody collider, CollisionInformation info) {
                if (!info.collision()) return;
                //find penetration vector with EPA
                Optional<Vector2f> epa = CollisionUtil.expandingPolytopeAlgorithm(collider.getCollisionShape(), parentComponent.getCollisionShape(), (Vector2f[]) info.get());

                //if two rigitbodies collide, the first one will handle the collision
                if (!epa.isPresent()) return;

                Vector2f reflection = epa.get();
                //solid intersection prevention
                collider.positionBuffer().add(reflection);
                //prevents movement in the direction of the collision
                collider.addFilter(new DirectionalVectorFilter(reflection.mul(-1, new Vector2f()), COLLISION_FILTER));
            }
        };
    }

    public static BiFunction<PrimitiveShape, PrimitiveShape, CollisionInformation> gjksmCollisionDetection() {
        return CollisionUtil::gjksmCollision;
    }

}