package ui.element;

import graphics.Color;
import org.lwjgl.glfw.GLFW;
import ui.Frame;
import ui.RenderableElement;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class Button extends RenderableElement implements TextHolder {

    private String text;

    public Button(String label, Color color, Frame frame) {
        super(color, frame);
        this.text = label;
        this.cursor = GLFW.GLFW_CROSSHAIR_CURSOR;
    }

    public Button(String label, String path, Frame frame) {
        super(path, frame);
        this.text = label;
        this.cursor = GLFW.GLFW_CROSSHAIR_CURSOR;
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
    public void update () {
        super.update();

        if (isMouseOnThis()) {
            this.setColor(Color.RED);
        }
    }

}