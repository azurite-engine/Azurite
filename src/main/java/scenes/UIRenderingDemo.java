package scenes;

import ecs.GameObject;
import ecs.SpriteRenderer;
import graphics.*;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import scene.Scene;
import ui.EventHandler;
import ui.Frame;
import ui.Layer;
import ui.Text;
import ui.element.Button;
import ui.element.CheckBox;
import ui.element.CheckBoxGroup;
import util.Engine;
import util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static graphics.Graphics.setDefaultBackground;

public class UIRenderingDemo extends Scene {

    public static void main(String[] args) {
        Log.setLogLevel(Log.ALL);
        Engine.init(900, 600, "Azurite UI Rendering Demo", 1, true);
        Engine.scenes().switchScene(new UIRenderingDemo());
        Engine.showWindow();
    }

    GameObject background;
    Spritesheet uiSprites;

    Layer menu;
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


//        description = new Text("Hello World!", 200, 200);

        int index = 0;
        for (Sprite i : uiSprites.getSprites()) {
            GameObject g = new GameObject(new Vector2f(index * size + 30, 10)).addComponent(new SpriteRenderer(i, new Vector2f(size, size)));
            index++;
        }

//        menu = new Layer(0, 0, Window.getWidth(), Window.getHeight());
//        Container container = new Container(100, 100, 500, 400, new AbsoluteLayout());
//        menu.registerComponent(container);

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

        button.getEventHandler().registerListener(EventHandler.Event.MOUSE_CLICK, new Consumer<EventHandler>() {
            int amount = 0;

            @Override
            public void accept(EventHandler eventHandler) {
                if (eventHandler.isMouseButtonClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                    button.setText("Clicked " + (amount++) + " times!");
                }
            }
        });

        uiRenderer.add(button);
        addUIElement(button);

        button.getEventHandler().registerListener(EventHandler.Event.MOUSE_CLICK, e -> {
            Log.p("CLICKED! \n" + e.getElement().toString());
        });
    }

    public void update() {
        background.getComponent(SpriteRenderer.class).setSize(new Vector2f(Window.getWidth(), Window.getHeight()));

        String s = "";
        for (String i : radios.getSelected()) {
            s += i + ", ";
        }
        //Log.p(s);

        for (String i : checks.getSelected()) {
            s += i + ", ";
        }
        //Log.p(s);

    }
}
