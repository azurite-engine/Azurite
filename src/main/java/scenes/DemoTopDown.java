package scenes;

import ecs.*;
import graphics.Camera;
import graphics.Color;
import graphics.Texture;
import input.Keyboard;
import input.Keys;
import org.joml.Vector2f;
import physics.AABB;
import physics.Transform;
import postprocess.BloomEffect;
import postprocess.PostProcessStep;
import scene.Scene;
import tiles.Spritesheet;
import tiles.Tilesystem;
import util.Assets;
import util.Engine;
import util.Utils;

import static graphics.Graphics.setDefaultBackground;

public class DemoTopDown extends Scene {
	public static void main(String[] args) {
		Engine.init(1080, 720, "Azurite Engine Demo 1", 0.01f);
		Engine.scenes().switchScene(new DemoTopDown(), true);
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

	boolean flip = true;

	public void awake() {
		camera = new Camera();
		setDefaultBackground(0);

		a = new Spritesheet(Assets.getTexture("src/assets/images/tileset.png"), 16, 16, 256, 0);
		b = new Spritesheet(Assets.getTexture("src/assets/images/walls.png"), 16, 16, 256, 0);
		t = new Tilesystem(this, a, b, 31, 15, 200, 200);

		trRes = new GameObject(this, "", new Transform(new Vector2f(0, 0), new Vector2f(100)), -20);

		booper = new GameObject(this, "Booper", new Transform(800, 800, 100, 100), 2);
		booper.addComponent(new Animation(1, a.getSprite(132), a.getSprite(150)));
		booper.addComponent(new CollisionTrigger(data -> System.out.println("Boop")));
		booperLight = new PointLight(new Color(255, 153, 102), 30);
		booper.addComponent(booperLight);
		booper.setTransformY(900);

		player = new GameObject(this, "Player", new Transform(600, 600, (float)Math.toRadians(90), 100, 100), 2);
		player.addComponent(new PointLight(new Color(250, 255, 181), 30));
		player.addComponent(new AABB());
		player.addComponent(new SpriteRenderer(a.getSprite(132)));
		player.addComponent(new CharacterController());

		greenLight = new GameObject(this, "Green light", new Transform(3315, 300, 1, 1), 3);
		greenLight.addComponent(new PointLight(new Color(102, 255, 102), 30));

		bloom = new BloomEffect(PostProcessStep.Target.DEFAULT_FRAMEBUFFER);
	}

	public void update() {
		super.update();
		player.getComponent(PointLight.class).intensity = Utils.map((float) Math.sin(Engine.millisRunning() / 600), -1, 1, 100, 140);
		if (booper.getComponent(PointLight.class) != null)
			booper.getComponent(PointLight.class).intensity = Utils.map((float) Math.cos(Engine.millisRunning() / 600), -1, 1, 70, 110);
		greenLight.getComponent(PointLight.class).intensity = Utils.map((float) Math.cos(Engine.millisRunning() / 600), -1, 1, 70, 110);

		player.getTransform().addRotation((float) Math.toRadians(1));

		if (Keyboard.getKeyDown(Keys.AZ_KEY_SPACE)) {
//			if (flip) {
//				booper.removeComponent(PointLight.class);
//				flip = false;
//			} else {
//				booper.addComponent(booperLight);
//				flip = true;
//			}

//			if (flip) {
//				removeGameObjectFromScene(booper);
//				flip = false;
//			} else {
//				addGameObjectToScene(booper);
//				flip = true;
//			}

			if (flip) {
				booper.setZindex(1);
				flip = false;
			} else {
				booper.setZindex(2);
				flip = true;
			}
		}

		camera.smoothFollow(player.getTransform());
	}

	@Override
	public void postProcess(Texture texture) {
		bloom.apply(texture);
	}
}
