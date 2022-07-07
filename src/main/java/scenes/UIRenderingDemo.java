package scenes;

import ecs.GameObject;
import ecs.SpriteRenderer;
import graphics.*;
import org.joml.Vector2f;
import scene.Scene;
import graphics.Spritesheet;
import ui.*;
import ui.element.Button;
import ui.element.CheckBox;
import ui.element.CheckBoxGroup;
import util.Engine;
import util.Logger;

import java.util.ArrayList;
import java.util.List;

import static graphics.Graphics.setDefaultBackground;

public class UIRenderingDemo extends Scene {

    public static void main(String[] args) {
        Engine.init(900, 600, "Azurite UI Rendering Demo");
        Engine.scenes().switchScene(new UIRenderingDemo(), true);
        Engine.showWindow();
    }

    GameObject background;
    Spritesheet uiSprites;

    CheckBoxGroup radios;
    CheckBoxGroup checks;

    Text description;

    Button button;

    public void awake() {

        camera = new Camera();
        setDefaultBackground(Color.WHITE);

        background = new GameObject(new Vector2f(0, 0));
        background.addComponent(new SpriteRenderer("src/assets/images/paper background.png", new Vector2f(Window.getWidth(), Window.getHeight())));
        int size = 36;
        uiSprites = new Spritesheet(new Texture("src/assets/images/radio-checks.png"), size, size, 12, 0);

        List<String> radioOptions = new ArrayList<>();
        radioOptions.add("Potion?");
        radioOptions.add("Option??");
        radioOptions.add("Woah!");

        radios = new CheckBoxGroup(CheckBox.Type.RADIO_SELECT, radioOptions, uiSprites.getSprite(0), uiSprites.getSprite(6), new Vector2f(30, 50));

        List<String> checkOptions = new ArrayList<>();
        checkOptions.add("A checkbox :O");
        checkOptions.add("So many choices...");
        checkOptions.add("You can select any of them!");

        checks = new CheckBoxGroup(CheckBox.Type.MULTI_SELECT, checkOptions, uiSprites.getSprite(3), uiSprites.getSprite(9), new Vector2f(30, 170));


        button = new Button("Button", "src/assets/images/Button-282-53.png", Color.WHITE, new Frame(30, 300, 282, 53));
        button.tintColor = new Color(200, 200, 200, 255).toNormalizedVec4f();

        uiRenderer.add(button);
        addUIElement(button);

        button.getEventHandler().registerListener(EventHandler.Event.MOUSE_CLICK, e -> {
            Logger.logInfo("CLICKED! \n" + e.getElement().toString());
        });
    }

    public void update () {
        String s = "";
        for (String i : radios.getSelected()) {
            s += i + ", ";
        }
        Logger.logInfo(s);

        for (String i : checks.getSelected()) {
            s += i + ", ";
        }
        Logger.debugLog(s);

    }
}
