package ecs;

import input.Keyboard;
import org.joml.Vector2f;

import java.util.function.Consumer;

//TODO this class is just made to support topDown and platformer demo scenes - its not good at all
public class CharacterController extends Component {

    public static final byte LEFT = 0;
    public static final byte RIGHT = 1;
    public static final byte UP = 2;
    public static final byte DOWN = 3;

    private final boolean[] keys = new boolean[4];
    private float speedModifier;

    private Consumer<CharacterController> changeInputEvent;


    public CharacterController(Consumer<CharacterController> handler, float speedModifier) {
        this.speedModifier = speedModifier;
        this.changeInputEvent = handler;
        this.order = SpriteRenderer.ORDER - 5;
    }

    public static Consumer<CharacterController> standardPlatformer(RigidBody body) {
        return new Consumer<CharacterController>() {
            Vector2f lastDirection = new Vector2f(0, 0);

            @Override
            public void accept(CharacterController cc) {
                if (body.velocity().x != 0)
                    body.velocity().sub(lastDirection.x, 0);
                if (body.velocity().y != 0)
                    body.velocity().sub(0, lastDirection.y);
                Vector2f direction = new Vector2f();
                if (cc.isKeyUsed(LEFT)) {
                    direction.add(-cc.speedModifier, 0);
                }
                if (cc.isKeyUsed(RIGHT)) {
                    direction.add(cc.speedModifier, 0);
                }
                if (cc.isKeyUsed(UP)) {
                    body.velocity().add(0, cc.speedModifier * -3);
                }
                lastDirection = direction;
                body.velocity().add(direction);
            }
        };
    }

    public static Consumer<CharacterController> standardTopDown(RigidBody body) {
        return new Consumer<CharacterController>() {
            Vector2f lastDirection = new Vector2f(0, 0);

            @Override
            public void accept(CharacterController cc) {
                if (body.velocity().x != 0)
                    body.velocity().sub(lastDirection.x, 0);
                if (body.velocity().y != 0)
                    body.velocity().sub(0, lastDirection.y);
                Vector2f direction = new Vector2f(0, 0);
                if (cc.isKeyUsed(LEFT)) {
                    direction.add(-cc.speedModifier, 0);
                }
                if (cc.isKeyUsed(RIGHT)) {
                    direction.add(cc.speedModifier, 0);
                }
                if (cc.isKeyUsed(UP)) {
                    direction.add(0, -cc.speedModifier);
                }
                if (cc.isKeyUsed(DOWN)) {
                    direction.add(0, cc.speedModifier);
                }
                lastDirection = direction;
                body.velocity().add(direction);
            }
        };
    }

    public void setSpeedModifier(float speedModifier) {
        this.speedModifier = speedModifier;
    }

    @Override
    public void update(float dt) {

        boolean change;

        change = keys[0] != (keys[0] = Keyboard.getKeyDown(Keyboard.A_KEY) || Keyboard.getKeyHeld(Keyboard.A_KEY));
        change = change || keys[1] != (keys[1] = Keyboard.getKeyDown(Keyboard.D_KEY) || Keyboard.getKeyHeld(Keyboard.D_KEY));
        change = change || keys[2] != (keys[2] = Keyboard.getKeyDown(Keyboard.W_KEY) || Keyboard.getKeyHeld(Keyboard.W_KEY));
        change = change || keys[3] != (keys[3] = Keyboard.getKeyDown(Keyboard.S_KEY) || Keyboard.getKeyHeld(Keyboard.S_KEY));

        //if something changed, update input
        if (change)
            changeInputEvent.accept(this);

    }

    public boolean isKeyUsed(int key) {
        if (key < 0 || key > 3) return false;
        return keys[key];
    }

}
