package audio;

import ecs.Component;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import util.Assets;
import util.Engine;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static audio.AudioBuffer.BUFFER_SIZE;
import static audio.AudioBuffer.NUM_BUFFERS;
import static org.lwjgl.openal.AL10.*;

/**
 * 
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
     * Index of the next alBuffer to be queued.
     */
    private int buffPtr = 0;

    /**
     * Current position in loaded buffer. Updated every time a new buffer is queued.
     */
    private long cursor = 0;

    private boolean atEnd = false;

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
        // artificial looping, because it doesn't work with
        // alSourcei
        isLooping = looping;
    }

    public boolean isLooping() {
        return isLooping;
    }

    private void clearQueue() {
        for (int i : getSelectedBuffer().getALBuffers())
            alSourceUnqueueBuffers(sourceID, new int[]{i});
        // suppress the error here; best to cover all possible bufferIDs
        //AudioMaster.alGetError();
        AL10.alGetError();
    }

    /**
     * Sets the selected buffer to whatever the index indicates, then plays all of
     * this buffer.
     */
    public void play(int index, boolean isLooping) {
        if (isPlaying()) stop();
        AudioMaster.alGetError();

        this.cursor = 0;
        this.atEnd = false;

        setLooping(isLooping);

        clearQueue();

        setSelectedBuffer(index);
        alSourcePlay(sourceID);
        AudioMaster.alGetError();
    }

    public boolean isPlaying() {
        int[] isPlaying = new int[1];
        alGetSourcei(this.sourceID, AL_SOURCE_STATE, isPlaying);
        AudioMaster.alGetError();
        return isPlaying[0] == AL_PLAYING;
    }

    public void stop() {
        alSourceStop(sourceID);
        AudioMaster.alGetError();

        clearQueue();

        this.cursor = 0;
        this.buffPtr = 0;
        this.atEnd = true;
    }

    void delete() {
        alDeleteSources(sourceID);
    }

    public AudioBuffer getSelectedBuffer() {
        return audioBuffers.get(index);
    }

    void setSelectedBuffer(int i) {
        index = i;

        AudioBuffer buff = audioBuffers.get(index);

        int toBuffer = Math.min(NUM_BUFFERS, (int) Math.ceil((float) buff.getAudioData().length / BUFFER_SIZE));

        for (int inc = 0; inc < toBuffer; inc++) {
            int toCopy = Math.min(BUFFER_SIZE, buff.getAudioData().length - inc*BUFFER_SIZE);
            AudioMaster.alGetError();

            ByteBuffer bu = BufferUtils.createByteBuffer(BUFFER_SIZE);
            if (toCopy > 0) bu.put(buff.getAudioData(), (int) cursor, toCopy);
            AudioMaster.alGetError();

            bu.flip();
            if (toCopy > 0) cursor += toCopy;
            else cursor = buff.getAudioData().length;

            alBufferData(audioBuffers.get(index).getALBuffer(inc), buff.getFormat(), bu, (int) buff.getSampleRate());
            AudioMaster.alGetError();

            alSourceQueueBuffers(sourceID, audioBuffers.get(index).getALBuffer(inc));
            AudioMaster.alGetError();
        }
        buffPtr = toBuffer - 1;
        atEnd = false;
    }

    private void updateStream() {
        int processedBuffers = alGetSourcei(sourceID, AL_BUFFERS_PROCESSED);

        if (processedBuffers <= 0) return;

        while (processedBuffers-- != 0) {
            if (++buffPtr >= NUM_BUFFERS) buffPtr = 0;

            int currentBuffer = getSelectedBuffer().getALBuffer(buffPtr);
            alSourceUnqueueBuffers(sourceID, new int[]{currentBuffer});
            AudioMaster.alGetError();
            if (!isPlaying()) return;

            int toCopy = Math.min(BUFFER_SIZE, getSelectedBuffer().getAudioData().length - (int) cursor);
            AudioMaster.alGetError();

            ByteBuffer bu;

            // if this occurs we're at the end of the audio data
            if (cursor + BUFFER_SIZE > getSelectedBuffer().getAudioData().length) {
                // if we're looping, put start of file at the end of the most recent buffer
                if (this.isLooping) {
                    bu = BufferUtils.createByteBuffer(BUFFER_SIZE);
                    bu.put(this.getSelectedBuffer().getAudioData(), (int) cursor, toCopy);
                    if (!atEnd)
                        bu.put(this.getSelectedBuffer().getAudioData(), 0, BUFFER_SIZE - toCopy);
                    this.cursor = BUFFER_SIZE - toCopy;
                } else {
                    bu = BufferUtils.createByteBuffer(toCopy);
                    bu.put(this.getSelectedBuffer().getAudioData(), (int) cursor, toCopy);
                    atEnd = true; // otherwise we're at the end.
                    this.cursor = getSelectedBuffer().getAudioData().length;
                }
            } else {
                bu = BufferUtils.createByteBuffer(BUFFER_SIZE);
                bu.put(this.getSelectedBuffer().getAudioData(), (int) cursor, BUFFER_SIZE);
                this.cursor += BUFFER_SIZE;
            }

            bu.flip();

            alBufferData(currentBuffer, getSelectedBuffer().getFormat(), bu, (int) getSelectedBuffer().getSampleRate());
            AudioMaster.alGetError();
            alSourceQueueBuffers(sourceID, currentBuffer);
            AudioMaster.alGetError();
        }
    }

    @Override
    public void start() {
        sourceID = alGenSources();
        Vector2f screenPos = Engine.scenes().currentScene().camera().position;

        alSource3f(sourceID, AL_POSITION, screenPos.x, screenPos.y, 0f);
        alSource3f(sourceID, AL_VELOCITY, 0f, 0f, 0f);

        alSourcef(sourceID, AL_PITCH, 1);
        alSourcef(sourceID, AL_GAIN, 1f);
        alSourcei(sourceID, AL_LOOPING, AL_FALSE);
        this.isLooping = false;
    }

    @Override
    public void update(float dt) {
        updateStream();

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
