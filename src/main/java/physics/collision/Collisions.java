package physics.collision;

import ecs.RigidBody;
import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 24.06.2021
 * @since 24.06.2021
 */
public class Collisions {

    public static CollisionHandler solidBouncy(float velocityFactor) {
        return new CollisionHandler() {
            @Override
            public void accept(RigidBody collider) {
                Vector2f reflection = parentComponent.getCollisionShape().reflect(collider.getCollisionShape().centroid(), collider.velocity());
                collider.velocity().add(reflection.mul(velocityFactor));
            }
        };
    }

    public static CollisionHandler solid() {
        return solidBouncy(1);
    }

}