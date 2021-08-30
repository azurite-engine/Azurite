package ecs;

import org.joml.Vector2f;
import org.joml.Vector3f;
import physics.LocationSensitive;
import scene.Scene;
import util.OrderPreservingList;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * A GameObject is the root of the Entity Component system used to store all entities in Azurite games.
 * Each GameObject can contain any of a number of available components including spriteRenderers and lights.
 * By default, each GameObject contains a Transform, which holds the X and Y position, and width and height of the object in pixels.
 *
 * @author Asher Haun
 * @author Gabe
 */

public class GameObject {

    public static final String DEFAULT_GAMEOBJECT_NAME = "Default GameObject Name";
    public static final String EMPTY_GAMEOBJECT_NAME = "Empty GameObject";
    public static final int DEFAULT_Z_INDEX = 0;
    private static long internalCounter = 0;
    private final long objId = internalCounter++;
    private final OrderPreservingList<Component> components;
    private final Scene parentScene;
    private final Collection<LocationSensitive> transformSensitives;
    private final String name;
    private final Vector3f locationData;
    private final Vector3f locationBuffer;
    private int zIndex;

    /**
     * Creates a new GameObject.
     *
     * @param scene         the scene to object will be added to
     * @param name
     * @param componentList
     * @param locationData
     * @param zIndex
     */
    public GameObject(Scene scene, String name, List<Component> componentList, Vector3f locationData, int zIndex) {
        this.name = name;
        this.components = new OrderPreservingList<>(componentList);
        this.locationData = locationData;
        this.locationBuffer = new Vector3f();
        this.zIndex = zIndex;
        this.parentScene = scene;
        scene.addGameObjectToScene(this);
        this.transformSensitives = new LinkedList<>();
    }

    /**
     * @param scene        the scene to object will be added to
     * @param name
     * @param locationData
     * @param zIndex
     */
    public GameObject(Scene scene, String name, Vector3f locationData, int zIndex) {
        this(scene, name, new LinkedList<>(), locationData, zIndex);
    }

    /**
     * @param scene  the scene to object will be added to
     * @param name
     * @param zIndex
     */
    public GameObject(Scene scene, String name, int zIndex) {
        this(scene, name, new LinkedList<>(), new Vector3f(), zIndex);
    }

    /**
     * @param scene        the scene to object will be added to
     * @param locationData
     * @param zIndex
     */
    public GameObject(Scene scene, Vector3f locationData, int zIndex) {
        this(scene, DEFAULT_GAMEOBJECT_NAME, new LinkedList<>(), locationData, zIndex);

    }

    /**
     * @param scene        the scene to object will be added to
     * @param locationData
     */
    public GameObject(Scene scene, Vector3f locationData) {
        this(scene, DEFAULT_GAMEOBJECT_NAME, new LinkedList<>(), locationData, DEFAULT_Z_INDEX);
    }

    /**
     * Creates an empty gameObject with an empty Transform and no Components.
     * Its name will be GameObject.EMPTY_GAMEOBJECT_NAME
     *
     * @param scene the scene to object will be added to
     */
    public GameObject(Scene scene) {
        this(scene, EMPTY_GAMEOBJECT_NAME, new LinkedList<>(), new Vector3f(), DEFAULT_Z_INDEX);
    }

    /**
     * Called once on gameObject creation, also starts any components that are passed to the constructor.
     */
    public void start() {
        for (Component component : components) {
            component.start();
        }
    }

    /**
     * Called once every frame for each GameObject, calls the update method for each component it contains
     */
    public void update(float dt) {

        //clear position buffer at the start to only record freshly made changes
        //transform.resetPositionBuffer();
        locationBuffer.set(0, 0, 0);

        //update components, that do impact the position of the object
        components.stream().filter(Component::transformingObject).forEach(component -> component.update(dt));

        //check collision to fix the position
        getParentScene().checkCollision(getComponent(RigidBody.class));

        //apply all changes made to the position buffer
        if (!locationBuffer.equals(0, 0, 0)) {
            //apply changes of the buffer
            locationData.add(locationBuffer);
            //update all components that are interested in a change of the transform
            transformSensitives.forEach(transformSensitive -> transformSensitive.update(locationData));
        }

        //update components, that update the screen visually only
        components.stream().filter(component1 -> !component1.transformingObject()).forEach(component -> component.update(dt));

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
    public long getObjId() {
        return objId;
    }

    /**
     * @return a new copy of the current location of the gameObject
     */
    public Vector3f getReadOnlyLocation() {
        return new Vector3f(locationData);
    }

    /**
     * @return a new copy of the current position of the gameObject
     */
    public Vector2f getReadOnlyPosition() {
        return new Vector2f(locationData.x, locationData.y);
    }

    /**
     * Do NOT change this transform here. Use locationBuffer for that.
     *
     * @return the raw transform object of the gameObject
     */
    public Vector3f getRawLocation() {
        return this.locationData;
    }

    public Vector3f locationBuffer() {
        return this.locationBuffer;
    }

    public int zIndex() {
        return zIndex;
    }

    @Deprecated
    public void setZindex(int z) {
        parentScene.removeGameObjectFromScene(this);
        zIndex = z;
        parentScene.addGameObjectToScene(this);
    }

    public String name() {
        return name;
    }

    /**
     * Takes a parameter of a class that extends component and returns it if it is contained in the GameObject's list of components.
     *
     * @param componentClass of component (ie. "SpriteRenderer.class")
     * @return Component of type passed as param is contained in GameObject
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    assert false : "[ERROR] Failed to cast component.";
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Takes a parameter of a class that extends component and removed it from the GameObject if it is contained in the list of components.
     *
     * @param componentClass of component (ie. "SpriteRenderer.class")
     */
    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                c.remove();
                c.gameObject = null;
                components.remove(i);
                if (c instanceof LocationSensitive)
                    transformSensitives.remove(c);
                parentScene.updateGameObject(this, false);
                return;
            }
        }
    }


    /**
     * Adds a new component to the GameObject's list.
     * If the new components conflicts with any other component, an {@link IllegalComponentStateException} will be thrown.
     * Whether a component is conflicting with another is determined by {@link Component#isConflictingWith(Class)}.
     *
     * @param c the new component
     * @return the gameobject itself
     */
    public GameObject addComponent(Component c) {
        if (this.components.stream().anyMatch(component -> c.isConflictingWith(component.getClass()) || component.isConflictingWith(c.getClass())))
            throw new IllegalComponentStateException("Component " + c.getClass() + " is conflicting with existing one");
        this.components.add(c);
        if (c instanceof LocationSensitive)
            this.transformSensitives.add((LocationSensitive) c);
        c.gameObject = this;
        //TODO check if this is necessary
        if (getParentScene() != null) {
            if (getParentScene().isActive()) {
                c.start();
                getParentScene().addToRenderers(this);
            }
        }
        //update collision maps in scene
        parentScene.updateGameObject(this, true);
        return this;
    }

    /**
     * @return List of Components in GameObject
     */
    public List<Component> getComponents() {
        return components;
    }
}
