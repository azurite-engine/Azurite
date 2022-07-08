package scenes;

import graphics.Camera;
import graphics.Color;
import scene.Scene;
import ui.Text;
import ui.UIXML;
import ui.fonts.Font;
import util.Engine;

import static graphics.Graphics.setDefaultBackground;

public class TestingXML extends Scene {
    public static void main(String[] args) {
        Engine.init(1080, 680, "UIXML");
        Engine.scenes().switchScene(new TestingXML(), true);
        Engine.showWindow();
    }

    Font openSans;
    Text text;

    UIXML ui;

    public void awake () {

        camera = new Camera();
        setDefaultBackground(40, 40, 40);

        ui = new UIXML("src/assets/data/test.xml");

        openSans = new Font("src/assets/fonts/OpenSans-Regular.ttf", 20, true);
        text = new Text(ui.getXml(), openSans, Color.WHITE, 10, 10);
    }

    public void update () {

    }
}
