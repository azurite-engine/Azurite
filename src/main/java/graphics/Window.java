package graphics;


import event.EventData;
import event.Events;
import input.Keyboard;
import input.Mouse;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import postprocess.PostProcessing;
import scene.Scene;
import scene.SceneManager;
import util.Engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {

    private SceneManager sceneManager;
    //public static Scene currentScene = demoPlatformer; TODO: remove later

    // Window Variables
    public long frameCount = 0;

    private String title;
    private static long glfwWindow;
    private final GLFWVidMode videoMode;

    private static int width;
    private static int height;
    private final boolean recalculateProjectionOnResize;

    public Window(int pwidth, int pheight, String ptitle, boolean fullscreen, float minSceneLighting, boolean recalculateProjectionOnResize) {
        videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        width = pwidth;
        height = pheight;
        title = ptitle;
        this.recalculateProjectionOnResize = recalculateProjectionOnResize;
        currentScene().minLighting = minSceneLighting;

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        if (!fullscreen)
            initWindow(width, height, title, 0);
        else
            initWindow(width, height, title, glfwGetPrimaryMonitor());
    }

    public Window(String ptitle, float minSceneLighting, boolean recalculateProjectionOnResize) {
        videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        width = videoMode.width();
        height = videoMode.height();
        title = ptitle;
        this.recalculateProjectionOnResize = recalculateProjectionOnResize;
        currentScene().minLighting = minSceneLighting;

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        initWindow(width, height, title, glfwGetPrimaryMonitor());
    }

    public Window(int pwidth, int pheight, String ptitle, float minSceneLighting, boolean recalculateProjectionOnResize) {
        this(pwidth, pheight, ptitle, false, minSceneLighting, recalculateProjectionOnResize);
    }

    public Window(int pwidth, int pheight, String ptitle, boolean recalculateProjectionOnResize) {
        this(pwidth, pheight, ptitle, 1.0f, recalculateProjectionOnResize);
    }

    public Window(String ptitle, boolean recalculateProjectionOnResize) {
        this(ptitle, 1.0f, recalculateProjectionOnResize);
    }

    public Window(String ptitle) {
        this(ptitle, false);
    }

    private void initWindow(int width, int height, String title, long monitor) {
        // Create window
        glfwWindow = glfwCreateWindow(width, height, title, monitor, 0);

        if (glfwWindow == 0)
            throw new IllegalStateException("[FATAL] Failed to create window.");

        // Set up callback
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);

            if (recalculateProjectionOnResize && currentScene().camera != null)
                currentScene().camera.adjustProjection();
            Events.windowResizeEvent.onEvent(new EventData.WindowResizeEventData(newWidth, newHeight));
        });

        Mouse.setupCallbacks();
        Keyboard.setupCallbacks();

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable V-Sync
//        glfwSwapInterval(1);

        // Center the window
        glfwSetWindowPos(glfwWindow, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        GL.createCapabilities();
    }

    void getFPS() {
        frameCount++;
        glfwSetWindowTitle(glfwWindow, title + " @ " + Math.round((frameCount / (Engine.millisRunning() / 1000))) + " FPS");
    }

    public static long glfwWindow() {
        return glfwWindow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(glfwWindow, title);
    }

    public void showWindow() {
        /*
         * scenes.Main game loop
         */
        glfwShowWindow(glfwWindow);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        float frameBeginTime = (float) glfwGetTime();
        float frameEndTime;

        currentScene().loadSceneResources();

        currentScene().initRenderers();
        currentScene().awake();

        currentScene().startGameObjects();

        float dt = 0;
        Engine.updateDeltaTime(dt);

        while (!glfwWindowShouldClose(glfwWindow)) {

            Mouse.update();
            Keyboard.update();
            // poll GLFW for input events
            glfwPollEvents();

            currentScene().update();
            currentScene().updateGameObjects();
            currentScene().render();
            PostProcessing.prepare();
            currentScene().postProcess(currentScene().renderer.fetchColorAttachment(0));
            PostProcessing.finish();
            currentScene().debugRender();

            glfwSwapBuffers(glfwWindow);
            getFPS();

            frameEndTime = (float) glfwGetTime();
            dt = frameEndTime - frameBeginTime;
            Engine.updateDeltaTime(dt);
            frameBeginTime = frameEndTime;
        }

        currentScene().clean();
        // Delete all framebuffers
        Framebuffer.clean();

        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public Scene currentScene() {
        return sceneManager.currentScene();
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    private static void setHeight(int newHeight) {
        height = newHeight;
    }

    private static void setWidth(int newWidth) {
        width = newWidth;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}