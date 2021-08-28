package event;

/**
 * Collection of Event Nodes for various Events
 *
 * @author VoxelRifts
 */
public class Events {
    public static EventNode<EventData.WindowResizeEventData> windowResizeEvent;
    public static EventNode<EventData.KeyEventData> keyEvent;
    public static EventNode<EventData.MouseScrollEventData> mouseScrollEvent;
    public static EventNode<EventData.MouseButtonEventData> mouseButtonEvent;

    static {
        windowResizeEvent = new EventNode<>();
        keyEvent = new EventNode<>();
        mouseScrollEvent = new EventNode<>();
        mouseButtonEvent = new EventNode<>();
    }
}
