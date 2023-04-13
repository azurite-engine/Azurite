package ecs;

import org.joml.Vector2f;
import physics.collision.Collider;
import scene.Scene;
import util.Engine;
import util.Log;
import util.OrderPreservingList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A GameObject is the root of the Entity Component system used to store all
 * entities in Azurite games.
 * Each GameObject can contain any of a number of available components including
 * spriteRenderers and lights.
 * By default, each GameObject contains a Transform, which holds the X and Y
 * position.
 *
 * <pre>
 * import ecs.*;
 * 
 * public class Main extends Scene {
 *     // ...
 *
 *     GameObject player;
 * 
 *     public void awake() {
 *         // ...
 *         player = new GameObject("Player", new Vector2f(600, 600), 2);
 *         player.addComponent(new PointLight(new Color(250, 255, 181), 30));
 *         player.addComponent(new SpriteRenderer(Assets.getTexture("src/assets/images/player.png"), new Vector2f(64, 64)));
 * 
 *     }
 * 
 *     // ...
 * }
 * </pre>
 * 
 * @author Asher Haun
 * @author Gabe
 * @author Juyas
 */

public class GameObject {

    public static final String DEFAULT_GAMEOBJECT_NAME = "Default GameObject Name";
    public static final int DEFAULT_Z_INDEX = 0;
    private static long internalCounter = 0;

    private final long objId = internalCounter++;
    private final String name;
    private final Scene parentScene;
    private final OrderPreservingList<Component> components;
    private final float[] position = new float[2];
    private int zIndex;

    /**
     * Creates a new GameObject.
     *
     * @param name
     * @param componentList
     * @param position
     * @param zIndex
     */
    public GameObject(String name, List<Component> componentList, Vector2f position, int zIndex) {
        this.name = name;
        if (this.name == null)
            Log.warn("GameObject with a name that is null created", 1);
        this.components = new OrderPreservingList<>(componentList);
        this.position[0] = position.x;
        this.position[1] = position.y;
        this.zIndex = zIndex;
        this.parentScene = Engine.window().currentScene();
        Engine.window().currentScene().addGameObjectToScene(this);
    }

    /**
     * Creates a new GameObject.
     *
     * @param scene    the scene to add the GameObject to. By default, GameObjects
     *                 are added to the currentScene.
     * @param name     name of the GameObject
     * @param position
     * @param zIndex
     */
    public GameObject(Scene scene, String name, Vector2f position, int zIndex) {
        this.name = name;
        if (this.name == null)
            Log.warn("GameObject with a name that is null created", 1);
        this.components = new OrderPreservingList<Component>(new LinkedList<>());
        this.position[0] = position.x;
        this.position[1] = position.y;
        this.zIndex = zIndex;
        this.parentScene = scene == null ? Engine.window().currentScene() : scene;
        this.parentScene.addGameObjectToScene(this);
    }

    /**
     * @param name
     * @param position
     * @param zIndex
     */
    public GameObject(String name, Vector2f position, int zIndex) {
        this(name, new LinkedList<>(), position, zIndex);
    }

    /**
     * @param name
     * @param zIndex
     */
    public GameObject(String name, int zIndex) {
        this(name, new LinkedList<>(), new Vector2f(), zIndex);
    }

    /**
     * @param position
     * @param zIndex
     */
    public GameObject(Vector2f position, int zIndex) {
        this(DEFAULT_GAMEOBJECT_NAME, new LinkedList<>(), position, zIndex);

    }

    /**
     * @param position
     */
    public GameObject(Vector2f position) {
        this(DEFAULT_GAMEOBJECT_NAME, new LinkedList<>(), position, DEFAULT_Z_INDEX);
    }

    /**
     * Called once on gameObject creation, also starts any components that are
     * passed to the constructor.
     */
    public void start() {
        for (Component component : components) {
            component.start();
        }
    }

    /**
     * Called once every frame for each GameObject, calls the update method for each
     * component it contains
     */
    public void update(float dt) {
        for (Component component : components) {
            component.update(dt);
        }
    }

    /**
     * @return the parent scene of this gameObject
     */
    public Scene getParentScene() {
        return parentScene;
    }

    /**
     * @return the universal and unique id among all objects
     */
    public long getUniqueId() {
        return objId;
    }

    public Vector2f getReadOnlyPosition() {
        return new Vector2f(position);
    }

    public float[] getPositionData() {
        return position;
    }

    public int zIndex() {
        return zIndex;
    }

    @Deprecated
    public void setZIndex(int z) {
        // TODO does this work now?
        parentScene.removeGameObjectFromScene(this);
        zIndex = z;
        parentScene.addGameObjectToScene(this);
    }

    public String name() {
        return name;
    }

    /**
     * Takes a parameter of a class that extends component and returns it if it is
     * contained in the GameObject's list of components.
     *
     * @param componentClass of component (ie. "SpriteRenderer.class")
     * @return Component of type passed as param is contained in GameObject
     */
    public <T> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    Log.fatal("failed to cast component to " + componentClass.getName());
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Takes a parameter of a class that extends component and returns it if it is
     * contained in the GameObject's list of components.
     *
     * @param componentClass of component (ie. "SpriteRenderer.class")
     * @return all components of type passed as param is contained in GameObject
     */
    public <T> List<T> getComponents(Class<T> componentClass) {
        List<T> comps = new ArrayList<>(components.size());
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    T cast = componentClass.cast(c);
                    comps.add(cast);
                } catch (ClassCastException e) {
                    Log.fatal("failed to cast component to " + componentClass.getName());
                    e.printStackTrace();
                }
            }
        }
        return comps;
    }

    /**
     * Takes a parameter of a class that extends component and removed it from the
     * GameObject if it is contained in the list of components.
     *
     * @param componentClass of component (ie. "SpriteRenderer.class")
     */
    public <T> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                c.remove();
                c.gameObject = null;
                components.remove(i);
                if (c instanceof Collider)
                    getParentScene().unregisterCollider(this);
                return;
            }
        }
    }

    /**
     * Adds a new component to the GameObject's list.
     *
     * @param c the new component
     * @return the gameobject itself
     */
    public GameObject addComponent(Component c) {
        this.components.add(c);
        c.gameObject = this;
        // TODO check if this is necessary
        if (getParentScene() != null) {
            if (getParentScene().isActive()) {
                c.start();
                getParentScene().addToRenderers(this);
            }
        }
        if (c instanceof Collider)
            getParentScene().registerCollider(this);
        return this;
    }

    /**
     * @return List of Components in GameObject
     */
    public List<Component> getComponents() {
        return components;
    }
}
