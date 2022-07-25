package ecs;

import org.joml.Vector2f;
import util.Log;
import util.debug.DebugPrimitive;

/**
 * Abstract structure for ECS Components.
 * It is highly recommended to use this when implementing any system that can/should be applied to a GameObject.
 */

public abstract class Component implements Comparable<Component> {

    /**
     * Parent GameObject
     */
    public GameObject gameObject = null;

    /**
     * Used to define a set order of components and their update cycle.
     * Required to maintain consistency in the update cycle.
     */
    private final ComponentOrder order;

    public Component(ComponentOrder order) {
        this.order = order == null ? ComponentOrder.POST_DRAW : order;
        if (order == null)
            Log.warn("Component no ComponentOrder created", 1);
    }

    public Component() {
        this.order = ComponentOrder.POST_DRAW;
    }

    /**
     * Called once on Component initialization.
     */
    public void start() {
    }

    /**
     * Called once per frame for each Component
     *
     * @param dt Engine.deltaTime
     */
    public void update(float dt) {
    }

    /**
     * Get an array of debug lines to be rendered in debug mode
     *
     * @return an array of lines to be rendered in debug mode
     */
    public DebugPrimitive[] debug() {
        return null;
    }

    /**
     * Shortcut to get the current position of the parent gameobject.
     *
     * @return the current position of the parent gameobject
     */
    protected Vector2f position() {
        return gameObject.getReadOnlyPosition();
    }

    /**
     * Shortcut to overwrite the current position of the parent gameobject.
     *
     * @return the current position of the parent gameobject
     */
    protected void setPosition(Vector2f position) {
        gameObject.getPositionData()[0] = position.x;
        gameObject.getPositionData()[1] = position.y;
    }

    //this method is primarily used to keep all components in order to update them properly
    @Override
    public int compareTo(Component o) {
        return order.o - o.order.o;
    }

    /**
     * Called when component is removed from GameObject.
     */
    public void remove() {
    }

    public enum ComponentOrder {
        PRE_CALC(0),
        INPUT(5),
        TRANSFORM(10),
        POST_TRANSFORM(20),
        COLLISION(30),
        DRAW(40),
        POST_DRAW(50);

        private final int o;

        ComponentOrder(int order) {
            this.o = order;
        }
    }

}
