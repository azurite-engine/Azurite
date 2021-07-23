package audio;

import org.junit.Test;

public class AudioTest {
    private static AudioSource s;
    private static AudioListener l;

    @Test
    public void playTwoAudioFiles() {
        AudioMaster.get();
        l = AudioListener.get();
        l.start();
        s = new AudioSource("src/assets/sounds/test1.wav", "src/assets/sounds/test2.wav");
        s.start();
        AudioSource s1 = new AudioSource("src/assets/sounds/test1.wav");
        s1.start();
        System.out.println("playing");
        s.play(1);
        // note: playing two noises from the same AudioSource before the first one finishes
        // stops the first one from finishing.
        s1.play(0);


        try {
            // simulate things happening in game
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("done");
        AudioMaster.get().clean();
    }
}
