package scenes;

import graphics.Camera;
import graphics.Color;
import graphics.Window;
import input.Mouse;
import org.joml.Vector2f;
import scene.Scene;
import ui.*;
import ui.element.Button;
import ui.fonts.Font;
import ui.layout.AbsoluteLayout;
import util.Engine;

import static graphics.Graphics.setDefaultBackground;

public class UIRenderingDemo extends Scene {

    Font openSans;
    Text movingText;

    RenderableElement e;
    RenderableElement f;

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
//        movingText = new Text("HAHA", openSans, Color.BLACK, 200, 200);

        menu = new Layer(0, 0, Window.getWidth(), Window.getHeight());
        Container container = new Container(100, 100, 500, 400, new AbsoluteLayout());
        menu.registerComponent(container);

        e = new Button("Button", "src/assets/images/button-300-84.png", new Frame(10, 10, 300, 84));
        f = new RenderableElement(Color.decode("#4e6b56"), new Frame(10, 110, 300, 84));

        container.addComponent(e);
        container.addComponent(f);

        uiRenderer.add(e);
        uiRenderer.add(f);
        addUIElement(e);
        addUIElement(f);
    }

    public void update () {
        menu.update();
    }

}
