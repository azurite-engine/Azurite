package scenes;

import ecs.*;
import graphics.Camera;
import graphics.Color;
import graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics.collision.Shapes;
import physics.force.ConstantForce;
import postprocess.BloomEffect;
import postprocess.PostProcessStep;
import scene.Scene;
import tiles.Spritesheet;
import tiles.TilesystemSideScroll;
import util.Assets;
import util.Engine;
import util.Utils;

import static graphics.Graphics.setDefaultBackground;

public class DemoPlatformer extends Scene {
    BloomEffect bloom;
    Spritesheet a;
    Spritesheet c;
    TilesystemSideScroll t;
    GameObject player;
    GameObject booper;

    public static void main(String[] args) {
        Engine.init(1920, 1080, "Azurite Engine Demo 2", 1.0f);
        Engine.scenes().switchScene(new DemoPlatformer(), true);
        Engine.showWindow();
    }

    public void awake() {
        camera = new Camera();
        setDefaultBackground(new Color(41, 30, 49));

        a = new Spritesheet(Assets.getTexture("src/assets/images/tileset.png"), 16, 16, 256, 0);
        c = new Spritesheet(Assets.getTexture("src/assets/images/platformer.png"), 8, 8, 26, 0);
        t = new TilesystemSideScroll(this, c, 31, 15, 100, 100, player);

<<<<<<< HEAD
        player = new GameObject(this, "Player", new Transform(600, 600, 100, 100), 2)
                .addComponent(new PointLight(new Color(250, 255, 181), 30))
                .addComponent(new AABB())
                .addComponent(new SpriteRenderer(a.getSprite(132)))
                .addComponent(new CharacterControllerGravity());

        booper = new GameObject(this, "Booper", new Transform(800, 800, 100, 100), 2)
                .addComponent(new SpriteRenderer(a.getSprite(150)))
                .addComponent(new PointLight(new Color(255, 153, 102), 30))
                .addComponent(new AABB())
                .addComponent(new Gravity());
=======
        player = new GameObject(this, "Player", new Vector3f(600, 600, 0), 2);
        player.addComponent(new PointLight(new Color(250, 255, 181), 30));
        //player.addComponent(new AABB());
        RigidBody playerBody = new RigidBody(Shapes.axisAlignedRectangle(0, 0, 100, 100), 1);
        playerBody.setMask(2, true);
        playerBody.applyForce(new ConstantForce("Gravity", new Vector2f(0, 0.010f)));
        player.addComponent(playerBody);
        player.addComponent(new SpriteRenderer(a.getSprite(132), new Vector2f(100)));
        player.addComponent(new CharacterController(CharacterController.standardPlatformer(playerBody), 1));
        player.getRawLocation().z = 90;

        booper = new GameObject(this, "Booper", new Vector3f(800, 800, 0), 2);
        booper.addComponent(new SpriteRenderer(a.getSprite(150), new Vector2f(100)));
        booper.addComponent(new PointLight(new Color(255, 153, 102), 30));
        //TODO not done yet

        RigidBody rigidBody = new RigidBody(Shapes.axisAlignedRectangle(0, 0, 100, 100), 2);
        rigidBody.applyForce(new ConstantForce("Gravity", new Vector2f(0, 0.005f)));
        rigidBody.setMask(2, true);
        booper.addComponent(rigidBody);

>>>>>>> 43c35baf2f4edb22cdcedb78c37d8caa77366450

        bloom = new BloomEffect(PostProcessStep.Target.DEFAULT_FRAMEBUFFER);
        bloom.init();

    }

    public void update() {
        super.update();
        player.getComponent(PointLight.class).intensity = Utils.map((float) Math.sin(Engine.millisRunning() / 600), -1, 1, 80, 120);
        booper.getComponent(PointLight.class).intensity = Utils.map((float) Math.cos(Engine.millisRunning() / 600), -1, 1, 70, 110);

        camera.smoothFollow(player.getReadOnlyLocation());

    }

    @Override
    public void postProcess(Texture texture) {
        bloom.apply(texture);
    }
}
