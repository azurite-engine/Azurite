package audio;

import ecs.Component;
import org.joml.Vector2f;

import static org.lwjgl.openal.AL10.*;

/**
 * This is where the "ear" is located. There can only ever be one Listener, hence the singleton.
 */
public class AudioListener extends Component {

    private static AudioListener instance;

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

    Vector2f firstPos = new Vector2f();

    @Override
    public void start() {
        alListener3f(AL_VELOCITY, 0.0f, 0.0f, 0.0f);
        alListener3f(AL_ORIENTATION, 0f, 0f, -1f);
    }

    public void update(float dt) {
        Vector2f secondPos = new Vector2f();
        alListener3f(AL_POSITION, secondPos.x, secondPos.y, 0.0f);
        alListener3f(AL_VELOCITY,
                secondPos.x - firstPos.x,
                secondPos.y - firstPos.y, 0.0f);
        firstPos = gameObject.getTransform().position;
    }
}
