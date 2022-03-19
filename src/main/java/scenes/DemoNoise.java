package scenes;

import audio.AudioListener;
import audio.AudioMaster;
import audio.AudioSource;
import ecs.*;
import event.EventData;
import event.EventListener;
import event.Events;
import graphics.Camera;
import graphics.Color;
import graphics.Texture;
import input.Keyboard;
import org.joml.Vector2f;
import physics.collision.Shapes;
import postprocess.BloomEffect;
import postprocess.PostProcessStep;
import scene.Scene;
import tiles.Spritesheet;
import tiles.Tilesystem;
import util.Assets;
import util.Engine;
import util.MathUtils;
import util.Transform;

import java.util.Arrays;

import static graphics.Graphics.setDefaultBackground;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

/**
 * This demo has the same stuff as <code>DemoTopDown.java</code>, but whenever you collide with
 * the booper, it plays a noise.
 */
public class DemoNoise extends Scene {
    public static void main(String[] args) {
        Engine.init(1080, 720, "Azurite Engine Demo 1", 0.01f);
        Engine.scenes().switchScene(new DemoNoise(), true);
        Engine.showWindow();
    }

    Spritesheet a;
    Spritesheet b;
    Tilesystem t;
    PointLight booperLight;
    GameObject player;
    GameObject booper;
    GameObject greenLight;
    GameObject trRes;
    BloomEffect bloom;

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
        player.addComponent(AudioListener.get());
        player.addComponent(new AudioSource(Assets.getAudioBuffer("src/assets/sounds/test1.wav")));

        greenLight = new GameObject("Green light", new Vector2f(3315, 300), 3);
        greenLight.addComponent(new PointLight(new Color(102, 255, 102), 30));

        bloom = new BloomEffect(PostProcessStep.Target.DEFAULT_FRAMEBUFFER);
    }

    boolean prev;
    boolean curr;
    public void update() {
        super.update();

        player.getComponent(PointLight.class).intensity = MathUtils.map((float) Math.sin(Engine.millisRunning() / 600), -1, 1, 100, 140);
        if (booper.getComponent(PointLight.class) != null)
            booper.getComponent(PointLight.class).intensity = MathUtils.map((float) Math.cos(Engine.millisRunning() / 600), -1, 1, 70, 110);
        greenLight.getComponent(PointLight.class).intensity = MathUtils.map((float) Math.cos(Engine.millisRunning() / 600), -1, 1, 70, 110);

        //this is not clean:
        //player.getRawTransform().addRotation(1);

        camera.smoothFollow(player.getReadOnlyPosition());
        prev = Keyboard.getKey(GLFW_KEY_SPACE);
        if (prev != curr && prev) player.getComponent(AudioSource.class).play(0, true);
        curr = prev;
    }

    @Override
    public void postProcess(Texture texture) {
        bloom.apply(texture);
    }
}
