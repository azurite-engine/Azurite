package ecs;

import physics.collision.Collider;
import physics.collision.CollisionInformation;

/**
 * A component to handle and react to collisions using a collider.
 * Can be attached to an object if it should react to collisions in any way.
 *
 * @author Juyas
 * @version 07.12.2021
 * @see CollisionHandlers
 * @since 07.12.2021
 */
public abstract class CollisionHandler extends Component {

    private final Collider collider;

    public CollisionHandler(Collider collider) {
        super(ComponentOrder.COLLISION);
        if (collider.passive()) throw new IllegalArgumentException("passive colliders dont detect collision");
        this.collider = collider;
    }

    @Override
    public void start() {
    }

    private void checkCollision(Collider other) {
        //test layer/mask
        if (collider.canCollideWith(other)) {
            //test boundingsphere for quickly detecting no collision
            if (!collider.getShape().boundingSphere().intersection(other.getShape().boundingSphere())) return;
            //do actual collision detection
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

    /**
     * Will be called in the update loop if a collision happens {@link CollisionInformation#collision()}
     * with the given environment collider and the defined one of this handler {@link this#getCollider()}.
     *
     * @param environment          the other collider in the environment
     * @param collisionInformation the data about the collision
     */
    abstract public void collision(Collider environment, CollisionInformation collisionInformation);

}