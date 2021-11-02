package event;

/**
 * <h1>Azurite</h1>
 * A Simple listener interface.
 * Any class that implements this "subscribes" to the {@code EventData}
 * class put in the type parameter. Whenever such an event is triggered,
 * {@code this.onEvent(triggeredEvent)} is ran.
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
