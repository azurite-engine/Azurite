package physics.collision;

import ecs.RigidBody;
import org.joml.Vector2f;
import physics.force.DirectionalVectorFilter;
import scenes.DemoPlatformer;
import util.Tuple;

import java.util.Optional;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 24.06.2021
 * @since 24.06.2021
 */
public class Collisions {

    public static final int COLLISION_FILTER = 601112109;

    public static CollisionHandler solidBouncy(float velocityFactor) {
        return new CollisionHandler() {
            @Override
            public void accept(RigidBody collider, Tuple<Vector2f> simplex) {
                Optional<Vector2f> epa = CollisionUtil.expandingPolytopeAlgorithm(collider.getCollisionShape(), parentComponent.getCollisionShape(), simplex);
                Vector2f reflection = epa.get();
                //TODO remove this, this is debug
                DemoPlatformer.last = reflection;
                collider.positionBuffer().add(reflection);
                collider.velocity().add(reflection.mul(velocityFactor, new Vector2f()));
                addFilter(collider, reflection);
            }
        };
    }

    public static CollisionHandler solid() {
        return new CollisionHandler() {
            @Override
            public void accept(RigidBody collider, Tuple<Vector2f> simplex) {
                Optional<Vector2f> epa = CollisionUtil.expandingPolytopeAlgorithm(collider.getCollisionShape(), parentComponent.getCollisionShape(), simplex);
                Vector2f reflection = epa.get();
                //TODO remove this, this is debug
                DemoPlatformer.last = reflection;
                collider.positionBuffer().add(reflection);
                addFilter(collider, reflection);
            }
        };
    }

    private static void addFilter(RigidBody body, Vector2f reflection) {
        body.addFilter(new DirectionalVectorFilter(reflection.mul(-1, new Vector2f()), COLLISION_FILTER));
    }

}