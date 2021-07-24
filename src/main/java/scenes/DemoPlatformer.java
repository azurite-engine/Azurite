package scenes;

import ecs.*;
import graphics.Camera;
import graphics.Color;
import graphics.Texture;
import io.JSONInstance;
import io.JSONParser;
import io.jsonfields.JSONArray;
import io.jsonfields.JSONObject;
import io.jsonfields.jsonprimitives.*;
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

        JSONInstance jsonInstance = new JSONInstance();
        jsonInstance.addField(new JSONString("test1", "testValue"));
        jsonInstance.addField(new JSONInteger("test2", 69));
        jsonInstance.addField(new JSONFloat("test3", 69.420f));
        jsonInstance.addField(new JSONChar("test4", 'a'));
        jsonInstance.addField(new JSONBoolean("test5", true));

        JSONObject object = new JSONObject("test6");
        object.addField(new JSONChar("objectTest1", 's'));
        object.addField(new JSONInteger("objectTest2", 123));
        object.addField(new JSONBoolean("objectTest3", true));
        object.addField(new JSONLong("objectTest4", 123456789L));
        object.addField(new JSONString("objectTest5", "testValue1"));

        jsonInstance.addField(object);

        JSONInstance jsonInstance1 = new JSONInstance(player);

        JSONParser.setPrettyPrint(true);
        System.out.println(JSONParser.parse(jsonInstance1));
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
