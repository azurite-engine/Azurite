package ecs;

import org.joml.Vector2f;
import physics.collision.Collider;
import physics.collision.CollisionInformation;
import util.MathUtils;

import java.util.Optional;

/**
 * @author Juyas
 * @version 07.12.2021
 * @since 07.12.2021
 */
public class CollisionHandlers {

    /**
     * A {@link CollisionHandler} for a {@link PolygonCollider} that should not pass through other colliders.
     * Can be thought of as making the collider a solid object.
     *
     * @param collider the collider to attach the handler to
     * @return the {@link CollisionHandler} to be attached to the gameObject holding the collider
     */
    public static CollisionHandler unpassablePolygonCollider(PolygonCollider collider) {
        return new CollisionHandler(collider) {
            @Override
            public void collision(Collider environment, CollisionInformation collisionInformation) {
                //calculate basic repulse vector
                Optional<Vector2f> optional = MathUtils.expandingPolytopeAlgorithm(collider.getShape(), environment.getShape(), (Vector2f[]) collisionInformation.get());
                //well, I cannot react... I guess
                if (!optional.isPresent()) return;
                Vector2f repulse = optional.get();
                collider.setPosition(collider.position().add(repulse));
            }
        };
    }

}