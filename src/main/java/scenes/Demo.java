package scenes;

import ecs.*;
import graphics.*;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;
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

    Spritesheet a;
    Spritesheet b;
    Tilesystem t;
    GameObject player;
    GameObject booper;
    GameObject greenLight;


    public static void main (String[] args) {
        Engine.init(1280, 720, "Azurite Engine Demo 1", 0.5f);
    }


    public void awake() {
        camera = new Camera();
        setDefaultBackground(Color.BLACK);

        a = new Spritesheet(Assets.getTexture("src/assets/images/tileset.png"), 16, 16, 256, 0);
        b = new Spritesheet(Assets.getTexture("src/assets/images/walls.png"), 16, 16, 256, 0);
        t = new Tilesystem(a, b, 31, 15, 200, 200);

        booper = new GameObject("Booper", new Transform(800, 200, 100, 100), 2);
        booper.addComponent(new SpriteRenderer(a.getSprite(150))); // a.getSprite(132)
        booper.addComponent(new AABB());
        booper.addComponent(new PointLight(new Color(255, 153, 102), 30));
        booper.addComponent(new Tween());

        player = new GameObject("Player", new Transform(600, 600, 100, 100), 2);
        player.addComponent(new PointLight(new Color(250, 255, 181), 30));
        player.addComponent(new AABB());
        player.addComponent(new SpriteRenderer(a.getSprite(132)));
        player.addComponent(new CharacterController());


        greenLight = new GameObject("Green light", new Transform(3315, 300, 1, 1), 3);
        greenLight.addComponent(new PointLight(new Color(102, 255, 102), 30));
    }

    private boolean startOnce = true;
    public void update() {
        super.update();
        player.getComponent(PointLight.class).intensity = Utils.map((float)Math.sin(Engine.millis()/600), -1, 1, 100, 140);
        booper.getComponent(PointLight.class).intensity = Utils.map((float)Math.cos(Engine.millis()/600), -1, 1, 70, 110);
        greenLight.getComponent(PointLight.class).intensity = Utils.map((float)Math.cos(Engine.millis()/600), -1, 1, 70, 110);

        camera.smoothFollow(player.getTransform());

        //Tween demo. You can remove the if statement for endless movement back and forth between these tweens.
        if(!booper.getComponent(Tween.class).tweenFinishedAll()) {
            booper.getComponent(Tween.class).setUpTween(booper.getTransform().position, booper.getTransform().position, new Vector2f(800, 600), 2, Tween.TweenMode.EASING_IN);
            booper.getComponent(Tween.class).setUpTween(booper.getTransform().position, new Vector2f(800, 600), booper.getTransform().position, 2, Tween.TweenMode.EASING_OUT);
            booper.getComponent(Tween.class).setUpTween(booper.getTransform().position, booper.getTransform().position, new Vector2f(800, 600), 1, Tween.TweenMode.NO_EASING);
            booper.getComponent(Tween.class).setUpTween(booper.getTransform().position, new Vector2f(800, 600), booper.getTransform().position, 1, Tween.TweenMode.EASING_IN_OUT);

            booper.getComponent(Tween.class).play();
        }

    }
}
