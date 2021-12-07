package scenes;

import ecs.*;
import graphics.Camera;
import graphics.Color;
import graphics.Texture;
import input.Keyboard;
import input.Keys;
import org.joml.Vector2f;
import physics.collision.Shapes;
import postprocess.BloomEffect;
import postprocess.PostProcessStep;
import scene.Scene;
import tiles.Spritesheet;
import tiles.Tilesystem;
import util.Assets;
import util.Engine;
import util.Utils;

import java.util.Arrays;

import static graphics.Graphics.setDefaultBackground;

public class DemoTopDown extends Scene {

    Spritesheet a;
    Spritesheet b;
    Tilesystem t;
    PointLight booperLight;
    GameObject player;
    GameObject booper;
    GameObject greenLight;
    GameObject trRes;
    BloomEffect bloom;
    boolean flip = true;

    public static void main(String[] args) {
        Engine.init(1080, 720, "Azurite Engine Demo 1", 0.01f);
        Engine.scenes().switchScene(new DemoTopDown(), true);
        Engine.showWindow();
    }

    public void awake() {
        camera = new Camera();
        setDefaultBackground(0);

        a = new Spritesheet(Assets.getTexture("src/assets/images/tileset.png"), 16, 16, 256, 0);
        b = new Spritesheet(Assets.getTexture("src/assets/images/walls.png"), 16, 16, 256, 0);
        t = new Tilesystem(a, b, 31, 15, 200, 200, new int[]{2});

        trRes = new GameObject("", new Vector2f(0, 0), -20); //scale 100 for no image remove

        //BOOPER
        booper = new GameObject("Booper", new Vector2f(800, 800), 2);
        booperLight = new PointLight(new Color(255, 153, 102), 30);
        booper.addComponent(booperLight);
        SpriteRenderer booperRenderer = new SpriteRenderer(a.getSprite(132), new Vector2f(100));
        SpriteAnimation booperAnimation = new SpriteAnimation(booperRenderer, a.getSprite(132), 1);
        booperAnimation.setAnimation("idle", Arrays.asList(a.getSprite(132), a.getSprite(150)));
        booperAnimation.nextAnimation("idle", -1);
        booper.addComponent(booperRenderer);
        booper.addComponent(booperAnimation);
        PolygonCollider booperBody = new PolygonCollider(Shapes.axisAlignedRectangle(0, 0, 100, 100)).mask(2);
        booper.addComponent(booperBody);


        //PLAYER
        player = new GameObject("Player", new Vector2f(600, 600), 2);
        player.addComponent(new PointLight(new Color(250, 255, 181), 30));
        PolygonCollider playerBody = new PolygonCollider(Shapes.axisAlignedRectangle(0, 0, 100, 100)).layer(2).mask(2);
        player.addComponent(playerBody);
        player.addComponent(CollisionHandlers.unpassablePolygonCollider(playerBody));
        player.addComponent(new SpriteRenderer(a.getSprite(132), new Vector2f(100)));
        Dynamics dynamics = new Dynamics();
        player.addComponent(dynamics);
        player.addComponent(CharacterController.standardTopDown(dynamics, 5));

        greenLight = new GameObject("Green light", new Vector2f(3315, 300), 3);
        greenLight.addComponent(new PointLight(new Color(102, 255, 102), 30));

        bloom = new BloomEffect(PostProcessStep.Target.DEFAULT_FRAMEBUFFER);
    }

    public void update() {
        super.update();
        player.getComponent(PointLight.class).intensity = Utils.map((float) Math.sin(Engine.millisRunning() / 600), -1, 1, 100, 140);
        if (booper.getComponent(PointLight.class) != null)
            booper.getComponent(PointLight.class).intensity = Utils.map((float) Math.cos(Engine.millisRunning() / 600), -1, 1, 70, 110);
        greenLight.getComponent(PointLight.class).intensity = Utils.map((float) Math.cos(Engine.millisRunning() / 600), -1, 1, 70, 110);

        //this is not clean:
        //player.getRawTransform().addRotation(1);

        camera.smoothFollow(player.getReadOnlyPosition());
        if (Keyboard.getKeyDown(Keys.AZ_KEY_SPACE)) {
//            if (added) {
//                booper.removeComponent(PointLight.class);
//                added = false;
//            } else {
//                booper.addComponent(booperLight);
//                added = true;
//            }

//            if (flip) {
//				removeGameObjectFromScene(booper);
//				flip = false;
//			} else {
//				addGameObjectToScene(booper);
//				flip = true;
//			}

//			if (flip) {
//				booper.setZindex(1);
//				flip = false;
//			} else {
//				booper.setZindex(2);
//				flip = true;
//			}

        }

    }

    @Override
    public void postProcess(Texture texture) {
        bloom.apply(texture);
    }
}
