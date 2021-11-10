package scenes;

import components.GameObject;
import components.SpriteRenderer;
import fonts.Font;
import graphics.Camera;
import graphics.Color;
import graphics.Window;
import input.Mouse;
import org.joml.Vector2f;
import org.joml.Vector3f;
import scene.Scene;
import ui.Text;
import util.Engine;

import static graphics.Graphics.setDefaultBackground;

public class UITestingScene extends Scene {

    Font openSans;

    public static void main(String[] args) {
        Engine.init(1600, 900, "UI Testing");
        Engine.scenes().switchScene(new UITestingScene(), true);
        Engine.showWindow();
    }

    public void awake () {

        camera = new Camera();
        setDefaultBackground(Color.WHITE);

    }

    public void update () {

    }

}