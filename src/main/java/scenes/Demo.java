package scenes;

import ecs.GameObject;
import ecs.PointLight;
import ecs.Sprite;
import ecs.SpriteRenderer;
import graphics.Camera;
import graphics.Color;
import graphics.Spritesheet;
import graphics.Window;
import input.Mouse;
import misc.Tilesystem;
import org.joml.Vector2f;
import physics.Transform;
import util.Assets;
import util.Engine;
import util.Scene;
import util.Utils;

import static graphics.Graphics.setDefaultBackground;

public class Demo extends Scene {
    public static void main (String[] args) {
        Engine.init(1920, 1080, "Azurite Engine Demo 1", 0.1f);
    }

    Spritesheet s;
    Tilesystem t;
    GameObject light;

    public void awake() {
        setDefaultBackground(Color.BLACK);
        camera = new Camera();
        s = new Spritesheet(Assets.getTexture("src/assets/images/tileset.png"), 16, 16, 26, 0);
        t = new Tilesystem(s, 34, 30, Window.getWidth()/32, Window.getHeight()/18);

        light = new GameObject(new Transform(Window.getWidth()/2, Window.getHeight()/2, 1, 1), 2).addComponent(new PointLight(new Color(245, 255, 98), 30));
    }

    public void update() {
        light.setTransformX(Mouse.mouseX);
        light.setTransformY(Mouse.mouseY);
        light.getComponent(PointLight.class).intensity = Utils.map((float)Math.sin(Engine.millis()/600), -1, 1, 10, 20);
    }
}
