package ui.component;

import input.Mouse;
import org.lwjgl.glfw.GLFW;
import ui.UIComponent;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class Button extends UIComponent implements TextHolder {

    private String text;

    public Button(String label) {
        this.text = getText();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void update() {
        if (isMouseOnThis()) {
            if (Mouse.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
                System.out.println("WORKS GREAT");
            }
        }
    }

    @Override
    public void draw() {

    }

}