package scenes;/*
 * 	Note when adding a new scene, remember to set it in Window.java

 * Javadoc style
 *
 * @author Name
 * @param if applicable
 * @return type and description if not void
 */

import ecs.GameObject;
import ecs.SpriteRenderer;
import graphics.Camera;
import physics.Transform;
import util.Engine;


import static graphics.Graphics.background;

public class Main extends util.Scene {
		
	public static void main (String[] args) {
		Engine.init(1920, 1080, "Hello World!");
	}

	GameObject pepper = new GameObject("Pepper", new Transform(100, 100, 140, 140), 10);

	public void awake() {		
		camera = new Camera();
		pepper.addComponent(new SpriteRenderer("src/assets/images/pepper.png"));
	}

	public void update() {
		background(50, 50, 50);

	}
}
