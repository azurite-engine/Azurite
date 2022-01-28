package audio;

import ecs.Component;
import org.joml.Vector2f;
import util.Assets;
import util.Engine;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL10.*;

/**
 * <h1>Azurite</h1>
 * Object representing a sound's source as well as it's velocity (for applying the doppler effect).
 *
 * @author HilbertCurve
 */
public class AudioSource extends Component {

    /**
     * List of audio buffers this source can play.
     */
    public List<AudioBuffer> audioBuffers = new ArrayList<>();
    /**
     * Index of the currently selected buffer.
     */
    private int index = 0;
    /**
     * Amount of time left until selected buffer has finished playing, in milliseconds.
     */
    private long timeLeft = 0;

    /**
     * Whether the buffer loops, a.k.a. the value of dest after
     * <code>alGetSourcei(this.sourceID, AL_LOOPING, dest)</code> is run.
     */
    private boolean isLooping;

    private int sourceID;
    private Vector2f position;

    public AudioSource() {
        position = new Vector2f(1.0f, 0.0f);
        AudioMaster.get().addSource(this);
    }

    public AudioSource(AudioBuffer buffer) {
        position = new Vector2f(0.0f, 0.0f);
        audioBuffers.add(buffer);
        AudioMaster.get().addSource(this);
    }

    public AudioSource(String... sources) {
        position = new Vector2f(0.0f, 0.0f);
        for (String s : sources) {
            audioBuffers.add(Assets.getAudioBuffer(s));
        }
        AudioMaster.get().addSource(this);
    }

    public void setLooping(boolean looping) {
        if (looping) {
            alSourcei(sourceID, AL_LOOPING, AL_TRUE);
        } else {
            alSourcei(sourceID, AL_LOOPING, AL_FALSE);
        }
        isLooping = looping;
    }

    public boolean isLooping() {
        return isLooping;
    }

    /**
     * Sets the selected buffer to whatever the index indicates, then plays all of
     * this buffer.
     */
    public void play(int index) {
        int[] isPlaying = new int[1];
        alGetSourcei(this.sourceID, AL_SOURCE_STATE, isPlaying);
        //if (isPlaying[0] == AL_PLAYING) this.stop();

        setSelectedBuffer(index);
        this.timeLeft = this.getSelectedBuffer().getTime();
        alSourcePlay(sourceID);
    }

    /**
     * Sets the selected buffer to whatever the index indicates, then plays <code>millis</code>
     * milliseconds of this buffer.
     */
    public void play(int index, long millis) {
        int[] isPlaying = new int[1];
        alGetSourcei(this.sourceID, AL_SOURCE_STATE, isPlaying);
        if (isPlaying[0] == AL_PLAYING) this.stop();

        setSelectedBuffer(index);
        this.timeLeft = millis;
        alSourcePlay(sourceID);
    }

    public boolean isPlaying() {
        int[] isPlaying = new int[1];
        alGetSourcei(this.sourceID, AL_SOURCE_STATE, isPlaying);
        return isPlaying[0] == AL_PLAYING;
    }

    public void stop() {
        alSourceStop(sourceID);
        this.timeLeft = 0;
        System.out.println("foo");
    }

    public void delete() {
        alDeleteSources(sourceID);
    }

    public AudioBuffer getSelectedBuffer() {
        return audioBuffers.get(index);
    }

    public void setSelectedBuffer(int i) {
        index = i;
        alSourcei(sourceID, AL_BUFFER, getSelectedBuffer().getName());
    }

    /*
     * Here I define the audio source location as the location of the gameObject, I might want to make this a little
     * more flexible; what if the sprite's mouth isn't located on it's stomach (or wherever the default position is
     * in relation to the sprite)?
     */
    @Override
    public void start() {
        sourceID = alGenSources();
        Vector2f screenPos = Engine.scenes().currentScene().camera().position;
        alSourcei(sourceID, AL_BUFFER, getSelectedBuffer().getName());
        alSource3f(sourceID, AL_POSITION, screenPos.x, screenPos.y, 0f);
        alSource3f(sourceID, AL_VELOCITY, 0f, 0f, 0f);

        alSourcef(sourceID, AL_PITCH, 1);
        alSourcef(sourceID, AL_GAIN, 1f);
        alSourcei(sourceID, AL_LOOPING, AL_FALSE);
        this.isLooping = false;
    }

    @Override
    public void update(float dt) {
        timeLeft -= dt;
        if (timeLeft <= 0) {
            if (!isLooping && isPlaying())
                this.stop();
            else
                timeLeft = getSelectedBuffer().getTime();
        }

        if (gameObject == null) return;
        Vector2f firstPos = position;
        Vector2f secondPos = gameObject.getReadOnlyPosition();
        alListener3f(AL_POSITION, secondPos.x, secondPos.y, 0.0f);
        alListener3f(AL_VELOCITY,
                secondPos.x - firstPos.x,
                secondPos.y - firstPos.y, 0.0f);
        position = gameObject.getReadOnlyPosition();
    }
}
