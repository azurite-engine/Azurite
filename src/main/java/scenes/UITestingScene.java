package scenes;

import components.GameObject;
import components.SpriteRenderer;
import ui.fonts.Font;
import graphics.Camera;
import graphics.Color;
import input.Mouse;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import scene.Scene;
import ui.Container;
import ui.CursorManager;
import ui.EventHandler;
import ui.Layer;
import ui.element.Button;
import ui.layout.BoxLayout;
import util.Engine;

import java.text.NumberFormat;

import static graphics.Graphics.setDefaultBackground;

public class UITestingScene extends Scene {

    public static void main(String[] args) {
        Engine.init(1600, 900, "UI Testing");
        Engine.scenes().switchScene(new UITestingScene(), true);
        Engine.showWindow();
    }

    private Layer menu;
    Button button, button2;

    public void awake() {

        camera = new Camera();
        setDefaultBackground(Color.WHITE);

        CursorManager.getInstance().loadCursor(GLFW.GLFW_HAND_CURSOR);
        CursorManager.getInstance().loadCursor(GLFW.GLFW_ARROW_CURSOR);

        //full size layer
        menu = new Layer(0, 0, 1600, 900);
        //set size of the container to the left half of the layer
        Container container = new Container(0, 0, 800, 900, new BoxLayout(BoxLayout.Orientation.VERTICAL));
        //put the container onto the layer
        menu.registerComponent(container);

        // --------------------  just to show them:  --------------------
        GameObject bot = new GameObject(new Vector3f(0, 450, 0));
        bot.addComponent(new SpriteRenderer(Color.RED, new Vector2f(800, 450)));
        // -------------------- -------------------- --------------------

        //create the button
        button2 = new Button("bottomButton");
        //set cursor for testing on bottom button
        button2.setCursor(GLFW.GLFW_HAND_CURSOR);
        //add them to the container
        container.addComponent(button2);
        //add onClick functions
        button2.getEventHandler().registerListener(EventHandler.Event.MOUSE_CLICK, eventHandler -> {
            if (eventHandler.isMouseButtonClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                bot.getComponent(SpriteRenderer.class).setColor(Color.randomColor());
            }
        });

    }

    int i = 0;

    public void update() {
        super.update();
        menu.update();
        i = (i + 1) % 60;
        if (i == 0) {
            System.out.println(button2.getFrame());
            System.out.println(Mouse.mouse.toString(NumberFormat.getInstance()));
        }
    }

}