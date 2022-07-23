package scenes;

import audio.AudioListener;
import audio.AudioSource;
import ecs.GameObject;
import graphics.Camera;
import org.joml.Vector2f;
import scene.Scene;
import util.Assets;
import util.Engine;
import util.Log;

import static graphics.Graphics.setDefaultBackground;

/**
 * Minimal usage example of the AudioListener and AudioSource components.
 */
public class AudioDemo extends Scene {
    public static void main(String[] args) {
        Log.setLogLevel(Log.ALL);
        Engine.init(1080, 720, "Azurite Audio Demo", 0.01f);
        Engine.scenes().switchScene(new AudioDemo());
        Engine.showWindow();
    }

    GameObject barFoo;

    public void awake() {
        camera = new Camera();
        setDefaultBackground(0);

        barFoo = new GameObject(new Vector2f(0, 0));
        barFoo.addComponent(AudioListener.get());
        barFoo.addComponent(new AudioSource(Assets.getAudioBuffer("src/assets/sounds/lines_of_code.wav")));
        barFoo.getComponent(AudioSource.class).play(0, true);
    }

    public void update() {

    }
}
