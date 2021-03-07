package ecs;

import input.Gamepad;
import input.Keyboard;
import org.joml.Vector2f;
import physics.Transform;
import util.Engine;

public class CharacterController extends Component {

    Vector2f position = new Vector2f(0, 0);
    Vector2f speed = new Vector2f(5, 5);

    float gravity = 9;
    private boolean grounded = false;
    private Vector2f lastPosition;

    float sprintSpeed = 0;

    @Override
    public void start() {
        lastPosition = new Vector2f();
        position = gameObject.getTransform().getPosition();
        super.start();
    }

    @Override
    public void update (float dt) {

        if (Keyboard.getKey(Keyboard.SPACE)) {
            sprintSpeed = 20;
        } else {
            sprintSpeed = 0;
        }

        // X

        gameObject.setTransformX(position.x);
        if (Keyboard.getKey(Keyboard.A_KEY) || Keyboard.getKey(Keyboard.LEFT_ARROW)) {
            position.x += -speed.x + sprintSpeed * Engine.deltaTime;
        }
        if (Keyboard.getKey(Keyboard.D_KEY) || Keyboard.getKey(Keyboard.RIGHT_ARROW)) {
            position.x += speed.x + sprintSpeed * Engine.deltaTime;
        }

        // Y
        gameObject.setTransformY(position.y);

        if (Keyboard.getKey(Keyboard.W_KEY) || Keyboard.getKey(Keyboard.UP_ARROW)) {
            position.y += -speed.y + sprintSpeed * Engine.deltaTime;
        }
        if (Keyboard.getKey(Keyboard.S_KEY) || Keyboard.getKey(Keyboard.DOWN_ARROW)) {
            position.y += speed.y + sprintSpeed * Engine.deltaTime;
        }



    }

    

}
