package scenes;

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

/**
 *
 * font rendering todo:
 * hot swap sticky rendering?
 * colors and hot swappable colors
 * clean up code
 * fix debug rendering order
 * speed improvements
 */

public class Main extends scene.Scene {

	public static void main (String[] args) {
		Engine.init(1000, 400, "Hello World!", 1);
		Engine.scenes().switchScene(new Main(), true);
		Engine.showWindow();
	}

	Text t;
	Text t2;
	Font f;
	Font f2;

	public void awake() {
		setDefaultBackground(Color.BLACK);
		camera = new Camera();

		f = new Font("src/assets/fonts/OpenSans-Regular.ttf", 25, true);
		f2 = new Font("src/assets/fonts/Maghrib-MVZpx.ttf", 40, true);
		t = new Text("Hello World!", f, 10, 10, 1, false);
		t2 = new Text("Other font", f2, 10, 40, 1, false);
	}

	public void update () {

	}

}