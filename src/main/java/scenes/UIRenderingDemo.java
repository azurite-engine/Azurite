package scenes;

import graphics.Camera;
import graphics.Color;
import graphics.Window;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import scene.Scene;
import ui.Container;
import ui.CursorManager;
import ui.ElementRenderer;
import ui.Layer;
import ui.element.Button;
import ui.fonts.Font;
import ui.layout.BoxLayout;
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

    private Layer menu;
    Button button, button2;

    public void awake() {

        camera = new Camera();
        setDefaultBackground(Color.DARK_GREEN);

        menu = new Layer(0, 0, Window.getWidth(), Window.getHeight());
        Container container = new Container(0, 0, 800, 900, new BoxLayout(BoxLayout.Orientation.VERTICAL));
        menu.registerComponent(container);

        e = new ElementRenderer(Color.RED, new Vector2f(100, 100));

        container.addComponent(e);

        uiRenderer.add(e);
        addUIElement(e);

        openSans = new Font("src/assets/fonts/OpenSans-Regular.ttf", 20, true);
    }

    public void update () {

    }

}
