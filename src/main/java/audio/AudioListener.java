package audio;

import ecs.Component;
import org.joml.Vector2f;
import util.Engine;

import static org.lwjgl.openal.AL10.*;
import static util.Utils.worldToScreenCoords;

/**
 * <h1>Azurite</h1>
 * This is where the "ear" is located. There can only ever be one Listener, hence the singleton.
 *
 * @author HilbertCurve
 */
public class AudioListener extends Component {

    private static AudioListener instance;

    private Vector2f position = new Vector2f();

    /**
     * Don't instantiate me; there should only be one audio listener.
     */
    private AudioListener() {

    }

    public static synchronized AudioListener get() {
        if (instance == null) {
            instance = new AudioListener();
        }

        return instance;
    }

    @Override
    public void start() {
        alListener3f(AL_POSITION, 0.0f, 0.0f, 0.0f);
        alListener3f(AL_VELOCITY, 0.0f, 0.0f, 0.0f);
        alListener3f(AL_ORIENTATION, 0f, 0f, -1f);
    }

    public void update(float dt) {
        Vector2f firstPos = worldToScreenCoords(position, Engine.scenes().currentScene().camera());
        Vector2f secondPos = worldToScreenCoords(gameObject.getTransform().position, Engine.scenes().currentScene().camera());
        alListener3f(AL_POSITION, secondPos.x, secondPos.y, 0.0f);
        alListener3f(AL_VELOCITY,
                secondPos.x - firstPos.x,
                secondPos.y - firstPos.y, 0.0f);
        position = gameObject.getTransform().position;
    }
}
