package util;

import graphics.Window;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class Engine {

	/**
	 * The Engine class initializes GLFW and the game loop.
	 */

	private static long startMillis = System.currentTimeMillis();
	public static Window w;
	public static boolean running = true;
	public static float deltaTime = 0;

	/**
	 * Start the engine, and initialize GLFW.
	 * @param windowWidth Width of the window to be created
	 * @param windowHeight Height of the window to be created
	 * @param windowTitle Title of the window to be created
	 * @param minSceneLighting float from 0-1 indicating the minimum scene light level
	 */
	public static void init(int windowWidth, int windowHeight, String windowTitle, float minSceneLighting) {

		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("[FATAL] Failed to initialize GLFW.");

		w = new Window(windowWidth, windowHeight, windowTitle, minSceneLighting, false);

		w.showWindow();
	}

	/**
	 * Start the engine, and initialize GLFW.
	 * @param windowWidth Width of the window to be created
	 * @param windowHeight Height of the window to be created
	 * @param windowTitle Title of the window to be created
	 */
	public static void init(int windowWidth, int windowHeight, String windowTitle) {

		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("[FATAL] Failed to initialize GLFW.");

		w = new Window(windowWidth, windowHeight, windowTitle, false);

		w.showWindow();
	}

	/**
	 * Start the engine, and initialize GLFW. This will create a fullscreen window.
	 * @param windowTitle Title of the window to be created
	 * @param minSceneLighting float from 0-1 indicating the minimum scene light level
	 */
	public static void init(String windowTitle, float minSceneLighting) {

		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("[FATAL] Failed to initialize GLFW.");

		w = new Window(windowTitle, minSceneLighting, false);

		w.showWindow();
	}

	public static void init(int windowWidth, int windowHeight, String windowTitle, float minSceneLighting, boolean recalculateProjectionOnResize) {

		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("[FATAL] Failed to initialize GLFW.");

		w = new Window(windowWidth, windowHeight, windowTitle, minSceneLighting, recalculateProjectionOnResize);

		w.showWindow();
	}

	/**
	 * Start the engine, and initialize GLFW.
	 * @param windowTitle Title of the window to be created
	 */
	public static void init(String windowTitle) {

		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit())
			throw new IllegalStateException("[FATAL] Failed to initialize GLFW.");

		w = new Window(windowTitle);

		w.showWindow();
	}

	/**
	 * @return Returns the number of milliseconds since the engine started.
	 */
	public static double millis() {
		return System.currentTimeMillis() - startMillis;
	}

}
