package scenes;

import ecs.*;
import graphics.Camera;
import graphics.Color;
import graphics.Texture;
import io.JSONInstance;
import io.JSONParser;
import io.SerializationTest;
import physics.AABB;
import physics.Gravity;
import physics.Transform;
import postprocess.BloomEffect;
import postprocess.PostProcessStep;
import scene.Scene;
import tiles.Spritesheet;
import tiles.TilesystemSideScroll;
import util.Assets;
import util.Engine;
import util.Logger;
import util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static graphics.Graphics.setDefaultBackground;

public class DemoPlatformer extends Scene {
    public static void main(String[] args) {
        Engine.init(1366, 768, "Azurite Engine Demo 2");
        Engine.scenes().switchScene(new DemoPlatformer(), true);
        Engine.showWindow();
    }

    BloomEffect bloom;

    Spritesheet a;
    Spritesheet c;
    TilesystemSideScroll t;
    GameObject player;
    GameObject booper;

    public void awake() {
        camera = new Camera();
        setDefaultBackground(new Color(3, 1, 4));

        a = new Spritesheet(Assets.getTexture("src/assets/images/tileset.png"), 16, 16, 256, 0);
        c = new Spritesheet(Assets.getTexture("src/assets/images/platformer.png"), 8, 8, 26, 0);
        t = new TilesystemSideScroll(this, c, 31, 15, 100, 100, player);

        player = new GameObject(this, "Player", new Transform(600, 600, 100, 100), 2);
        player.addComponent(new PointLight(new Color(250, 255, 181), 30));
        player.addComponent(new AABB());
        player.addComponent(new SpriteRenderer(a.getSprite(132)));
        player.addComponent(new CharacterControllerGravity());

        booper = new GameObject(this, "Booper", new Transform(800, 800, 100, 100), 2);
        booper.addComponent(new SpriteRenderer(a.getSprite(150)));
        booper.addComponent(new PointLight(new Color(255, 153, 102), 30));
        booper.addComponent(new AABB());
        booper.addComponent(new Gravity());

        bloom = new BloomEffect(PostProcessStep.Target.DEFAULT_FRAMEBUFFER);
        bloom.init();

        List<String> l = new ArrayList<>();
        l.add("lElement1");
        l.add("lElement2");
        l.add("lElement3");
        l.add("lElement4");
        l.add("lElement5");

        SerializationTest test = new SerializationTest(69, true, 'c', "sss", l);

        System.out.println(JSONParser.serialize(new JSONInstance(test)));
    }

    int r;
    int i = 1;

    public void update() {
        super.update();

        player.getComponent(PointLight.class).intensity = Utils.map((float) Math.sin(Engine.millisRunning() / 600), -1, 1, 80, 120);
        booper.getComponent(PointLight.class).intensity = Utils.map((float) Math.cos(Engine.millisRunning() / 600), -1, 1, 70, 110);

        camera.smoothFollow(player.getTransform());

        if (i % 400 == 0) {
            r = Utils.randomInt(-3, 3);
            Logger.logInfo("" + r);
        }

        if (r <= -1) {
            booper.getTransform().addX(-50 * Engine.deltaTime());
            if (booper.getComponent(AABB.class).isCollidingX()) {
                booper.getComponent(Gravity.class).addVelocityY(-20);
                Logger.logInfo("Do jump left");
            }
        }
        if (r >= 1) {
            booper.getTransform().addX(50 * Engine.deltaTime());
            if (booper.getComponent(AABB.class).isCollidingX()) {
                booper.getComponent(Gravity.class).addVelocityY(-20);
                Logger.logInfo("Do jump right");
            }
        }

        i++;
    }

    @Override
    public void postProcess(Texture texture) {
        bloom.apply(texture);
    }
}
