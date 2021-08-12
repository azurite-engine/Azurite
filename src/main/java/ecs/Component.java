package ecs;

import util.debug.DebugLine;
import util.debug.DebugPrimitive;

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

	/**
	 * Called when component is removed from GameObject.
	 */
	public void remove() {}

	/**
	 * Get an array of debug lines to be rendered in debug mode
	 *
	 * @return an array of lines to be rendered in debug mode
	 */
	public DebugPrimitive[] debug() { return null; }
}
