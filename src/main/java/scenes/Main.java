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
import fonts.Font;
import graphics.Camera;
import graphics.Color;
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

	public void awake() {

		setDefaultBackground(Color.BLACK);
		camera = new Camera();

		f = new Font("src/assets/fonts/OpenSans-Regular.ttf", 100, true);
		t = new Text("Hello", f, 100, 100, 1);

	}

	int x = 0;

	public void update () {
		if (x % 100 == 0) {
			t.change("" + Utils.randomInt(0, 10000));
		}
		x ++;
	}

}
