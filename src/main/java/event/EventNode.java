package event;

import java.util.LinkedList;
import java.util.List;

/**
 * <h1>Azurite</h1>
 * A class which can handle Event listeners by automatically calling all listeners whenever
 * the event {@code T} occurs. Whenever a specific event wants to be subscribed to, one passes
 * a lambda into the {@code subscribe} function as a callback. As an example, take
 * {@link Events#windowResizeEvent}:
 * <code>
 *     class WindowExample {
 *         private static void resizeWindow(EventData.WindowResizeEventData data) {
 *             glViewport(0, 0, data.x, data.y);
 *         }
 *
 *         public static void init() {
 *             Events.windowResizeEvent.subscribe(WindowExample::resizeWindow);
 *         }
 *     }
 * </code>
 *
 * @param <T> Type of Event Data this event is for
 */
public class EventNode<T extends EventData> {
    /**
     * The listeners that are subscribed to this event
     */
    private List<EventListener<T>> listeners;

    /**
     * A default constructor
     */
    public EventNode() {
        listeners = new LinkedList<>();
    }

    /**
     * Add a function to the listeners list. The function will be called when the event occurs
     *
     * @param listener The function that is to be called when the event occurs
     */
    public void subscribe(EventListener<T> listener) {
        listeners.add(listener);
    }

    /**
     * Notifies the node that an event has occurred. Notifies all listeners
     *
     * @param t the data that is to be given to all the listeners.
     */
    public void onEvent(T t) {
        listeners.forEach(listener -> listener.onEvent(t));
    }
}
