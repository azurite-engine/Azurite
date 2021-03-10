package scenes;

/*
 * 	Note when adding a new scene, remember to set it in Window.java
 * Javadoc style
 *
 * Method description if not obvious
 * @param if applicable
 * @return type and description if not void
 */
import ecs.GameObject;
import ecs.PointLight;
import ecs.Sprite;
import ecs.SpriteRenderer;
import graphics.Camera;
import graphics.Color;
import graphics.Texture;
import input.Keyboard;
import input.Mouse;
import org.joml.Vector2f;
import physics.Transform;
import util.Engine;


import static graphics.Graphics.background;
import static graphics.Graphics.setDefaultBackground;

public class Main extends util.Scene {

	public static void main (String[] args) {
		Engine.init(1920, 1080, "Hello World!");
	}

//	GameObject box = new GameObject("Two", new Transform((1080/2)-(720/2), 0, 720, 720), 10);
//	GameObject pepper = new GameObject("Pepper", new Transform((1080/2)-(720/2), 0, 720, 720), 12);
	GameObject light1 = new GameObject();
	GameObject light2 = new GameObject();

	public void awake() {
		setDefaultBackground(Color.BLACK);
		camera = new Camera();

//		pepper.addComponent(new SpriteRenderer("src/assets/images/pepper.png"));
//		box.addComponent(new SpriteRenderer(new Color(150, 150,150, 255)));
		light1.addComponent(new PointLight(Color.CYAN, 100));
		light2.addComponent(new PointLight(Color.RED, 100));
	}

	public void update() {
		light1.getTransform().setPosition(new Vector2f(Mouse.mouseX, Mouse.mouseY));
	}

}
