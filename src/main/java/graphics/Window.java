package graphics;

import audio.AudioMaster;
import event.EventData;
import event.Events;
import graphics.postprocess.PostProcessing;
import input.Keyboard;
import input.Mouse;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import scene.Scene;
import scene.SceneManager;
import util.Engine;
import util.Log;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * The Window class handles setup of GLFW
 */
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

    public static Window instance = null;

    public Window(int pwidth, int pheight, String ptitle, boolean fullscreen, float minSceneLighting, boolean recalculateProjectionOnResize) {
        instance = this;

        Log.debug("construct window instance (" + ptitle + ", " + pwidth + ", " + pheight + ", "
                + minSceneLighting + ", recalc: " + recalculateProjectionOnResize + ", fullscreen: " + fullscreen + ")");
        videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        width = pwidth;
        height = pheight;
        title = ptitle;
        this.recalculateProjectionOnResize = recalculateProjectionOnResize;

        // create the sceneManager to be able to set a scene
        sceneManager = new SceneManager();

        sceneManager.setMinSceneLight(minSceneLighting);

        // Configure GLFW
        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);

        if (!fullscreen)
            initWindow(width, height, title, 0);
        else
            initWindow(width, height, title, glfwGetPrimaryMonitor());
        Log.info("window creation successful");
    }

    public Window(String ptitle, float minSceneLighting, boolean recalculateProjectionOnResize) {
        instance = this;

        Log.debug("construct window instance (" + ptitle + ", " + minSceneLighting + ", " + recalculateProjectionOnResize + ")");
        videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        width = videoMode.width();
        height = videoMode.height();
        Log.debug("video mode " + width + "/" + height);
        title = ptitle;
        this.recalculateProjectionOnResize = recalculateProjectionOnResize;

        // create the sceneManager to be able to set a scene
        sceneManager = new SceneManager();

        sceneManager.setMinSceneLight(minSceneLighting);

        // Configure GLFW
        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);

        initWindow(width, height, title, glfwGetPrimaryMonitor());
        Log.info("window creation successful");
    }

    public Window(int pwidth, int pheight, String ptitle, float minSceneLighting,
                  boolean recalculateProjectionOnResize) {
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
        Log.info("creating window");
        glfwWindow = glfwCreateWindow(width, height, title, monitor, 0);

        if (glfwWindow == 0) {
            Log.fatal("failed to create the window");
            throw new IllegalStateException("[FATAL] Failed to create window.");
        }

        Log.info("setting up glfw");
        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable V-Sync
        glfwSwapInterval(1);


        // Center the window
        int xpos = (videoMode.width() - width) / 2;
        int ypos = (videoMode.height() - height) / 2;
        glfwSetWindowPos(glfwWindow, xpos, ypos);
        Log.debug("window centered at " + xpos + "/" + ypos);
        GL.createCapabilities();

        // Set up callback
        glfwSetFramebufferSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
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

        Log.debug("setting up input callbacks");
        Mouse.setupCallbacks();
        Keyboard.setupCallbacks();

    }

    public float getFPS() {
        float fps = 1 / Engine.deltaTime();
        glfwSetWindowTitle(glfwWindow, title + " @ " + (int) fps + " FPS");
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
        Log.info("starting window");
        glfwShowWindow(glfwWindow);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        double frameBeginTime = glfwGetTime();
        double frameEndTime = glfwGetTime();

        Log.info("enabling scene");
        sceneManager.enable();

        Log.info("starting game loop");
        while (!glfwWindowShouldClose(glfwWindow)) {
            frameEndTime = glfwGetTime();
            Engine.updateDeltaTime((float) (frameEndTime - frameBeginTime));
            frameBeginTime = frameEndTime;

            glfwPollEvents();

            if (!sleeping && currentScene().isActive()) {
                Mouse.update();
                sceneManager.update();
                sceneManager.updateGameObjects();
                sceneManager.render();
                PostProcessing.prepare();
                sceneManager.postProcess(currentScene().renderer.fetchColorAttachment(0));
                PostProcessing.finish();
                sceneManager.updateUI();
                sceneManager.debugRender();
            }
            glfwSwapBuffers(glfwWindow);
            getFPS();
            frameEndTime = glfwGetTime();
        }

        Log.debug("shutting down");

        currentScene().clean();
        // Delete all framebuffers
        Framebuffer.clean();
        AudioMaster.get().clean();

        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        Log.info("window closed");
        //stopping engine and all threads listening for the engine to run
        Engine.getInstance().windowStopped();

    }

    public Scene currentScene() {
        return sceneManager.currentScene();
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public static Camera getCamera() {
        return instance.currentScene().camera();
    }

    public void setIcon(String path) {
        Texture icon = new Texture(path);
        GLFWImage image = GLFWImage.malloc();
        GLFWImage.Buffer buffer = GLFWImage.malloc(1);
        ByteBuffer iconBuffer = icon.loadImageInByteBuffer(path);
        image.set(icon.getWidth(), icon.getHeight(), iconBuffer);
        buffer.put(0, image);
        glfwSetWindowIcon(glfwWindow, buffer);
        buffer.free();
        image.free();
    }
}