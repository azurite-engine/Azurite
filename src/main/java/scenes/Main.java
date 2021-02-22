package scenes;/*
 * 	Note when adding a new scene, remember to set it in Window.java

 * Javadoc style
 *
 * Method description if not obvious
 * @param if applicable
 * @return type and description if not void
 */

import ecs.GameObject;
import ecs.PointLight;
import ecs.SpriteRenderer;
import graphics.Camera;
import org.joml.Vector3f;
import physics.Transform;
import util.Engine;


import static graphics.Graphics.background;

public class Main extends util.Scene {
		
	public static void main (String[] args) {
		Engine.init(1080, 720, "Hello World!");
	}

	GameObject pepper = new GameObject("Pepper", new Transform(0, 0, 1080, 720), 10);
	GameObject light1 = new GameObject("Light", new Transform(460, 360, 50, 50), 10);
	GameObject light2 = new GameObject("Light", new Transform(620, 360, 50, 50), 10);

	public void awake() {
		camera = new Camera();
		pepper.addComponent(new SpriteRenderer("src/assets/images/pepper.png"));
		light1.addComponent(new PointLight(new Vector3f(0.8f, 0.2f, 0.3f), 10));
		light2.addComponent(new PointLight(new Vector3f(0.2f, 0.3f, 0.8f), 10));
	}

	public void update() {
		background(50, 50, 50);
	}
}
