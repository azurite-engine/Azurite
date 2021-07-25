package scene;

import ecs.GameObject;
import ecs.Text;
import graphics.Camera;
import graphics.Texture;
import graphics.renderer.DebugRenderer;
import graphics.renderer.DefaultRenderer;
import graphics.renderer.LightmapRenderer;
import graphics.renderer.Renderer;
import graphics.renderer.TextRenderer;
import input.Keyboard;
import org.lwjgl.glfw.GLFW;
import postprocess.ForwardToTexture;
import postprocess.PostProcessStep;
import util.Assets;
import util.Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Asher Haun
 * @author Juyas
 * @author VoxelRifts
 */
public abstract class Scene {

    private static int sceneCounter = 0;

    public DefaultRenderer renderer = new DefaultRenderer();
    public LightmapRenderer lightmapRenderer = new LightmapRenderer();
    public DebugRenderer debugRenderer = new DebugRenderer();
    public TextRenderer textRenderer = new TextRenderer();

    private List<Renderer<?>> rendererRegistry = new LinkedList<>();

    protected Camera camera;
    private boolean debugMode = true;
    private boolean active = false;
    private List<GameObject> gameObjects = new LinkedList<>();
    private ArrayList<Text> uiObjects = new ArrayList<>();

    protected ForwardToTexture forwardToScreen;

    private int sceneId = sceneCounter++;

    public boolean isActive() {
        return active;
    }

    /**
     * Runs only once on startup, useful for initializing gameObjects or for first time setup.
     */
    public void awake() {
        camera = new Camera();
    }

    /**
     * This method will be called each time this scene becomes active by {@link SceneManager}.
     * Will be called right before the first update.
     * Can be used to prepare the scene to be shown after been shown previously to reset to a certain state.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * This method will be called each time this scene becomes inactive by {@link SceneManager},
     * because of switching to another method or termination of the program.
     * Can be used to preserve the current state of the scene or quickly complete/cancel tasks that were midst execution.
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * This method is called every frame, and can be used to update objects.
     */
    public void update() {
        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_GRAVE_ACCENT)) {
            debugMode = !debugMode;
        }
    }

    /**
     * Apply post processing to a texture
     * @param texture input texture
     */
    public void postProcess(Texture texture) {
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
        this.textRenderer.clean();
        rendererRegistry.forEach(Renderer::clean);
    }

    // The following methods shouldn't be overridden. For this, added final keyword

    public final void startUi () {
        textRenderer.init();
    }

    /**
     * The sceneId is meant to represent the instance of a scene as an integer
     *
     * @see SceneManager
     */
    public final int sceneId() {
        return sceneId;
    }

    /**
     * @return Returns the List of gameObjects contained in the scene.
     */
    public List<GameObject> getGameObjects() {
        return Collections.unmodifiableList(gameObjects);
    }

    /**
     * @param gameObject GameObject to be added.
     *                   Add a new gameObject to the scene and immediately call its start method.
     */
    public void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);
        if (active) {
            gameObject.start();
            addToRenderers(gameObject);
        }
    }

    public void addUiObject (Text t) {
        uiObjects.add(t);
    }

    /**
     * @param gameObject GameObject to be added.
     */
    public void removeGameObjectFromScene(GameObject gameObject) {
        gameObjects.remove(gameObject);
        removeFromRenderers(gameObject);
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
            go.update(Engine.deltaTime());
        }
    }

    public void render() {
        rendererRegistry.forEach(Renderer::render);
        lightmapRenderer.render();
        lightmapRenderer.bindLightmap();
        renderer.render();
    }

    public final void textRender() {
        textRenderer.render();
    }

    public void debugRender() {
        if (debugMode) this.debugRenderer.render();
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

    /**
     * Add a gameObject to all renderers
     * @param gameObject the gameObject to be added
     */
    public void addToRenderers(GameObject gameObject) {
        this.renderer.add(gameObject);
        this.lightmapRenderer.add(gameObject);
        this.debugRenderer.add(gameObject);
        rendererRegistry.forEach(r -> r.add(gameObject));
    }

    /**
     * Remove a gameObject from all renderers
     * @param gameObject the gameObject to be removed
     */
    private void removeFromRenderers(GameObject gameObject) {
        this.renderer.remove(gameObject);
        this.lightmapRenderer.remove(gameObject);
        this.debugRenderer.remove(gameObject);
        rendererRegistry.forEach(r -> r.remove(gameObject));
    }

    public void updateUI () {
        for (Text i : uiObjects) {
            i.update();
        }
    }
}
