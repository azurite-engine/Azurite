package audio;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;
import util.Log;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL10.alGetString;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.EXTEfx.ALC_MAX_AUXILIARY_SENDS;

/**
 * 
 * Main audio center: equivocal to graphics.Window but for initializing audio stuff,
 * like toggling and changing some parameters in the OpenAL API
 *
 * @author HilbertCurve
 */
public class AudioMaster {

    private static List<AudioSource> sources = new ArrayList<>();
    private static AudioMaster instance;
    private static long device;

    private AudioMaster() {

    }

    public static synchronized AudioMaster get() {
        if (instance == null) {
            try {
                init();
            } catch (Exception e) {
                Log.fatal("could not initialize AudioMaster.");
                e.printStackTrace();
            }
            instance = new AudioMaster();
        }

        return instance;
    }

    /**
     * Allows us to use OpenAL.
     */
    private static void init() throws Exception {
        // get pointer to default device
        device = ALC10.alcOpenDevice((ByteBuffer) null);

        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        // Create context (often already present, but here, necessary)
        IntBuffer contextAttribList = BufferUtils.createIntBuffer(16);

        // put some attributes in...
        contextAttribList.put(ALC_REFRESH);
        contextAttribList.put(60);

        contextAttribList.put(ALC_SYNC);
        contextAttribList.put(ALC_FALSE);

        contextAttribList.put(ALC_MAX_AUXILIARY_SENDS);
        contextAttribList.put(2);

        contextAttribList.put(0);
        contextAttribList.flip();

        // create the context with the provided attributes
        long newContext = ALC10.alcCreateContext(device, contextAttribList);

        if (!ALC10.alcMakeContextCurrent(newContext)) {
            Log.fatal("failed to make context current");
            throw new Exception("Failed to make context current");
        }

        AL.createCapabilities(deviceCaps);
        AudioMaster.alGetError();
    }

    public void addSource(AudioSource s) {
        sources.add(s);
    }

    public void update(float dt) {
        AudioListener.get().update(dt);
    }

    public void clean() {
        for (AudioSource s : sources) {
            s.delete();
        }

        sources.clear();
        alcCloseDevice(device);
    }

    public static void alGetError() {
        int err = AL10.alGetError();
        if (err != 0) {
            Log.fatal("AL error received: " + alGetString(err));
            throw new RuntimeException();
        }
    }

}
