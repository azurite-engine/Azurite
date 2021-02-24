package event;

public class EventData {
    /**
     * Window Resized Event Data
     */
    public static class WindowResizeEventData extends EventData {
        public final int x;
        public final int y;

        public WindowResizeEventData(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

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

    public static class MouseScrollEventData extends EventData {
        public final double xScroll;
        public final double yScroll;

        public MouseScrollEventData(double xScroll, double yScroll) {
            this.xScroll = xScroll;
            this.yScroll = yScroll;
        }
    }

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
}
