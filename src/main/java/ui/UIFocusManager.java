package ui;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class UIFocusManager {

    private static final UIFocusManager instance = new UIFocusManager();

    public static UIFocusManager getInstance() {
        return instance;
    }

    private UIComponent focused = null;

    private UIFocusManager() {

    }

    private void unfocus() {
        if (focused != null)
            focused.setfocused(false);
    }

    private void focus(UIComponent component) {
        this.focused = component;
        component.setfocused(true);
    }

    public static void requestFocus(UIComponent component) {
        getInstance().unfocus();
        getInstance().focus(component);
    }

}