package graphics;//{This comment is intentionally added to create a git merge conflict}


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

    private static long glfwWindow;
    private static int width;
    private static int height;
    private final GLFWVidMode videoMode;
    private final boolean recalculateProjectionOnResize;
    private SceneManager sceneManager;
    // Window Variables
    private long frameCount = 0;
    private String title;
    private boolean sleeping = false;

    public Window(int pwidth, int pheight, String ptitle, boolean fullscreen, float minSceneLighting, boolean recalculateProjectionOnResize) {
        videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        width = pwidth;
        height = pheight;
        title = ptitle;
        this.recalculateProjectionOnResize = recalculateProjectionOnResize;

        //create the sceneManager to be able to set a scene
        sceneManager = new SceneManager();

        sceneManager.setMinSceneLight(minSceneLighting);

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

        //create the sceneManager to be able to set a scene
        sceneManager = new SceneManager();

        sceneManager.setMinSceneLight(minSceneLighting);

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
            if (newWidth == 0 || newHeight == 0) {
                sleeping = true;
                return;
            }
            sleeping = false;
            Window.width = newWidth;
            Window.height = newHeight;

            if (recalculateProjectionOnResize && currentScene().camera() != null)
                currentScene().camera().adjustProjection();
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

    public float getFPS() {
        float fps = 1/Engine.deltaTime();
        glfwSetWindowTitle(glfwWindow, title + " @ " + (int)fps + " FPS");
        return fps;
    }

    public String getTitle() {
        return title;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    public static long glfwWindow() {
        return glfwWindow;
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

        double frameBeginTime = glfwGetTime();
        double frameEndTime = glfwGetTime();

        sceneManager.enable();

        while (!glfwWindowShouldClose(glfwWindow)) {

            frameEndTime = glfwGetTime();
            Engine.updateDeltaTime((float) (frameEndTime - frameBeginTime));
            frameBeginTime = frameEndTime;
            glfwPollEvents();

            if (!sleeping) {
                Mouse.update();
                sceneManager.update();
                sceneManager.updateGameObjects();
                sceneManager.render();
                PostProcessing.prepare();
                sceneManager.postProcess(currentScene().renderer.fetchColorAttachment(0));
                PostProcessing.finish();
                sceneManager.debugRender();
                sceneManager.updateUI();
            }
            glfwSwapBuffers(glfwWindow);
            getFPS();
            frameEndTime = glfwGetTime();
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
}