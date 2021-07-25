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

	GameObject g;

	public void awake() {
		setDefaultBackground(Color.BLACK);
		camera = new Camera();

		f = new Font("src/assets/fonts/OpenSans-Regular.ttf", 25, true);
		t = new Text("Hello World! (String 1)\nTest Text line 2.", f, 10, 0, 1, false);

		g = new GameObject(this, new Transform(300, 10, 100, 100), 1).addComponent(new SpriteRenderer(Color.WHITE));
	}

	int x = 0;

	public void update () {
//		if (x % 100 == 0) {
//			t.change("" + Utils.randomInt(0, 400) + ":" + Utils.randomInt(1, 100));
//			t2.change("" + Utils.randomInt(0, 400) + ":" + Utils.randomInt(1, 100));
//		}

		t.addX(0.6f);

		x ++;
	}

}
