package scenes;

import graphics.Camera;
import graphics.Color;
import graphics.Window;
import input.Mouse;
import org.joml.Vector2f;
import scene.Scene;
import ui.Container;
import ui.ElementRenderer;
import ui.Layer;
import ui.Text;
import ui.element.Button;
import ui.fonts.Font;
import ui.layout.BoxLayout;
import util.Engine;

import static graphics.Graphics.setDefaultBackground;

public class UIRenderingDemo extends Scene {

    Font openSans;
    Text movingText;

    ElementRenderer e;

    public static void main(String[] args) {
        Engine.init(900, 600, "Azurite UI Rendering Demo");
        Engine.scenes().switchScene(new UIRenderingDemo(), true);
        Engine.showWindow();
    }

    private Layer menu;
    Button button;

    public void awake() {

        camera = new Camera();
        setDefaultBackground(Color.WHITE);

        openSans = new Font("src/assets/fonts/OpenSans-Regular.ttf", 20, true);
        movingText = new Text("HAHA", openSans, Color.GREEN, 200, 200);

        menu = new Layer(100, 0, Window.getWidth(), Window.getHeight());
        Container container = new Container(0, 0, 800, 900, new BoxLayout(BoxLayout.Orientation.VERTICAL));
        menu.registerComponent(container);

        e = new ElementRenderer(Color.RED, new Vector2f(100, 100));

        container.addComponent(e);

        uiRenderer.add(e);
        addUIElement(e);
    }

    public void update () {
        menu.update();
        movingText.setPosition(Mouse.mouse);
    }

}
