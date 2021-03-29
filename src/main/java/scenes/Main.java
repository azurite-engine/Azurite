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
import fonts.Font;
import util.Engine;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static graphics.Graphics.setDefaultBackground;

public class Main extends util.Scene {

	public static void main (String[] args) {
		Engine.init(1600, 900, "Hello World!", 1);
	}

	Font f;
	Text t;

	public void awake() {

		setDefaultBackground(Color.BLACK);
		camera = new Camera();

		f = new Font("src/assets/fonts/Chango-Regular.ttf", 60, true);
		t = new Text("Hello World!\nLorem Ipsum set dolar amet...", f, 100, 100);
		t.draw();

	}

	public void update () {

	}

}
