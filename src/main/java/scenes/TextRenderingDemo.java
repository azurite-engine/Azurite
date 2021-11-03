package scenes;

import ecs.GameObject;
import ecs.SpriteRenderer;
import ecs.Text;
import fonts.Font;
import graphics.Camera;
import graphics.Color;
import input.Mouse;
import org.joml.Vector2f;
import org.joml.Vector3f;
import scene.Scene;
import util.Engine;
import util.Transform;

import static graphics.Graphics.setDefaultBackground;

public class TextRenderingDemo extends Scene {

    Font maghrib;
    Font openSans;

    Text titleText;
    Text movingText;
    Text rainbowText;

    GameObject centerLine;

    public static void main(String[] args) {
        Engine.init(1920, 1080, "Azurite Engine Demo 3");
        Engine.scenes().switchScene(new TextRenderingDemo(), true);
        Engine.showWindow();
    }

    public void awake () {

        camera = new Camera();
        setDefaultBackground(Color.WHITE);

        maghrib = new Font("src/assets/fonts/Maghrib-MVZpx.ttf", 50, true);
        openSans = new Font("src/assets/fonts/OpenSans-Regular.ttf", 20, true);

        titleText = new Text("Azurite text\nrendering demo", openSans, Color.BLACK,300, 5, 1, true, true);
//        movingText = new Text("HAHA", openSans, Color.RED, 200, 200);
//        rainbowText = new Text("Begin typing to change this text: ", openSans, Color.BLUE, 10, 50);

        centerLine = new GameObject("", new Vector3f(300, 0, 0), 1).addComponent(new SpriteRenderer(Color.BLUE, new Vector2f(3, 200)));
    }

    public void update () {

//        rainbowText.change("Azurite Engine demo\nDT: " + Engine.deltaTime() + "\nFPS: " + (int) Engine.getInstance().getWindow().getFPS() + "\nMouse " + Mouse.mouse.x() + " | " + Mouse.mouse.y());
//
//        movingText.setPosition(Mouse.mouse);
//
//        rainbowText.rainbowify();

    }

}