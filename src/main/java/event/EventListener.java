package event;

/**
 * A Simple listener interface
 *
 * @param <T> Type of Data for this listener
 */
@FunctionalInterface
public interface EventListener<T extends EventData> {
    /**
     * Called when the respective event occurs
     *
     * @param t Data for that event
     */
    void onEvent(T t);
}
