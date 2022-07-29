package scenes;

import audio.AudioListener;
import audio.AudioSource;
import ecs.GameObject;
import input.Keyboard;
import input.Keys;
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
    GameObject barFoo1;
    public void awake() {
        camera = new Camera();
        setDefaultBackground(0);

        barFoo = new GameObject(new Vector2f(0, 0));
        barFoo.addComponent(new AudioSource(Assets.getAudioBuffer("src/assets/sounds/lines_of_code.wav")));
        //barFoo.getComponent(AudioSource.class).play(0, true);

        barFoo1 = new GameObject(new Vector2f(0, 0));
        barFoo1.addComponent(new AudioSource(Assets.getAudioBuffer("src/assets/sounds/hit.wav")));
    }

    boolean isPressed = false;
    public void update() {
        if (Keyboard.getKeyDown(Keys.KEY_SPACE)) {
            if (!isPressed)
                barFoo1.getComponent(AudioSource.class).play(0, false);
            isPressed = true;
        } else {
            isPressed = false;
        }
    }
}
