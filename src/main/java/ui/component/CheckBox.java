package ui.component;

import org.lwjgl.glfw.GLFW;
import ui.Component;
import ui.EventHandler;
import util.Observable;

/**
 * @author Juyas
 * @version 09.11.2021
 * @since 09.11.2021
 */
public class CheckBox extends Component {

    /**
     * The text displayed for the CheckBox
     */
    private String text;

    /**
     * Whether the CheckBox is currently checked
     */
    private Observable<Boolean> checked;

    public CheckBox(String text, boolean preChecked) {
        this.text = text;
        this.checked = new Observable<>(preChecked);
        this.getEventHandler().registerListener(EventHandler.Event.MOUSE_CLICK, eventHandler -> {
            if (eventHandler.isMouseButtonClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT))
                this.checked.setValue(!this.checked.getValue());
        });
    }

    public CheckBox(String text) {
        this(text, false);
    }

    public void setChecked(boolean checked) {
        this.checked.setValue(checked);
    }

    public Observable<Boolean> getChecked() {
        return checked;
    }

    public boolean isChecked() {
        return checked.getValue();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}