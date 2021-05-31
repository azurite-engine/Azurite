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
import ecs.SpriteRenderer;
import ecs.Text;
import fonts.Font;
import graphics.Camera;
import graphics.Color;
import physics.Transform;
import util.Engine;
import util.Logger;
import util.Utils;

import static graphics.Graphics.setDefaultBackground;

public class Main extends util.Scene {

	public static void main (String[] args) {
		Engine.init(1000, 400, "Hello World!", 1);
	}

	Text t;
	Font f;

	GameObject g;

	public void awake() {

		setDefaultBackground(Color.BLACK);
		camera = new Camera();

		f = new Font("src/assets/fonts/OpenSans-Regular.ttf", 25, true);
		t = new Text("Hello World!\nTest Text line 2.", f, 10, 0, 1);

		g = new GameObject("test", new Transform(100, 100, 100, 100), 1);
		g.addComponent(new SpriteRenderer(Color.RED));

	}

	int x = 0;

	public void update () {
//		if (x % 100 == 0) {
//			t.change("" + Utils.randomInt(0, 400) + ":" + Utils.randomInt(1, 100));
//		}
//		x ++;
	}

}
