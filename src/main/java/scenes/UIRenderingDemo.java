package scenes;

import graphics.Camera;
import graphics.Color;
import graphics.Window;
import scene.Scene;
import ui.*;
import ui.element.Button;
import ui.fonts.Font;
import ui.layout.AbsoluteLayout;
import ui.layout.GridLayout;
import util.Engine;

import static graphics.Graphics.setDefaultBackground;

public class UIRenderingDemo extends Scene {

    Font openSans;
    Text movingText;

    RenderableElement e;


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
//        movingText = new Text("HAHA", openSans, Color.BLACK, 200, 160);

        menu = new Layer(0, 0, Window.getWidth(), Window.getHeight());
        Container container = new Container(100, 100, 500, 400, new GridLayout(10, 10));
        menu.registerComponent(container);

//        e = new Button("Button", "src/assets/images/button-300-84.png", Color.WHITE, new Frame(10, 10, 300, 84));
//        e.tintColor = Color.RED.toNormalizedVec4f();

//        container.addElement(e);
//        uiRenderer.add(e);
//        addUIElement(e);


        for (int i = 0; i < 200; i ++) {
            // Note how peppers that can't be fit keep default Frame
            RenderableElement f = new RenderableElement("src/assets/images/pepper.png", new Frame(0, 0, 10, 10));
            container.addElement(f);
            uiRenderer.add(f);
            addUIElement(f);
        }



    }

    public void update () {
        menu.update();
    }

}
