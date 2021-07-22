package audio;

import ecs.Component;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL10.*;

/**
 * Object representing a sound's source as well as it's velocity (for applying the doppler effect).
 */
public class AudioSource extends Component {

    public int sourceID;
    public Vector3f position;
    /**
     * List of audio buffers this source can play.<br>
     * TODO: make an audio batch to reduce OpenAL calls?
     */
    public List<AudioBuffer> audioBuffers = new ArrayList<>();
    /**
     * Index of the currently selected buffer.
     */
    private int index = 0;
    private Vector2f firstPos;

    public AudioSource() {
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        AudioMaster.get().addSource(this);
    }

    public AudioSource(AudioBuffer buffer) {
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        audioBuffers.add(buffer);
        AudioMaster.get().addSource(this);
    }

    /**
     * Sets the selected buffer to whatever the index indicates, then plays all of
     * this buffer.
     */
    public void play(int index) {
        setSelectedBuffer(index);
        play0(getSelectedBuffer().getTime());
    }

    /**
     * Sets the selected buffer to whatever the index indicates, then plays <code>millis</code>
     * milliseconds of this buffer.
     */
    public void play(int index, long millis) {
        setSelectedBuffer(index);
        play0(millis);
    }

    // not the best way to do it...
    private void play0(long millis) {
        alSourcePlay(sourceID);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stop();
    }

    public void stop() {
        alSourceStop(sourceID);
    }

    public void delete() {
        alDeleteSources(sourceID);
    }

    public AudioBuffer getSelectedBuffer() {
        return audioBuffers.get(index);
    }

    public void setSelectedBuffer(int i) {
        index = i;
        alSourcei(sourceID, AL_BUFFER, audioBuffers.get(index).getAudioData()[0]);
    }

    /*
     * Here I define the audio source location as the location of the gameObject, I might want to make this a little
     * more flexible; what if the sprite's mouth isn't located on it's stomach (or wherever the default position is
     * in relation to the sprite)?
     */
    @Override
    public void start() {
        sourceID = alGenSources();
        alSourcei(sourceID, AL_BUFFER, getSelectedBuffer().getName());
        alSource3f(sourceID, AL_POSITION, 0f, 0f, 0f);
        alSource3f(sourceID, AL_VELOCITY, 0f, 0f, 0f);

        alSourcef(sourceID, AL_PITCH, 1);
        alSourcef(sourceID, AL_GAIN, 1f);
        alSourcei(sourceID, AL_LOOPING, AL_FALSE);
    }

    @Override
    public void update(float dt) {
        Vector2f secondPos = new Vector2f();
        alListener3f(AL_POSITION, secondPos.x, secondPos.y, 0.0f);
        alListener3f(AL_VELOCITY,
                secondPos.x - firstPos.x,
                secondPos.y - firstPos.y, 0.0f);
    }
}
