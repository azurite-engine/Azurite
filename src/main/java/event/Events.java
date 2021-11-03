package event;

/**
 * Collection of Event Nodes for various Events
 *
 * @author VoxelRifts
 */
public class Events {
    /**
     * Triggered whenever the window resizes; more specifically, whenever the
     * callback set with {@code glfwWindowResizeCallback()} is run by the system.
     *
     * @see graphics.Window
     */
    public static EventNode<EventData.WindowResizeEventData> windowResizeEvent;
    /**
     * Triggered whenever a key is pressed; more specifically, whenever the
     * callback set with {@code glfwSetKeyCallback()} is run by the system.
     *
     * @see input.Keyboard
     */
    public static EventNode<EventData.KeyEventData> keyEvent;
    /**
     * Triggered whenever the mouse scroll wheel is used; more specifically,
     * whenever the callback set with {@code glfwSetScrollCallback()} is run
     * by the system.
     *
     * @see input.Mouse
     */
    public static EventNode<EventData.MouseScrollEventData> mouseScrollEvent;
    /**
     * Triggered whenever a mouse button is pressed; more specifically,
     * whenever the callback set with {@code glfwSetMouseButtonCallback()}
     * is run by the system.
     *
     * @see input.Mouse
     */
    public static EventNode<EventData.MouseButtonEventData> mouseButtonEvent;

    // initialization
    static {
        windowResizeEvent = new EventNode<>();
        keyEvent = new EventNode<>();
        mouseScrollEvent = new EventNode<>();
        mouseButtonEvent = new EventNode<>();
    }
}
