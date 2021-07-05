package ecs;

import input.Keyboard;
import org.joml.Vector2f;

import java.util.Arrays;


public class CharacterController extends Component {

    private float movementSpeed;
    private Vector2f currentDirection;
    private final boolean[] keys = new boolean[3];
    private RigidBody body;

    public CharacterController() {
        movementSpeed = 0.5f;
        this.order = SpriteRenderer.ORDER - 5;
        this.currentDirection = new Vector2f();
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    @Override
    public void start() {
        super.start();
        this.body = gameObject.getComponent(RigidBody.class);
    }

    @Override
    public void update(float dt) {

        if (this.body == null) return;

        boolean change;

        change = keys[0] != (keys[0] = Keyboard.getKeyDown(Keyboard.A_KEY) || Keyboard.getKeyHeld(Keyboard.A_KEY));
        change = change || keys[1] != (keys[1] = Keyboard.getKeyDown(Keyboard.D_KEY) || Keyboard.getKeyHeld(Keyboard.D_KEY));
        change = change || keys[2] != (keys[2] = Keyboard.getKeyDown(Keyboard.W_KEY) || Keyboard.getKeyHeld(Keyboard.W_KEY));

        if (!change) return;

        //remove old movement
        this.body.velocity().add(currentDirection.mul(-1, 0));
        currentDirection = new Vector2f();

        //define new movement

        if (keys[0] != keys[1]) {
            currentDirection.add(keys[0] ? -movementSpeed : movementSpeed, 0);
        }

        if (keys[2] && this.body.isColliding())
            currentDirection.add(0, -movementSpeed * 3);

        System.out.println("change move direction to: " + currentDirection + " | " + Arrays.toString(keys));
        this.body.velocity().add(currentDirection);

    }

}
