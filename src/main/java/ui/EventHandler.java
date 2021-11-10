package ui;

import input.Mouse;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * @author Juyas
 * @version 10.11.2021
 * @since 10.11.2021
 */
public class EventHandler {

    private final UIComponent parent;
    private final HashMap<Event, Consumer<EventHandler>> listener;
    private boolean mouseIsOnComponent;
    private boolean[] mouseClick;

    public EventHandler(UIComponent parent) {
        this.parent = parent;
        this.listener = new HashMap<>();
        for (Event e : Event.values())
            this.listener.put(e, (eh) -> {
            });
        this.mouseIsOnComponent = false;
        this.mouseClick = new boolean[Mouse.mouseButton.length];
    }

    public void update() {
        if (parent.isMouseOnThis()) {
            if (mouseIsOnComponent) {
                callEvent(Event.MOUSE_HOVER);
            } else {
                callEvent(Event.MOUSE_ENTER);
                mouseIsOnComponent = true;
            }
            for (int i = 0; i < mouseClick.length; i++) {
                if (isMouseButtonClicked(i)) {
                    callEvent(Event.MOUSE_CLICK);
                }
            }
            this.mouseClick = Mouse.mouseButton;
        } else if (mouseIsOnComponent) {
            callEvent(Event.MOUSE_LEAVE);
        }
    }

    public UIComponent getComponent() {
        return parent;
    }

    public boolean isMouseButtonClicked(int button) {
        return mouseClick[button] && !Mouse.mouseButton[button];
    }

    public void callEvent(Event event) {
        listener.get(event).accept(this);
    }

    public enum Event {
        MOUSE_ENTER,
        MOUSE_LEAVE,
        MOUSE_HOVER,
        MOUSE_CLICK,
        GAIN_FOCUS,
        LOOSE_FOCUS
    }

}