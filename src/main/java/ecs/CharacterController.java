package ecs;

import input.Keyboard;
import org.joml.Vector2f;
import physics.AABB;
import util.Engine;

public class CharacterController extends Component {

    Vector2f position;
    Vector2f speed;

    float gravity;
    private final boolean grounded;
    protected Vector2f lastPosition;

    float sprintSpeed;

    protected AABB collision;
    public boolean AABB_enabled;

    public CharacterController() {
        position = new Vector2f(0, 0);
        speed = new Vector2f(300, 300);
        gravity = 9;
        grounded = false;
        sprintSpeed = 0;
        AABB_enabled = false;
        this.order = SpriteRenderer.ORDER - 2;
    }

    @Override
    public void start() {
        lastPosition = new Vector2f();
        position = gameObject.getTransform().getPosition();
        super.start();
    }

    @Override
    public void update(float dt) {
        moveX();
        if (collision != null) collision.collideX();

        moveY();
        if (collision != null) collision.collideY();
    }

    public void enableAABB() {
        AABB_enabled = true;
        collision = gameObject.getComponent(AABB.class);
    }

    protected void moveX() {
        // X
        gameObject.setTransformX(position.x);
        if (Keyboard.getKey(Keyboard.A_KEY) || Keyboard.getKey(Keyboard.LEFT_ARROW)) {
            position.x += (-speed.x + sprintSpeed) * Engine.deltaTime();
        }
        if (Keyboard.getKey(Keyboard.D_KEY) || Keyboard.getKey(Keyboard.RIGHT_ARROW)) {
            position.x += (speed.x + sprintSpeed) * Engine.deltaTime();
        }
    }

    protected void moveY() {
        // Y
        gameObject.setTransformY(position.y);

        if (Keyboard.getKey(Keyboard.W_KEY) || Keyboard.getKey(Keyboard.UP_ARROW)) {
            position.y += (-speed.y + sprintSpeed) * Engine.deltaTime();
        }
        if (Keyboard.getKey(Keyboard.S_KEY) || Keyboard.getKey(Keyboard.DOWN_ARROW)) {
            position.y += (speed.y + sprintSpeed) * Engine.deltaTime();
        }
    }

}
