package audio;

import ecs.Component;
import org.joml.Vector2f;
import util.Engine;

import static org.lwjgl.openal.AL10.*;

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
        AudioMaster.alGetError();
        alListener3f(AL_VELOCITY, 0.0f, 0.0f, 0.0f);
        AudioMaster.alGetError();
    }

    public void update(float dt) {
        if (gameObject == null) return;
        Vector2f firstPos = position;
        Vector2f secondPos = gameObject.getReadOnlyPosition();
        alListener3f(AL_POSITION, secondPos.x, secondPos.y, 0.0f);
        AudioMaster.alGetError();
        alListener3f(AL_VELOCITY,
                secondPos.x - firstPos.x,
                secondPos.y - firstPos.y, 0.0f);
        AudioMaster.alGetError();
        position = gameObject.getReadOnlyPosition();
    }
}
