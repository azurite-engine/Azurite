package scene;

import ecs.GameObject;
import graphics.Camera;
import graphics.renderer.DebugRenderer;
import graphics.renderer.DefaultRenderer;
import graphics.renderer.LightmapRenderer;
import graphics.renderer.Renderer;
import input.Keyboard;
import org.lwjgl.glfw.GLFW;
import postprocess.ForwardToTexture;
import postprocess.PostProcessStep;
import util.Assets;
import util.Engine;

import java.util.ArrayList;
import java.util.List;

import static util.Engine.deltaTime;

public abstract class Scene {
    public DefaultRenderer renderer = new DefaultRenderer();
    public LightmapRenderer lightmapRenderer = new LightmapRenderer();
    public DebugRenderer debugRenderer = new DebugRenderer();
    private List<Renderer<?>> rendererRegistry = new ArrayList<>();
    public Camera camera;
    private boolean isRunning = false;
    private boolean debugMode = true;
    static protected List<GameObject> gameObjects = new ArrayList<>();

    protected ForwardToTexture forwardToScreen;

    public float minLighting;

    /**
     * @param args Entry point to start the application
     */
    public static void main(String[] args) {
        Engine.init(1600, 900, "Hello World!", 1);
    }

    /**
     * Runs only once on startup, useful for initializing gameObjects or for first time setup.
     */
    public void awake() {
        camera = new Camera();
    }

    /**
     * This method is called every frame, and can be used to update objects.
     */
    public void update() {
        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_GRAVE_ACCENT)) {
            debugMode = !debugMode;
        }
    }

    public void postProcess(int texture) {
        forwardToScreen.setTexture(texture);
        forwardToScreen.apply();
    }

    /**
     * This method is called at the end of the program
     */
    public void clean() {
        this.renderer.clean();
        this.lightmapRenderer.clean();
        this.debugRenderer.clean();
        rendererRegistry.forEach(Renderer::clean);
    }

    // The following methods shouldn't be overridden. For this, added final keyword

    /**
     * Loops through all gameobjects already in the scene and calls their start methods.
     */
    public final void startGameObjects() {
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            this.renderer.add(gameObject);
            this.lightmapRenderer.add(gameObject);
            this.debugRenderer.add(gameObject);
            rendererRegistry.forEach(r -> r.add(gameObject));
        }
        isRunning = true;
    }

    /**
     * @return Returns an ArrayList of gameObjects in the scene.
     */
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    /**
     * @param gameObject GameObject to be added.
     *                   Add a new gameObject to the scene and immediately call its start method.
     */
    public void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);
        gameObject.start();
    }

    /**
     * Register a renderer to this scene
     *
     * @param renderer the renderer to be registered
     */
    public void registerRenderer(Renderer<?> renderer) {
        rendererRegistry.add(renderer);
    }

    /**
     * @return Returns the scene's instance of Camera
     */
    public Camera camera() {
        return this.camera;
    }

    /**
     * Loops through all the gameObjects in the scene and calls their update methods.
     */
    public void updateGameObjects() {
        for (GameObject go : gameObjects) {
            go.update(deltaTime);
        }
    }

    public void render() {
        rendererRegistry.forEach(Renderer::render);
        lightmapRenderer.render();
        lightmapRenderer.bindLightmap();
        renderer.render();
        lightmapRenderer.framebuffer.blitColorBuffersToScreen();
    }

    public void debugRender() {
        if (debugMode) this.debugRenderer.render();
    }

    /**
     * Loads the shader.
     */
    public void loadEngineResources() {
        Assets.getShader("src/assets/shaders/default.glsl");
    }

    /**
     * Initialize all renderers
     */
    public void initRenderers() {
        debugRenderer.init();
        lightmapRenderer.init();
        renderer.init();
        forwardToScreen = new ForwardToTexture(PostProcessStep.Target.DEFAULT_FRAMEBUFFER);
        forwardToScreen.init();
    }
}