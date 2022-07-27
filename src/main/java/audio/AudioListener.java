package audio;

import ecs.Component;
import graphics.Camera;
import org.joml.Vector2f;
import util.Engine;

import static org.lwjgl.openal.AL10.*;

/**
 * 
 * This is where the "ear" is located. There can only ever be one Listener, hence the singleton.
 *
 * @author HilbertCurve
 */
public class AudioListener {

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

    public void update(float dt) {
        Vector2f firstPos = position;
        Vector2f secondPos = Camera.instance.getPosition();
        alListener3f(AL_POSITION, secondPos.x, secondPos.y, 0.0f);
        AudioMaster.alGetError();
        alListener3f(AL_VELOCITY,
                secondPos.x - firstPos.x,
                secondPos.y - firstPos.y, 0.0f);
        AudioMaster.alGetError();
        position = secondPos;
    }
}
