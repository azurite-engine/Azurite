package audio;

import org.junit.Test;
import util.Assets;

public class AudioTest {
    private static AudioSource s;
    private static AudioListener l;
    private static AudioBuffer b;


    @Test
    public void play() {
        AudioMaster.get();
        l = AudioListener.get();
        l.start();
        b = Assets.getAudioBuffer("src/assets/sounds/test1.wav");
        System.out.println(b.getAudioData().length);
        s = new AudioSource(b);
        s.start();
        System.out.println("playing");
        s.play(0);


        System.out.println("done");
        AudioMaster.get().clean();
    }
}
