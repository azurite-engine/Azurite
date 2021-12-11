package scenes;

import ecs.GameObject;
import ecs.SpriteRenderer;
import ecs.Text;
import fonts.Font;
import graphics.Camera;
import graphics.Color;
import graphics.Window;
import input.Mouse;
import org.joml.Vector2f;
import scene.Scene;
import util.Engine;

import static graphics.Graphics.setDefaultBackground;

public class TextRenderingDemo extends Scene {

    Font maghrib;
    Font openSans;

    Text titleText;
    Text centeredText;
    Text movingText;
    Text rainbowText;

    GameObject centerLine;

    public static void main(String[] args) {
        Engine.init(900, 600, "Azurite Font Rendering Demo");
        Engine.scenes().switchScene(new TextRenderingDemo(), true);
        Engine.showWindow();
    }

    public void awake () {

        camera = new Camera();
        setDefaultBackground(Color.WHITE);

        maghrib = new Font("src/assets/fonts/Maghrib-MVZpx.ttf", 50, true);
        openSans = new Font("src/assets/fonts/OpenSans-Regular.ttf", 20, true);

        float halfWindowWidth = Window.getWidth() / 2.0f;
        titleText = new Text("Azurite text rendering demo", maghrib, Color.BLACK, halfWindowWidth, 5, 1, true, true);
        movingText = new Text("HAHA", openSans, Color.RED, 200, 200);
        rainbowText = new Text("Rainbow text", openSans, Color.BLUE, 10, 50);
        centeredText = new Text("(Centered Text)\n(Centered Text line 2)", openSans, Color.BLACK, halfWindowWidth, 80, 1, true, true);

        centerLine = new GameObject("", new Vector2f(halfWindowWidth, 50), 1).addComponent(new SpriteRenderer(Color.GRAY, new Vector2f(1, 100)));
    }

    public void update () {

        rainbowText.change("Azurite Engine demo\nDT: " + Engine.deltaTime() + "\nFPS: " + (int) Engine.getInstance().getWindow().getFPS() + "\nMouse " + Mouse.mouse.x + " | " + Mouse.mouse.y);

        movingText.setPosition(Mouse.mouse);

        rainbowText.rainbowify();

    }

}
