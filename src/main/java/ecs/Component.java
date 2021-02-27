package ecs;

/**
 * Abstract structure for ECS Components.
 * It is highly recommended to use this when implementing any system that can/should be applied to a GameObject.
 */
public abstract class Component {

	/** Parent GameObject */
	public GameObject gameObject = null;

	/**
	 * Called once on Component initialization.
	 */
	public void start () {}

	/**
	 *  Called once per frame for each Component
	 * @param dt Engine.deltaTime
	 */
	public void update (float dt) {}

}
