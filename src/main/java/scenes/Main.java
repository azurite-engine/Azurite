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
import ecs.Text;
import graphics.Camera;
import graphics.Color;
import fonts.Font;
import physics.Transform;
import util.Engine;

import static graphics.Graphics.setDefaultBackground;

public class Main extends util.Scene {

	public static void main (String[] args) {
		Engine.init(1920, 1080, "Hello World!", 1);
	}

	Font f;
	Text t;

	public void awake() {
		super.update();
		setDefaultBackground(Color.BLACK);
		camera = new Camera();

		f = new Font(new java.awt.Font("src/assets/fonts/OpenSans-Regular.ttf", java.awt.Font.PLAIN, 16), true);

		t = new Text("Hello World!", f, 1, 1);
		t.draw();

	}

	public void update () {

	}

}
