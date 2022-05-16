package scenes;

import graphics.Camera;
import graphics.Color;
import graphics.Window;
import org.lwjgl.glfw.GLFW;
import scene.Scene;
import ui.*;
import ui.element.Button;
import ui.fonts.Font;
import ui.layout.AbsoluteLayout;
import ui.layout.BoxLayout;
import ui.layout.GridLayout;
import util.Engine;
import util.Logger;

import static graphics.Graphics.setDefaultBackground;

public class UIRenderingDemo extends Scene {



    public static void main(String[] args) {
        Engine.init(900, 600, "Azurite UI Rendering Demo");
        Engine.scenes().switchScene(new UIRenderingDemo(), true);
        Engine.showWindow();
    }

    Button button;
    private Layer menu;

    public void awake() {

        camera = new Camera();
        setDefaultBackground(Color.WHITE);

        menu = new Layer(0, 0, Window.getWidth(), Window.getHeight());
        Container container = new Container(100, 100, 500, 400, new AbsoluteLayout());
        menu.registerComponent(container);

        button = new Button("Button", "src/assets/images/button-300-84.png", Color.WHITE, new Frame(10, 10, 300, 84));
        button.tintColor = new Color(200, 200, 200, 255).toNormalizedVec4f();
//        container.addElement(e);
        uiRenderer.add(button);
        addUIElement(button);

        button.getEventHandler().registerListener(EventHandler.Event.MOUSE_CLICK, e -> {
            Logger.logInfo("CLICKED! \n" + e.getElement().toString());
        });
    }

    public void update () {
        menu.update();
    }
}
