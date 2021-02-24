package event;

import java.util.ArrayList;
import java.util.List;

public class EventNode<T extends EventData> {
    private List<EventListener<T>> listeners;

    public EventNode() {
        listeners = new ArrayList<>();
    }

    public void subscribe(EventListener<T> listener) {
        listeners.add(listener);
    }

    public void onEvent(T t) {
        listeners.forEach(listener -> listener.onEvent(t));
    }
}
