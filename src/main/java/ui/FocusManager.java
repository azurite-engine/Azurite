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

    private Element focused = null;

    private FocusManager() {

    }

    private void unfocus() {
        if (focused != null) {
            focused.setFocused(false);
        }
    }

    private void focus(Element element) {
        this.focused = element;
        element.setFocused(true);
    }

    public Element getFocusedComponent() {
        return focused;
    }

    public static void requestFocus(Element element) {
        getInstance().unfocus();
        getInstance().focus(element);
    }

}