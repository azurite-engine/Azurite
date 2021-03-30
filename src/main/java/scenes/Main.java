package scenes;

/*
 * 	Note when adding a new scene, remember to set it in Window.java
 * Javadoc style
 *
 * Method description if not obvious
 * @param if applicable
 * @return type and description if not void
 */

import ecs.Text;
import graphics.Camera;
import graphics.Color;
import util.Engine;

import static graphics.Graphics.setDefaultBackground;

public class Main extends util.Scene {

	public static void main (String[] args) {
		Engine.init(1600, 900, "Hello World!", 1);
	}

	Text t;

	public void awake() {

		setDefaultBackground(Color.BLACK);
		camera = new Camera();

		t = new Text("Hello World!\nLorem Ipsum set dolar amet...", 100, 100);
		t.draw();
		textRenderer.add(t);

	}

	public void update () {

	}

}
