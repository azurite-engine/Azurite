package ui;

import input.Mouse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * @author Juyas
 * @version 10.11.2021
 * @since 10.11.2021
 */
public class EventHandler {

    private final Element parent;
    private final HashMap<Event, Consumer<EventHandler>> listener;
    private boolean mouseIsOnComponent;
    private boolean[] mouseClick;

    public EventHandler(Element parent) {
        this.parent = parent;
        this.listener = new HashMap<>();
        for (Event e : Event.values())
            this.listener.put(e, (eh) -> {
            });
        this.mouseIsOnComponent = false;
        this.mouseClick = new boolean[Mouse.mouseButton.length];
    }

    public void update() {
        //read events and call them
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
            this.mouseClick = Arrays.copyOf(Mouse.mouseButton, Mouse.mouseButton.length);
        } else if (mouseIsOnComponent) {
            callEvent(Event.MOUSE_LEAVE);
            mouseIsOnComponent = false;
        }
    }

    /**
     * Register a new event listener for this event handler and this component.
     *
     * @param event    the event to listen for
     * @param listener the listener containing logic to be executed if the specified event happens
     */
    public void registerListener(Event event, Consumer<EventHandler> listener) {
        this.listener.put(event, this.listener.get(event).andThen(listener));
    }

    public Element getElement() {
        return parent;
    }

    /**
     * Check, whether a mouse button has been clicked (pressed and released again) in this update loop.
     * Should be used inside the {@link Event#MOUSE_CLICK}, will probably lead to false in any other case.
     *
     * @param button the mouse button to check, e.g. {@link org.lwjgl.glfw.GLFW#GLFW_MOUSE_BUTTON_LEFT}
     * @return true if and only if the button has been clicked
     */
    public boolean isMouseButtonClicked(int button) {
        return mouseClick[button] && !Mouse.mouseButton[button];
    }

    /**
     * Call a specific event. Note: This might lead to unexpected and unwanted behaviour, when used wrongly.
     *
     * @param event the event to call
     */
    public void callEvent(Event event) {
        listener.get(event).accept(this);
    }

    public enum Event {
        MOUSE_ENTER,
        MOUSE_LEAVE,
        MOUSE_HOVER,
        MOUSE_CLICK
    }

}