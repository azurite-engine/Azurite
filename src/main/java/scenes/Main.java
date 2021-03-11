package scenes;

/*
 * 	Note when adding a new scene, remember to set it in Window.java
 * Javadoc style
 *
 * Method description if not obvious
 * @param if applicable
 * @return type and description if not void
 */
import fonts.AFont;
import graphics.Camera;
import graphics.Color;
import testFonts.Font;
import util.Engine;

import static graphics.Graphics.setDefaultBackground;

public class Main extends util.Scene {

	public static void main (String[] args) {
		Engine.init(1920, 1080, "Hello World!", 1);
	}

	Font f;

	public void awake() {
		setDefaultBackground(Color.BLACK);
		camera = new Camera();

		f = new Font(new java.awt.Font("src/assets/fonts/OpenSans-Regular.ttf", java.awt.Font.PLAIN, 16), true);
	}

	public void update () {

	}

}
