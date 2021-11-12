package ui;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class FocusManager {

    private static final FocusManager instance = new FocusManager();

    public static FocusManager getInstance() {
        return instance;
    }

    private Component focused = null;

    private FocusManager() {

    }

    private void unfocus() {
        if (focused != null) {
            focused.setFocused(false);
        }
    }

    private void focus(Component component) {
        this.focused = component;
        component.setFocused(true);
    }

    public Component getFocusedComponent() {
        return focused;
    }

    public static void requestFocus(Component component) {
        getInstance().unfocus();
        getInstance().focus(component);
    }

}