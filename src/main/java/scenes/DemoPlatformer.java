package scenes;

import ecs.*;
import graphics.Camera;
import graphics.Color;
import org.joml.Vector2f;
import physics.AABB;
import physics.Gravity_old;
import physics.Transform;
import physics.collision.Shapes;
import physics.force.ConstantForce;
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
        Engine.init(900, 600, "Azurite Engine Demo 2", 1.0f);
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
        //TODO not done yet

        RigidBody rigidBody = new RigidBody(Shapes.axisAlignedRectangle(0, 0, 32, 32), 1);
        booper.addComponent(rigidBody);
        rigidBody.applyForce(new ConstantForce("Gravity", new Vector2f(0, 0.005f)));


        bloom = new BloomEffect(PostProcessStep.Target.DEFAULT_FRAMEBUFFER);
        bloom.init();

    }

    int r;
    int i = 1;
    boolean jumpBooper = false;

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
                booper.getComponent(Gravity_old.class).addVelocityY(-20);
                Logger.logInfo("Do jump left");
            }
        }
        if (r >= 1) {
            booper.getTransform().addX(50 * Engine.deltaTime());
            if (booper.getComponent(AABB.class).isCollidingX()) {
                booper.getComponent(Gravity_old.class).addVelocityY(-20);
                Logger.logInfo("Do jump right");
            }
        }

        i++;
    }

    @Override
    public void postProcess(int texture) {
        bloom.apply(texture);
    }
}
