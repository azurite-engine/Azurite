package event;

@FunctionalInterface
public interface EventListener<T extends EventData> {
    void onEvent(T t);
}
