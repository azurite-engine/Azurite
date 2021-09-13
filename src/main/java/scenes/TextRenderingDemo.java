package scenes;

import ecs.Text;
import fonts.Font;
import graphics.Camera;
import graphics.Color;
import input.Keyboard;
import input.Mouse;
import scene.Scene;
import util.Engine;
import util.Utils;

import static graphics.Graphics.setDefaultBackground;

public class TextRenderingDemo extends Scene {

    Font maghrib;
    Font openSans;

    Text titleText;
    Text movingText;
    Text changingText;

    public static void main(String[] args) {
        Engine.init(1920, 1080, "Azurite Engine Demo 3");
        Engine.scenes().switchScene(new TextRenderingDemo(), true);
        Engine.showWindow();
    }

    public void awake () {

        camera = new Camera();
        setDefaultBackground(Color.BLACK);

        maghrib = new Font("src/assets/fonts/Maghrib-MVZpx.ttf", 50, true);
        openSans = new Font("src/assets/fonts/OpenSans-Regular.ttf", 20, true);

//        titleText = new Text("Azurite text rendering demo", maghrib, 10, 5, 100, true);
        movingText = new Text("HAHA", openSans, 200, 200, 100, true);
//        changingText = new Text("Begin typing to change this text: ", openSans, 10, 50, 100, true);
    }

    public void update () {

//        changingText.change(movingText.getX() + " | " + movingText.getY() + "\nMouse " + Mouse.mouse.x() + " | " + Mouse.mouse.y());

        movingText.setX(Mouse.mouseX);

//        movingText.setX(Mouse.mouseX);
//        movingText.setY(Mouse.mouseY);
//        movingText.addX(Engine.deltaTime() * 100 * (Mouse.mouse.x() <  movingText.getX() ? -1 : 1));
    }

}
