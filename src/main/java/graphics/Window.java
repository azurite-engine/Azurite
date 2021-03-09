package graphics;


import event.EventData;
import event.Events;
import input.Keyboard;
import scenes.Demo;
import scenes.Main;
import input.Mouse;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import util.Engine;
import util.Scene;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {

    // Define and set the current scene
    public static Scene main = new Main();
    public static Scene demo = new Demo();

    public static Scene currentScene = demo;

    // Window Variables
    public long frameCount = 0;

    String title;
    public static long window;
    private GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

    public static int width;
    public static int height;

    private float dt = 0; // deltaTime (accessible from util.Engine.deltaTime)

    public Window(int pwidth, int pheight, String ptitle, boolean fullscreen, float minSceneLighting) {
        width = pwidth;
        height = pheight;
        title = ptitle;
        currentScene.minLighting = minSceneLighting;

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create window
        if (!fullscreen)
            window = glfwCreateWindow(width, height, title, 0, 0);
        else
            window = glfwCreateWindow(width, height, title, glfwGetPrimaryMonitor(), 0);

        if (window == 0)
            throw new IllegalStateException("[FATAL] Failed to create window.");

        // Set up callback
        glfwSetWindowSizeCallback(window, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);

            Events.windowResizeEvent.onEvent(new EventData.WindowResizeEventData(newWidth, newHeight));
        });

        Mouse.setupCallbacks();
        Keyboard.setupCallbacks();

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Enable V-Sync
        //glfwSwapInterval(1);


        // Center the window
        glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        GL.createCapabilities();
    }

    void getFPS() {
        frameCount++;
        glfwSetWindowTitle(window, title + " @ " + Math.round((frameCount / (Engine.millis() / 1000))) + " FPS");
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(window, title);
    }

    public void showWindow() {
        /*
         * scenes.Main game loop
         */
        glfwShowWindow(window);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        float frameBeginTime = (float)glfwGetTime();
        float frameEndTime = (float)glfwGetTime();

        currentScene.loadEngineResources();

        currentScene.awake();
        currentScene.initRenderers();

        currentScene.startGameObjects();

        while (!glfwWindowShouldClose(window)) {
            Engine.deltaTime = dt;

            Mouse.update();
            Keyboard.update();
            // poll GLFW for input events
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            currentScene.update();
            currentScene.updateGameObjects();
            currentScene.render();

            glfwSwapBuffers(window);
            getFPS();

            frameEndTime = (float)glfwGetTime();
            dt = frameEndTime - frameBeginTime;
            Engine.deltaTime = dt;
            frameBeginTime = frameEndTime;
        }

        currentScene.clean();
        // Delete all framebuffers
        Framebuffer.clean();

        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private static void setHeight(int newHeight) {
        height = newHeight;
    }

    private static void setWidth(int newWidth) {
        width = newWidth;
    }

    public static int getWidth () {
        return width;
    }

    public static int getHeight () {
        return height;
    }
}