package ecs;

import physics.collision.Collider;
import physics.collision.CollisionInformation;

/**
 * @author Juyas
 * @version 07.12.2021
 * @since 07.12.2021
 */
public abstract class CollisionHandler extends Component {

    private final Collider collider;

    public CollisionHandler(Collider collider) {
        if(collider.passive()) throw new IllegalArgumentException("passive colliders dont detect collision");
        this.collider = collider;
    }

    @Override
    public void start() {
    }

    private void checkCollision(Collider other) {
        if (collider.canCollideWith(other)) {
            CollisionInformation ci = collider.detectCollision(other);
            if (ci.collision())
                collision(other, ci);
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        gameObject.getParentScene().getColliders()
                .stream()
                .forEach(this::checkCollision);
    }

    protected Collider getCollider() {
        return collider;
    }

    abstract public void collision(Collider environment, CollisionInformation collisionInformation);

}