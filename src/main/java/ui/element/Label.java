package ui.element;

import ui.Element;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class Label extends Element implements TextHolder {

    /**
     * The text displayed on the label.
     */
    private String text;

    public Label(String text) {
        this.text = text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}