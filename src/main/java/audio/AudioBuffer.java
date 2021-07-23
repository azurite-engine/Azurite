package audio;

import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.AL10.*;

/**
 * Instantiations of this class hold all information about how a sound should be replayed
 * and the sound data itself (if you know OpenGL, think of this as a massive vbo containing a
 * single noise or piece of music). <br><br>
 *
 * Personally I don't really like how it works right now; it allocates a lot of memory for every
 * sound file that needs to be played; maybe we can have a destructor or some kind of cleanup daemon
 * to do this for us? <br><br>
 *
 * I based the design off of an article by Michael Eric Oberlin found <a href="https://michaelericoberlin.wordpress.com/2016/07/04/effective-openal-with-lwjgl-3/">here</a>.
 *
 * @author HilbertCurve
 */
public class AudioBuffer {

    /**
     * Types of audio sources.
     */
    private static final int MONO = 1, STEREO = 2;

    /**
     * Size of the file in bytes, or -1 if the file could not be opened/processed.
     */
    private long fileSize = -1;
    /**
     * Music data.
     */
    private byte[] audioData;
    /**
     * Audio format of the file.
     */
    private int format;
    /**
     * Length of time it would take to play this buffer, in milliseconds.
     */
    private long time;
    /**
     * "Name" of the file. Used by OpenAL to open an ALuint buffer, I think.
     */
    private final IntBuffer name = BufferUtils.createIntBuffer(1);

    private AudioBuffer() {

    }

    public AudioBuffer(String path) {
        this.init(path);
    }

    /**
     * Initializer of an audio buffer.
     */
    private void init(String path) {
        AudioInputStream stream;

        // i don't know if we should be able to run this twice.
        if (fileSize != -1) return;

        try {
            alGenBuffers(name);

            stream = AudioSystem.getAudioInputStream(new File(path));
            fileSize = stream.getFrameLength() * stream.getFormat().getFrameSize();

            AudioFormat audioFormat = stream.getFormat();
            if(audioFormat.isBigEndian()) throw new UnsupportedAudioFileException("Can't handle Big Endian formats yet");

            int channels = audioFormat.getChannels();
            int sampSize = audioFormat.getSampleSizeInBits();

            if (channels == MONO) {
                if (sampSize == 8) {
                    format = AL_FORMAT_MONO8;
                } else if (sampSize == 16) {
                    format = AL_FORMAT_MONO16;
                }
            } else if (channels == STEREO) {
                if (sampSize == 8) {
                    format = AL_FORMAT_STEREO8;
                } else if (sampSize == 16) {
                    format = AL_FORMAT_STEREO16;
                }
            }

            // load data into a ByteBuffer
            audioData = new byte[stream.available()];
            stream.read(audioData);
            ByteBuffer bu = BufferUtils.createByteBuffer(audioData.length).put(audioData);
            bu.flip();

            alBufferData(getName(), format, bu, (int) audioFormat.getSampleRate());
            time = (long) (1000f * stream.getFrameLength() / audioFormat.getFrameRate());
        } catch (UnsupportedAudioFileException | IOException e) {
            assert false : "something went wrong with loading file " + path + ": " + e;
            fileSize = -1;
        }
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public int getName() {
        return name.get(0);
    }

    public long getTime() {
        return time;
    }
}
