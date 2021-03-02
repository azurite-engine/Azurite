package ecs;

import graphics.Window;
import scenes.Main;
import physics.Transform;
import java.util.ArrayList;
import java.util.List;

/**
 * A GameObject is the root of the Entity Component system used to store all entities in Azurite games.
 * Each GameObject can contain any of a number of available components including spriteRenderers and lights.
 * By default, each GameObject contains a Transform, which holds the X and Y position, and width and height of the object in pixels.
 * @author Asher Haun
 * @author Gabe
 */

public class GameObject {

	public String name;
	private List<Component> components;
	private Transform transform;
	private int zIndex;

	/**
	 * Creates a new GameObject.
	 * @param name
	 * @param componentList
	 * @param transform
	 * @param zIndex
	 * @param queue if the gameObject is being added from inside of a gameObject or component, it has to be added to a queue to avoid ConcurrentModificationException
	 */
	public GameObject (String name, List<Component> componentList, Transform transform, int zIndex, boolean queue) {
		this.name = name;
		this.components = componentList;
		this.transform = transform;
		this.zIndex = zIndex;
		if (queue) {
			Window.currentScene.queueGameObject(this);
		} else {
			Window.currentScene.addGameObjectToScene(this);
		}
	}

	/**
	 * Creates a new GameObject.
	 * @param name
	 * @param componentList
	 * @param transform
	 * @param zIndex
	 */
	public GameObject (String name, List<Component> componentList, Transform transform, int zIndex) {
		this.name = name;
		this.components = componentList;
		this.transform = transform;
		this.zIndex = zIndex;
		Window.currentScene.addGameObjectToScene(this);
	}

	/**
	 * @param name
	 * @param transform
	 * @param zIndex
	 */
	public GameObject (String name, Transform transform, int zIndex) {
		this(name, new ArrayList<>(), transform, zIndex);
	}

	/**
	 * @param name
	 * @param zIndex
	 */
	public GameObject (String name, int zIndex) {
		this(name, new ArrayList<>(), new Transform(), zIndex);
	}

	/**
	 * @param transform
	 * @param zIndex
	 */
	public GameObject (Transform transform, int zIndex) {
		this("Default GameObject Name", new ArrayList<>(), new Transform(), zIndex);

	}

	/**
	 * @param transform
	 */
	public GameObject (Transform transform) {
		this("Default GameObject Name", new ArrayList<>(), transform, 0);
	}

	/**
	 * Creates an empty gameObject with an empty Transform and no Components.
	 */
	public GameObject () {
		this("Empty GameObject", new ArrayList<>(), new Transform(), 0);
	}

	/**
	 * Called once on gameObject creation, also starts any components that are passed to the constructor.
	 */
	public void start () {
		for (int i = 0; i < components.size(); i ++) {
			components.get(i).start();
		}
	}

	/**
	 * Called once every frame for each GameObject, calls the update method for each component it contains
	 */
	public void update (float dt) {
		for (int i = 0; i < components.size(); i ++) {
			components.get(i).update(dt);
		}
	}

	/**
	 * @return Transform of the gameObject
	 */
	public Transform getTransform () {
		return this.transform;
	}

	/**
	 * Takes a Transform as a parameter and sets this instance to a copy of that transform
	 * @param transform
	 */
	public void setTransform (Transform t) {
		this.transform = t.copy();
	}

	public void setTransformX (float x) {
		this.transform.setX(x);
	}

	public void setTransformY (float y) {
		this.transform.setY(y);
	}

	public void setTransformWidth (float w) {
		this.transform.setWidth(w);
	}

	public void setTransformHeight (float h) {
		this.transform.setHeight(h);
	}

	public int zIndex () {
		return zIndex;
	}

	public void setZindex (int z) {
		zIndex = z;
	}

	public String getName () {
		return name;
	}

	/**
	 * Takes a parameter of a class that extends component and returns it if it is contained in the GameObject's list of components.
	 * @param class of component (ie. "SpriteRenderer.class")
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
	 * @param class of component (ie. "SpriteRenderer.class")
	 */
	public <T extends Component> void removeComponent (Class<T> componentClass) {
		for (int i = 0; i < components.size(); i ++) {
			Component c = components.get(i);
			if (componentClass.isAssignableFrom(c.getClass())) {
				components.remove(i);
				return;
			}
		}
	}

	/**
	 * Adds a new component to the GameObject's list
	 * @param component
	 */
	public void addComponent (Component c) {
		this.components.add(c);
		c.gameObject = this;
	}

	/**
	 * @return List of Components in GameObject
	 */
	public List<Component> getComponents () {
		return components;
	}
}
