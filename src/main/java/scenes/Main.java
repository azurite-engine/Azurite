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
		t = new Text("Hello World! (String 1)\nTest Text line 2.", f, 10, 0, 1);
//		t2 = new Text("Hello World! (String 2)\nTest Text line.", f, 400, 0, 1);

		g = new GameObject(this, new Transform(100, 100, 100, 100), 1).addComponent(new SpriteRenderer(Color.RED));

	}

	int x = 0;

	public void update () {
		if (x % 100 == 0) {
			t.change("" + Utils.randomInt(0, 400) + ":" + Utils.randomInt(1, 100));
//			t2.change("" + Utils.randomInt(0, 400) + ":" + Utils.randomInt(1, 100));
		}

//		if (x % 100 == 0) {
//			t.change("" + Utils.randomInt(0, 400) + ":" + Utils.randomInt(1, 100));
//		} else if (x % 99 == 0) {
//			t2.change("" + Utils.randomInt(0, 400) + ":" + Utils.randomInt(1, 100));
//		}
		x ++;
	}

}
