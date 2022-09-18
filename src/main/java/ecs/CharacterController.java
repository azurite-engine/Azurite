package ecs;

import input.Keyboard;
import input.Keys;
import org.joml.Vector2f;
import physics.force.Force;
import util.Engine;

/**
 * Character controllers built to support the Top down and Side scroller Demo scenes.
 */
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
                if (up()) direction.add(0, -speedModifier * dt);
                if (down()) direction.add(0, speedModifier * dt);
                if (left()) direction.add(-speedModifier * dt, 0);
                if (right()) direction.add(speedModifier * dt, 0);
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
        return Keyboard.keyDownOrHold(Keys.KEY_UP) || Keyboard.keyDownOrHold(Keys.KEY_W);
    }

    private static boolean down() {
        return Keyboard.keyDownOrHold(Keys.KEY_DOWN) || Keyboard.keyDownOrHold(Keys.KEY_S);
    }

    private static boolean left() {
        return Keyboard.keyDownOrHold(Keys.KEY_LEFT) || Keyboard.keyDownOrHold(Keys.KEY_A);
    }

    private static boolean right() {
        return Keyboard.keyDownOrHold(Keys.KEY_RIGHT) || Keyboard.keyDownOrHold(Keys.KEY_D);
    }
}
