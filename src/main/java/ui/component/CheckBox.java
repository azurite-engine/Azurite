package ui.component;

import org.lwjgl.glfw.GLFW;
import ui.EventHandler;
import ui.UIComponent;

/**
 * @author Juyas
 * @version 09.11.2021
 * @since 09.11.2021
 */
public class CheckBox extends UIComponent {

    /**
     * The text displayed for the CheckBox
     */
    private String text;

    /**
     * Whether the CheckBox is currently checked
     */
    private boolean checked;

    public CheckBox(String text, boolean preChecked) {
        this.text = text;
        this.checked = preChecked;
        this.getEventHandler().registerListener(EventHandler.Event.MOUSE_CLICK, eventHandler -> {
            if (eventHandler.isMouseButtonClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT))
                this.checked = !this.checked;
        });
    }

    public CheckBox(String text) {
        this(text, false);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}