package scene;

import ecs.GameObject;
import graphics.Camera;
import graphics.Texture;
import graphics.postprocess.ForwardToTexture;
import graphics.postprocess.PostProcessStep;
import graphics.renderer.*;
import input.Keyboard;
import org.lwjgl.glfw.GLFW;
import physics.collision.Collider;
import ui.Element;
import ui.RenderableElement;
import ui.Text;
import util.Engine;
import util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class encapsulating the game logic, the gameObjects, the renderers, the physics
 * specifications and all the necessary ecs-related logic. Programming/Interacting with this
 * game engine will usually involve the following boilerplate code:
 * <pre>
 * public class Main extends Scene {
 *     public static void main(String[] args) {
 *         Engine.init(1920, 1080, "Azurite Engine Demo In Comment", 1.0f);
 *         Engine.scenes().switchScene(new Main());
 *         Engine.showWindow();
 *     }
 *
 *     public void awake() {
 *         Graphics.setDefaultBackground(Color.BLACK);
 *         camera = new Camera();
 *         ...
 *     }
 *
 *     public void update() {
 *         ...
 *     }
 * }
 * </pre>
 * A simple example of a scene with just a rendered sprite:
 * <pre>
 * public class Main extends Scene {
 *     GameObject player;
 *     Sprite s;
 *
 *     public static void main(String[] args) {
 *         Engine.init(1920, 1080, "Azurite Engine Demo In Comment", 1.0f);
 *         Engine.scenes().switchScene(new Main());
 *         Engine.showWindow();
 *     }
 *
 *     public void awake() {
 *         Graphics.setDefaultBackground(Color.BLACK);
 *         camera = new Camera();
 *
 *         player = new GameObject();
 *         s = new Sprite
 *         player.addComponent(new SpriteRenderer(s, new Vector2f(100)));
 *     }
 *
 *     public void update() {
 *         if (Keyboard.getKeyDown(GLFW.GLFW_KEY_SPACE)) {
 *             player.transform.add(new Vector2f(1, 0));
 *         }
 *     }
 * }
 * </pre>
 * @see SceneManager
 * @author Asher Haun
 * @author Juyas
 * @author VoxelRifts
 */
public abstract class Scene {

    private static int sceneCounter = 0;
    private final int sceneId = sceneCounter++;

    private final List<GameObject> gameObjects = new LinkedList<>();
    private final List<Collider> colliders = new LinkedList<>();
    private final List<Text> texts = new ArrayList<>();
    private final List<Element> uiElements = new ArrayList<>();

    private List<Renderer> rendererRegistry = new LinkedList<>();


    public DefaultRenderer renderer = new DefaultRenderer();
    public LightmapRenderer lightmapRenderer = new LightmapRenderer();
    public DebugRenderer debugRenderer = new DebugRenderer();
    public TextRenderer textRenderer = new TextRenderer();
    public UIRenderer uiRenderer = new UIRenderer();

    protected Camera camera;
    protected ForwardToTexture forwardToScreen;
    private boolean debugMode = false;
    private boolean active = false;

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
        Log.debug("scene " + sceneId + " activated");
    }

    /**
     * This method will be called each time this scene becomes inactive by {@link SceneManager},
     * because of switching to another method or termination of the program.
     * Can be used to preserve the current state of the scene or quickly complete/cancel tasks that were midst execution.
     */
    public void deactivate() {
        this.active = false;
        Log.debug("scene " + sceneId + " deactivated");
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
     *
     * @see SceneManager
     */
    public final int sceneId() {
        return sceneId;
    }

    /**
     * Loops through all gameobjects already in the scene and calls their start methods.
     */
    // TODO not called? find out why
    public final void startGameObjects() {
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            this.renderer.add(gameObject);
            this.lightmapRenderer.add(gameObject);
            this.debugRenderer.add(gameObject);
            rendererRegistry.forEach(r -> r.add(gameObject));
        }
    }

    public List<Collider> getColliders() {
        return colliders;
    }

    public final void registerCollider(GameObject gameObject) {
        colliders.add(gameObject.getComponent(Collider.class));
    }

    public final void unregisterCollider(GameObject gameObject) {
        colliders.remove(gameObject.getComponent(Collider.class));
    }

    /**
     * @return the List of gameObjects contained in the scene.
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
        if (active) {
            gameObject.start();
            addToRenderers(gameObject);
        }
    }

    /**
     * @param gameObject GameObject to be added.
     */
    public void removeGameObjectFromScene(GameObject gameObject) {
        gameObjects.remove(gameObject);
        removeFromRenderers(gameObject);
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
    public void updateGameObjects () {
        for (GameObject go : gameObjects) {
            go.update(Engine.deltaTime());
        }
    }

    public void updateUI () {
        // Logger.logInfo("There are " + texts.size() + " text elements.");
        for (Element e : uiElements) {
            e.update();
        }
        for (Text t : texts) {
            t.update();
        }
    }

    // ----- Rendering -----

    /**
     * Apply post processing to a texture
     *
     * @param texture input texture
     */
    public void postProcess(Texture texture) {
        forwardToScreen.setTexture(texture);
        forwardToScreen.apply();
    }

    /**
     * Register a renderer to this scene
     *
     * @param renderer the renderer to be registered
     */
    public void registerRenderer(Renderer renderer) {
        rendererRegistry.add(renderer);
    }

    /**
     * Initialize all renderers
     */
    public void initRenderers () {
        debugRenderer.init();
        lightmapRenderer.init();
        renderer.init();
        forwardToScreen = new ForwardToTexture(PostProcessStep.Target.DEFAULT_FRAMEBUFFER);
        forwardToScreen.init();
        uiRenderer.init();
    }

    public final void startUi () {
        textRenderer.init();

        for (Element e : uiElements) {
            if (e instanceof RenderableElement) {
                ((RenderableElement) e).start();
            }
        }
    }

    public void render () {
        rendererRegistry.forEach(Renderer::render);
        lightmapRenderer.render();
        lightmapRenderer.bindLightmap();
        renderer.render();
    }

    public void debugRender () {
        if (debugMode) this.debugRenderer.render();
    }

    public final void textRender() {
        uiRenderer.render();
        textRenderer.render();
    }

    /**
     * This method is called at the end of the program
     */
    public void clean() {
        this.renderer.clean();
        this.lightmapRenderer.clean();
        this.debugRenderer.clean();
        this.textRenderer.clean();
        this.uiRenderer.clean();
        rendererRegistry.forEach(Renderer::clean);
    }

    public void addText (Text t) {
        texts.add(t);
    }

    public void addUIElement (Element e) {
        uiElements.add(e);

        if (e instanceof RenderableElement) {
            uiRenderer.add((RenderableElement) e);
        }
    }

    /**
     * Add a gameObject to all renderers
     *
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
     *
     * @param gameObject the gameObject to be removed
     */
    private void removeFromRenderers(GameObject gameObject) {
        this.renderer.remove(gameObject);
        this.lightmapRenderer.remove(gameObject);
        this.debugRenderer.remove(gameObject);
        rendererRegistry.forEach(r -> r.remove(gameObject));
    }
}
