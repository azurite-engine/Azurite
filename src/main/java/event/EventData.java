package event;

/**
 * A base class to be extended by classes that are going to function as Data for the Event System
 */
public class EventData {
    /**
     * Event Data for the Window Resized Event
     */
    public static class WindowResizeEventData extends EventData {
        public final int x;
        public final int y;

        public WindowResizeEventData(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Event Data for the Key Event
     */
    public static class KeyEventData extends EventData {
        public final int keycode;
        public final int scancode;
        public final int action;
        public final int modifiers;

        public KeyEventData(int keycode, int scancode, int action, int modifiers) {
            this.keycode = keycode;
            this.scancode = scancode;
            this.action = action;
            this.modifiers = modifiers;
        }
    }

    /**
     * Event Data for the Mouse Scroll Event
     */
    public static class MouseScrollEventData extends EventData {
        public final double xScroll;
        public final double yScroll;

        public MouseScrollEventData(double xScroll, double yScroll) {
            this.xScroll = xScroll;
            this.yScroll = yScroll;
        }
    }

    /**
     * Event Data for the Mouse Button Event
     */
    public static class MouseButtonEventData extends EventData {
        public final int button;
        public final int action;
        public final int modifiers;

        public MouseButtonEventData(int button, int action, int modifiers) {
            this.button = button;
            this.action = action;
            this.modifiers = modifiers;
        }
    }

    /**
     * Event Data for the Trigger Enter Event. Empty
     */
    public static class TriggerEnterEvent extends EventData {
    }

    /**
     * Event Data for the Trigger Exit Event. Empty
     */
    public static class TriggerExitEvent extends EventData {
    }
}
