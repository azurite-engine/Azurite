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
import java.util.Arrays;

import static org.lwjgl.openal.AL10.*;

/**
 * 
 * Instantiations of this class hold all information about how a sound should be replayed
 * and the sound data itself (if you know OpenGL, think of this as a massive vbo containing a
 * single noise or piece of music). <br><br>
 *
 * Personally I don't really like how it works right now; it allocates a lot of memory for every
 * sound file that needs to be played; maybe we can have a destructor or some kind of cleanup daemon? <br><br>
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
     * Audio metadata.
     */
    private int format, channels, sampleSize;
    private float sampleRate;
    /**
     * Length of time it would take to play this buffer, in milliseconds.
     */
    private long time;

    static final int NUM_BUFFERS = 8, BUFFER_SIZE = 2 << 15;

    private int[] alBuffers = new int[NUM_BUFFERS];

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
            for (int i = 0; i < NUM_BUFFERS; i++) {
                alBuffers[i] = alGenBuffers();
            }
            AudioMaster.alGetError();

            stream = AudioSystem.getAudioInputStream(new File(path));
            fileSize = stream.getFrameLength() * stream.getFormat().getFrameSize();

            AudioFormat audioFormat = stream.getFormat();
            if(audioFormat.isBigEndian()) throw new UnsupportedAudioFileException("Can't handle Big Endian formats yet");

            channels = audioFormat.getChannels();
            sampleSize = audioFormat.getSampleSizeInBits();
            sampleRate = audioFormat.getSampleRate();

            if (channels == MONO) {
                if (sampleSize == 8) {
                    format = AL_FORMAT_MONO8;
                } else if (sampleSize == 16) {
                    format = AL_FORMAT_MONO16;
                }
            } else if (channels == STEREO) {
                if (sampleSize == 8) {
                    format = AL_FORMAT_STEREO8;
                } else if (sampleSize == 16) {
                    format = AL_FORMAT_STEREO16;
                }
            }

            audioData = new byte[stream.available()];
            stream.read(audioData);
        } catch (UnsupportedAudioFileException | IOException e) {
            assert false : "something went wrong with loading file " + path + ": " + e;
            fileSize = -1;
            alDeleteBuffers(alBuffers);
        }
    }

    public byte[] getAudioData() {
        return audioData;
    }

    int getALBuffer(int index) {
        return alBuffers[index];
    }

    public long getTime() {
        return time;
    }

    public int[] getALBuffers() {
        return alBuffers;
    }

    public int getFormat() {
        return format;
    }

    public float getSampleRate() {
        return sampleRate;
    }
}
