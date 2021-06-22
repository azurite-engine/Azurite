package physics;

import ecs.CharacterController;
import org.joml.Vector2f;
import util.Engine;

@Deprecated
public class Gravity_old extends CharacterController {
    private float gravity = 9f;
    Vector2f velocity = new Vector2f(0, 100);
    private boolean grounded = false;

    AABB aabb;

    @Override
    public void start() {
        super.start();
        aabb = gameObject.getComponent(AABB.class);
    }

    @Override
    public void update (float dt) {
        if (collision != null) collision.collideX();

        moveY();
        if (collision != null) collision.collideY();

        if (aabb != null) {
            if (aabb.isCollidingY()) {
                grounded = true;
            } else {
                grounded = false;
            }
        } else {
            aabb = gameObject.getComponent(AABB.class);
        }
    }

    protected void moveY () {
        lastPosition.y = gameObject.getTransform().getY();

        if (!grounded) {
            velocity.y += gravity;
        }
        gameObject.getTransform().addY(velocity.y * Engine.deltaTime());
    }

    public void addVelocityY (float y) {
        velocity.y += y;
    }
}
