package scenes;

import ecs.*;
import graphics.*;
import physics.AABB;
import tiles.Spritesheet;
import tiles.Tilesystem;
import physics.Transform;
import util.Assets;
import util.Engine;
import util.Scene;
import util.Utils;

import static graphics.Graphics.setDefaultBackground;

public class Demo extends Scene {
    public static void main (String[] args) {
        Engine.init(1080, 720, "Azurite Engine Demo 1", 0.1f);
    }

    Spritesheet a;
    Spritesheet b;
    Tilesystem t;
    GameObject player;
    GameObject booper;
    GameObject greenLight;

    public void awake() {
        camera = new Camera();
        setDefaultBackground(0);

        a = new Spritesheet(Assets.getTexture("src/assets/images/tileset.png"), 16, 16, 256, 0);
        b = new Spritesheet(Assets.getTexture("src/assets/images/walls.png"), 16, 16, 256, 0);
        t = new Tilesystem(a, b, 31, 15, 200, 200);

        booper = new GameObject("Booper", new Transform(800, 800, 100, 100), 2);
        booper.addComponent(new Animation(1, a.getSprite(132), a.getSprite(150)));
        booper.addComponent(new CollisionTrigger(data -> System.out.println("Boop")));
        booper.addComponent(new PointLight(new Color(255, 153, 102), 30));

        player = new GameObject("Player", new Transform(600, 600, 100, 100), 2);
        player.addComponent(new PointLight(new Color(250, 255, 181), 30));
        player.addComponent(new AABB());
        player.addComponent(new SpriteRenderer(a.getSprite(132)));
        player.addComponent(new CharacterController());

        greenLight = new GameObject("Green light", new Transform(3315, 300, 1, 1), 3);
        greenLight.addComponent(new PointLight(new Color(102, 255, 102), 30));
    }

    public void update() {
        player.getComponent(PointLight.class).intensity = Utils.map((float)Math.sin(Engine.millis()/600), -1, 1, 100, 140);
        booper.getComponent(PointLight.class).intensity = Utils.map((float)Math.cos(Engine.millis()/600), -1, 1, 70, 110);
        greenLight.getComponent(PointLight.class).intensity = Utils.map((float)Math.cos(Engine.millis()/600), -1, 1, 70, 110);

        camera.smoothFollow(player.getTransform());
    }
}
