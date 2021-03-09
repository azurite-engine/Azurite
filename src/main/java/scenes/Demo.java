package scenes;

import ecs.*;
import graphics.*;
import graphics.renderer.LightmapRenderer;
import org.joml.Vector2f;
import physics.AABB;
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

    public void awake() {
        camera = new Camera();
        setDefaultBackground(Color.BLACK);

        a = new Spritesheet(Assets.getTexture("src/assets/images/tileset.png"), 16, 16, 256, 0, 16);
        b = new Spritesheet(Assets.getTexture("src/assets/images/walls.png"), 16, 16, 256, 0, 16);
        t = new Tilesystem(a, b, 31, 15, 200, 200);

        booper = new GameObject("Booper", new Transform(800, 800, 100, 100), 2);
        booper.addComponent(new SpriteRenderer(a.getSprite(132)));
        booper.addComponent(new AABB(new Vector2f(850, 850), 100, 100, false));

        player = new GameObject("Player", new Transform(600, 600, 100, 100), 2);
        player.addComponent(new PointLight(new Color(250, 255, 181), 30));
        player.addComponent(new AABB(new Vector2f(650, 650), 100, 100, true));
        player.addComponent(new SpriteRenderer(a.getSprite(132)));
        player.addComponent(new CharacterController());
    }

    public void update() {
        super.update();
        player.getComponent(PointLight.class).intensity = Utils.map((float)Math.sin(Engine.millis()/600), -1, 1, 100, 140);

        camera.smoothFollow(player.getTransform());
    }
}
