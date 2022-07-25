package scenes;

import ecs.*;
import ui.fonts.Font;
import graphics.Camera;
import graphics.Color;
import graphics.Texture;
import org.joml.Vector2f;
import physics.collision.Shapes;
import physics.force.ConstantForce;
import graphics.postprocess.BloomEffect;
import graphics.postprocess.PostProcessStep;
import scene.Scene;
import graphics.Spritesheet;
import tiles.TilesystemSideScroll;
import ui.Text;
import util.Assets;
import util.Engine;
import util.MathUtils;

import static graphics.Graphics.setDefaultBackground;

@Deprecated
public class DemoPlatformer extends Scene {
    BloomEffect bloom;
    Spritesheet a;
    Spritesheet c;
    TilesystemSideScroll t;
    GameObject player;
    GameObject booper;

    Text text;
    Font f;

    public static void main(String[] args) {
        Engine.init(1920, 1080, "Azurite Engine Demo 2", 0.4f);
        Engine.scenes().switchScene(new DemoPlatformer());
        Engine.showWindow();
    }

    public void awake() {

        f = new Font("src/assets/fonts/OpenSans-Regular.ttf", 18, true);
        text = new Text("Azurite Engine demo", f, Color.WHITE, 15, 5, 100, true, false);

        camera = new Camera();
        setDefaultBackground(new Color(41, 30, 49));

        a = new Spritesheet(Assets.getTexture("src/assets/images/tileset.png"), 16, 16, 256, 0);
        c = new Spritesheet(Assets.getTexture("src/assets/images/platformer.png"), 8, 8, 26, 0);
        t = new TilesystemSideScroll(c, 31, 15, 100, 100, player, new int[]{2});

        player = new GameObject("Player", new Vector2f(600, 600), 2);
        PolygonCollider playerBody = new PolygonCollider(Shapes.axisAlignedRectangle(0, 0, 100, 100)).layer(2).mask(2);
        Dynamics playerDynamics = new Dynamics();
        playerDynamics.applyForce(new ConstantForce("Gravity", new Vector2f(0, 2)));
        player.addComponent(playerDynamics);
        player.addComponent(playerBody);
        player.addComponent(CollisionHandlers.unpassablePolygonCollider(playerBody));
        player.addComponent(new SpriteRenderer(a.getSprite(132), new Vector2f(100)));
        player.addComponent(CharacterController.standardPlatformer(playerDynamics, 5));
        player.addComponent(new PointLight(new Color(250, 255, 181), 30));

        booper = new GameObject("Booper", new Vector2f(800, 800), 2);
        booper.addComponent(new SpriteRenderer(a.getSprite(150), new Vector2f(100)));
        booper.addComponent(new PointLight(new Color(255, 153, 102), 30));
        PolygonCollider collider = new PolygonCollider(Shapes.axisAlignedRectangle(0, 0, 100, 100)).mask(2);
        Dynamics dynamicsBooper = new Dynamics();
        dynamicsBooper.applyForce(new ConstantForce("Gravity", new Vector2f(0, 2)));
        booper.addComponent(collider);
        booper.addComponent(CollisionHandlers.unpassablePolygonCollider(collider));
        booper.addComponent(dynamicsBooper);


        bloom = new BloomEffect(PostProcessStep.Target.DEFAULT_FRAMEBUFFER);
        bloom.init();
    }

    public void update() {
        super.update();
        player.getComponent(PointLight.class).intensity = MathUtils.map((float) Math.sin(Engine.millisRunning() / 600), -1, 1, 80, 120);
        booper.getComponent(PointLight.class).intensity = MathUtils.map((float) Math.cos(Engine.millisRunning() / 600), -1, 1, 70, 110);

        text.change("Azurite Engine demo\nDT: " + Engine.deltaTime() + "\nFPS: " + (int) Engine.getInstance().getWindow().getFPS());

        // camera.smoothFollow(player.getRawTransform());
        camera.smoothFollow(player.getReadOnlyPosition());

    }

    @Override
    public void postProcess(Texture texture) {
        bloom.apply(texture);
    }
}
