package ecs;

import input.Keyboard;
import org.joml.Vector2f;
import physics.force.Force;

//this class is just made to support topDown and platformer demo scenes - its not good at all!!!!
public class CharacterController extends Component {

    private final float speedModifier;
    private final Force playerInputForce;

    private CharacterController(float speedModifier, Force playerInputForce) {
        super(ComponentOrder.INPUT);
        this.playerInputForce = playerInputForce;
        this.speedModifier = speedModifier;
    }

    public float getSpeedModifier() {
        return speedModifier;
    }

    public Force getPlayerInputForce() {
        return playerInputForce;
    }

    public static CharacterController standardPlatformer(Dynamics dynamics, float speedModifier) {
        Force f = new Force() {
            private final Vector2f direction = new Vector2f(0, 0);

            @Override
            public String identifier() {
                return "GodlikePlayerInput";
            }

            @Override
            public boolean update(float dt) {
                direction.set(0, 0);
                if (up()) direction.add(0, -speedModifier);
                //nothing on down input
                if (left()) direction.add(-speedModifier, 0);
                if (right()) direction.add(speedModifier, 0);
                return true;
            }

            @Override
            public Vector2f direction() {
                return direction;
            }
        };
        dynamics.applyForce(f);
        return new CharacterController(speedModifier, f);
    }

    public static CharacterController standardTopDown(Dynamics dynamics, float speedModifier) {
        Force f = new Force() {
            private final Vector2f direction = new Vector2f(0, 0);

            @Override
            public String identifier() {
                return "GodlikePlayerInput";
            }

            @Override
            public boolean update(float dt) {
                direction.set(0, 0);
                if (up()) direction.add(0, -speedModifier);
                if (down()) direction.add(0, speedModifier);
                if (left()) direction.add(-speedModifier, 0);
                if (right()) direction.add(speedModifier, 0);
                return true;
            }

            @Override
            public Vector2f direction() {
                return direction;
            }
        };
        dynamics.applyForce(f);
        return new CharacterController(speedModifier, f);
    }

    private static boolean up() {
        return Keyboard.keyDownOrHold(Keyboard.UP_ARROW) || Keyboard.keyDownOrHold(Keyboard.W_KEY);
    }

    private static boolean down() {
        return Keyboard.keyDownOrHold(Keyboard.DOWN_ARROW) || Keyboard.keyDownOrHold(Keyboard.S_KEY);
    }

    private static boolean left() {
        return Keyboard.keyDownOrHold(Keyboard.LEFT_ARROW) || Keyboard.keyDownOrHold(Keyboard.A_KEY);
    }

    private static boolean right() {
        return Keyboard.keyDownOrHold(Keyboard.RIGHT_ARROW) || Keyboard.keyDownOrHold(Keyboard.D_KEY);
    }

}
