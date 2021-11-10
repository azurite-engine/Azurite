package scenes;

import components.GameObject;
import components.SpriteRenderer;
import fonts.Font;
import graphics.Camera;
import graphics.Color;
import input.Mouse;
import org.joml.Vector2f;
import org.joml.Vector3f;
import scene.Scene;
import ui.UIContainer;
import ui.UILayer;
import ui.component.Button;
import ui.layout.BoxLayout;
import util.Engine;

import java.text.NumberFormat;

import static graphics.Graphics.setDefaultBackground;

public class UITestingScene extends Scene {

    Font openSans;

    public static void main(String[] args) {
        Engine.init(1600, 900, "UI Testing");
        Engine.scenes().switchScene(new UITestingScene(), true);
        Engine.showWindow();
    }

    private UILayer menu;
    Button button, button2;

    public void awake() {

        camera = new Camera();
        setDefaultBackground(Color.WHITE);

        //full size layer
        menu = new UILayer(0, 0, 1600, 900);
        //add container
        UIContainer container = new UIContainer(new BoxLayout(BoxLayout.Orientation.VERTICAL));
        //set size of the container to the left half of the layer
        container.getFrame().set(0, 0, 800, 900);
        menu.registerComponent(container);
        button = new Button("topButton");
        button2 = new Button("bottomButton");
        container.addComponent(button);
        container.addComponent(button2);

        //just to show them:
        GameObject top = new GameObject(new Vector3f(0, 0, 0));
        GameObject bot = new GameObject(new Vector3f(0, 450, 0));
        top.addComponent(new SpriteRenderer(Color.GREEN, new Vector2f(800, 450)));
        bot.addComponent(new SpriteRenderer(Color.RED, new Vector2f(800, 450)));

    }

    int i = 0;

    public void update() {
        menu.update();
        i = (i + 1) % 60;
        if (i == 0) {
            System.out.println(button.getFrame());
            System.out.println(button2.getFrame());
            System.out.println(Mouse.mouse.toString(NumberFormat.getInstance()));
        }
    }

}