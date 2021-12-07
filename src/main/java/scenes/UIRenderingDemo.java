package scenes;

import components.GameObject;
import components.SpriteRenderer;
import graphics.Camera;
import graphics.Color;
import graphics.Window;
import input.Mouse;
import org.joml.Vector2f;
import org.joml.Vector3f;
import scene.Scene;
import ui.ElementRenderer;
import ui.Text;
import ui.fonts.Font;
import util.Engine;

import static graphics.Graphics.setDefaultBackground;

public class UIRenderingDemo extends Scene {

    Font openSans;
    ElementRenderer e;

    public static void main(String[] args) {
        Engine.init(900, 600, "Azurite UI Rendering Demo");
        Engine.scenes().switchScene(new UIRenderingDemo(), true);
        Engine.showWindow();
    }

    public void awake () {
        camera = new Camera();
        setDefaultBackground(Color.WHITE);

        e = new ElementRenderer(Color.RED, new Vector2f(100, 100));
        uiRenderer.add(e);
        addUIElement(e);

        openSans = new Font("src/assets/fonts/OpenSans-Regular.ttf", 20, true);
    }

    public void update () {

    }

}
