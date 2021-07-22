package audio;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.EXTEfx.ALC_MAX_AUXILIARY_SENDS;

public class ToBeAbstracted {
    public ToBeAbstracted() throws Exception {
        //Start by acquiring the default device
        long device = ALC10.alcOpenDevice((ByteBuffer)null);

        //Create a handle for the device capabilities, as well.
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        // Create context (often already present, but here, necessary)
        IntBuffer contextAttribList = BufferUtils.createIntBuffer(16);

        // Note the manner in which parameters are provided to OpenAL...
        contextAttribList.put(ALC_REFRESH);
        contextAttribList.put(60);

        contextAttribList.put(ALC_SYNC);
        contextAttribList.put(ALC_FALSE);

        // Don't worry about this for now; deals with effects count
        contextAttribList.put(ALC_MAX_AUXILIARY_SENDS);
        contextAttribList.put(2);

        contextAttribList.put(0);
        contextAttribList.flip();

        //create the context with the provided attributes
        long newContext = ALC10.alcCreateContext(device, contextAttribList);

        if(!ALC10.alcMakeContextCurrent(newContext)) {
            throw new Exception("Failed to make context current");
        }

        AL.createCapabilities(deviceCaps);


        //define listener
        AL10.alListener3f(AL10.AL_VELOCITY, 0f, 0f, 0f);
        AL10.alListener3f(AL10.AL_ORIENTATION, 0f, 0f, -1f);


        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        AL10.alGenBuffers(buffer);

        //We'll get to this next!
        long time = createBufferData(buffer.get(0));

        //Define a source
        int source = AL10.alGenSources();
        AL10.alSourcei(source, AL10.AL_BUFFER, buffer.get(0));
        AL10.alSource3f(source, AL10.AL_POSITION, 0f, 0f, 0f);
        AL10.alSource3f(source, AL10.AL_VELOCITY, 0f, 0f, 0f);

        //fun stuff
        AL10.alSourcef(source, AL10.AL_PITCH, 1);
        AL10.alSourcef(source, AL10.AL_GAIN, 1f);
        AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_FALSE);

        //Trigger the source to play its sound
        AL10.alSourcePlay(source);

        try {
            Thread.sleep(time); //Wait for the sound to finish
        } catch(InterruptedException ex) {}

        AL10.alSourceStop(source); //Demand that the sound stop

        //and finally, clean up
        AL10.alDeleteSources(source);


    }

    private long createBufferData(int p) throws UnsupportedAudioFileException, IOException {
        //shortcut finals:
        final int MONO = 1, STEREO = 2;

        AudioInputStream stream = null;
        stream = AudioSystem.getAudioInputStream(new File("src/assets/sounds/test1.wav"));

        AudioFormat format = stream.getFormat();
        if(format.isBigEndian()) throw new UnsupportedAudioFileException("Can't handle Big Endian formats yet");

        //load stream into byte buffer
        int openALFormat = -1;
        switch(format.getChannels()) {
            case MONO:
                switch(format.getSampleSizeInBits()) {
                    case 8:
                        openALFormat = AL10.AL_FORMAT_MONO8;
                        break;
                    case 16:
                        openALFormat = AL10.AL_FORMAT_MONO16;
                        break;
                }
                break;
            case STEREO:
                switch(format.getSampleSizeInBits()) {
                    case 8:
                        openALFormat = AL10.AL_FORMAT_STEREO8;
                        break;
                    case 16:
                        openALFormat = AL10.AL_FORMAT_STEREO16;
                        break;
                }
                break;
        }

        //load data into a byte buffer
        //I've elected to use IOUtils from Apache Commons here, but the core
        //notion is to load the entire stream into the byte array--you can
        //do this however you would like.
        byte[] b = new byte[stream.available()];
        stream.read(b);
        ByteBuffer data = BufferUtils.createByteBuffer(b.length).put(b);
        data.flip();

        //load audio data into appropriate system space....
        AL10.alBufferData(p, openALFormat, data, (int)format.getSampleRate());

        //and return the rough notion of length for the audio stream!
        return (long)(1000f * stream.getFrameLength() / format.getFrameRate());
    }

    public static void main(String[] args) {
        try {
            ToBeAbstracted p = new ToBeAbstracted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
