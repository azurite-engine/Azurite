package util;

import graphics.Window;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class Engine {

    /**
     * The Engine class initializes GLFW and the game loop.
     */

    private static Engine instance;

    private final long startMillis;
    private Window window;
    private boolean running;
    private float deltaTime;

    private Engine() {
        running = true;
        deltaTime = 0;
        startMillis = System.currentTimeMillis();
    }

    public static Engine getInstance() {
        if (instance == null) {
            synchronized (instance) {
                instance = new Engine();
            }
        }
        return instance;
    }

    public static float deltaTime() {
        return getInstance().getDeltaTime();
    }

    public static void updateDeltaTime(float deltaTime) {
        getInstance().deltaTime = deltaTime;
    }

    public static boolean isRunning() {
        return getInstance().running;
    }

    public static Window window() {
        return getInstance().getWindow();
    }

    public Window getWindow() {
        return window;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    //internal method called before any init
    private static void preInit() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("[FATAL] Failed to initialize GLFW.");
    }

    /**
     * Start the engine, and initialize GLFW.
     *
     * @param windowWidth      Width of the window to be created
     * @param windowHeight     Height of the window to be created
     * @param windowTitle      Title of the window to be created
     * @param minSceneLighting float from 0-1 indicating the minimum scene light level
     */
    public static void init(int windowWidth, int windowHeight, String windowTitle, float minSceneLighting) {

        preInit();
        getInstance().window = new Window(windowWidth, windowHeight, windowTitle, minSceneLighting, false);
        getInstance().window.showWindow();
    }

    /**
     * Start the engine, and initialize GLFW.
     *
     * @param windowWidth  Width of the window to be created
     * @param windowHeight Height of the window to be created
     * @param windowTitle  Title of the window to be created
     */
    public static void init(int windowWidth, int windowHeight, String windowTitle) {

        preInit();

        getInstance().window = new Window(windowWidth, windowHeight, windowTitle, false);

        getInstance().window.showWindow();
    }

    /**
     * Start the engine, and initialize GLFW. This will create a fullscreen window.
     *
     * @param windowTitle      Title of the window to be created
     * @param minSceneLighting float from 0-1 indicating the minimum scene light level
     */
    public static void init(String windowTitle, float minSceneLighting) {

        preInit();

        getInstance().window = new Window(windowTitle, minSceneLighting, false);

        getInstance().window.showWindow();
    }

    public static void init(int windowWidth, int windowHeight, String windowTitle, float minSceneLighting, boolean recalculateProjectionOnResize) {

        preInit();

        getInstance().window = new Window(windowWidth, windowHeight, windowTitle, minSceneLighting, recalculateProjectionOnResize);

        getInstance().window.showWindow();
    }

    /**
     * Start the engine, and initialize GLFW.
     *
     * @param windowTitle Title of the window to be created
     */
    public static void init(String windowTitle) {

        preInit();

        getInstance().window = new Window(windowTitle);

        getInstance().window.showWindow();
    }

    /**
     * @return Returns the number of milliseconds since the engine started. (since the first call)
     */
    public static double millisRunning() {
        return System.currentTimeMillis() - getInstance().startMillis;
    }

}
