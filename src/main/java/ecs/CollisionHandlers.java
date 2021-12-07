package ecs;

import org.joml.Vector2f;
import physics.collision.Collider;
import physics.collision.CollisionInformation;
import physics.collision.CollisionUtil;

import java.util.Optional;

/**
 * @author Juyas
 * @version 07.12.2021
 * @since 07.12.2021
 */
public class CollisionHandlers {

    public static CollisionHandler unpassablePolygonCollider(PolygonCollider collider) {
        return new CollisionHandler(collider) {
            @Override
            public void collision(Collider environment, CollisionInformation collisionInformation) {
                //calculate basic repulse vector
                Optional<Vector2f> optional = CollisionUtil.expandingPolytopeAlgorithm(collider.getShape(), environment.getShape(), (Vector2f[]) collisionInformation.get());
                //well, I cannot react... I guess
                if(!optional.isPresent()) return;
                Vector2f repulse = optional.get();
                collider.setPosition(collider.position().add(repulse));
            }
        };
    }

}