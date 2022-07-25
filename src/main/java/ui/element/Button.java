package ui.element;

import graphics.Color;
import graphics.Sprite;
import org.lwjgl.glfw.GLFW;
import ui.Frame;
import ui.RenderableElement;
import ui.Text;

/**
 * @author Juyas
 * @author Asher Haun
 * @version 07.11.2021
 * @since 15.5.2022
 */
public class Button extends RenderableElement implements TextHolder {

    private Text label;

    public Button(String label, Color backgroundColor, Color labelColor, Frame frame) {
        super(backgroundColor, frame);
        this.label = new Text(label, labelColor, 0, 0);
        this.label.setCentered(true);
        this.cursor = GLFW.GLFW_POINTING_HAND_CURSOR;
    }

    public Button(String label, String path, Color labelColor, Frame frame) {
        super(path, frame);
        this.label = new Text(label, labelColor, 0, 0);
        this.label.setCentered(true);
        this.cursor = GLFW.GLFW_POINTING_HAND_CURSOR;
    }

    public Button(String label, Sprite texture, Color labelColor, Frame frame) {
        super(texture, frame);
        this.label = new Text(label, labelColor, 0, 0);
        this.label.setCentered(true);
        this.cursor = GLFW.GLFW_POINTING_HAND_CURSOR;
    }

    @Override
    public String getText() {
        return label.getText();
    }

    @Override
    public void setText(String text) {
        this.label.change(text);
    }

    @Override
    public void update () {
        super.update();

        label.setPosition(getX() + frame.getWidth() / 2, getY() + frame.getHeight() / 2 - label.getHeight() / 2);

        if (isMouseOnThis()) {
            if (tintColor != null) {
                this.setColor(tintColor);
            }
        } else {
            this.setColor(defaultColor);
        }
    }
}