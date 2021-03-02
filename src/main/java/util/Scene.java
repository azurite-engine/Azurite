package util;

import ecs.GameObject;
import graphics.renderer.DefaultRenderer;
import graphics.renderer.Renderer;
import graphics.Camera;
import physics.Particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static util.Engine.deltaTime;

public abstract class Scene {
    public DefaultRenderer renderer = new DefaultRenderer();
    public Camera camera;
    private boolean isRunning = false;
    static protected ArrayList<GameObject> gameObjects = new ArrayList<>();
    static protected ArrayList<GameObject> gameObjectsQueue = new ArrayList<>();
    protected GameObject activeGameObject = null;

    public float minLighting;

    /**
     * @param args
     * Entry point to start the application
     */
    public static void main(String[] args) {
        Engine.init(1600, 900, "Hello World!", 0.3f);
    }

    /**
     * Runs only once on startup, useful for initializing gameObjects or for first time setup.
     */
    public void awake () {
        camera = new Camera();
    }

    /**
     * This method is called every frame, and can be used to update objects.
     */
    public void update() {}

    /**
     * This method is called at the end of the program
     */
    public void clean() {
        this.renderer.clean();
    }

    // The following methods shouldn't be overridden. For this, added final keyword
    /**
     * Loops through all gameobjects already in the scene and calls their start methods.
     */
    public final void startGameObjects() {
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            this.renderer.add(gameObject);
        }
        isRunning = true;
    }

    /**
     * @return Returns an ArrayList of gameObjects in the scene.
     */
    public ArrayList getGameObjects () {
        return gameObjects;
    }

    /**
     * @param gameObject GameObject to be added.
     * Add a new gameObject to the scene and immediately call its start method.
     */
    public static void addGameObjectToScene (GameObject gameObject) {
        gameObjects.add(gameObject);
        gameObject.start();
    }

    public static void queueGameObject (GameObject gameObject) {
        gameObjectsQueue.add(gameObject);
    }

    private static void emptyQueue () {
        for (GameObject go : gameObjectsQueue) {
            addGameObjectToScene(go);
        }
        if (gameObjectsQueue.size() >= 1) {
            gameObjectsQueue = new ArrayList<>();
        }
    }

    /**
     * @return Returns the scene's instance of Camera
     */
    public Camera camera () {
        return this.camera;
    }

    /**
     * Loops through all the gameObjects in the scene and calls their update methods.
     */
    public void updateGameObjects () {
        emptyQueue();
        for (GameObject go : gameObjects) {
            go.update((float) deltaTime);
        }
    }

    public void render() {
        this.renderer.render();
    }

    /**
     * Loads the shader.
     */
    public void loadEngineResources () {
        Assets.getShader("src/assets/shaders/default.glsl");
    }

    public void initRenderers() {
        renderer.init();
    }
}