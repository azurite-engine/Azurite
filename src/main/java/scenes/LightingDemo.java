package scenes;

import ecs.GameObject;
import ecs.PointLight;
import ecs.SpriteRenderer;
import graphics.Camera;
import graphics.Color;
import input.Mouse;
import org.joml.Vector2f;
import physics.Transform;
import util.Engine;
import util.Scene;

import java.util.ArrayList;
import java.util.List;

import static graphics.Graphics.setDefaultBackground;

public class LightingDemo extends Scene {
    public static void main (String[] args) {
        Engine.init(1080, 720, "Hello World!", 0.0f);
    }

    GameObject box = new GameObject("box", new Transform(0, 0, 1080, 720), 0);
    GameObject pepper = new GameObject("Pepper", new Transform((1080/2)-(720/2), (720/2)-(720/2), 720, 720), 1);

    ArrayList<GameObject> lights = new ArrayList<>();

    public void awake() {
        setDefaultBackground(Color.BLACK);
        camera = new Camera();

        pepper.addComponent(new SpriteRenderer("src/assets/images/pepper.png"));
        box.addComponent(new SpriteRenderer(Color.WHITE));
    }

    public void update() {
        if (Mouse.mouseButtonDown(0)) {
            lights.add(new GameObject(new Transform(Mouse.mouseX, Mouse.mouseY, 0, 0), 2));
            lights.get(lights.size()-1).addComponent(new PointLight(Color.randomColor(), 2));
//            light1.getTransform().setPosition(new Vector2f(Mouse.mouseX, Mouse.mouseY));
        }


    }

}
