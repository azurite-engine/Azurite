package ui.element;

import ui.Element;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class Button extends Element implements TextHolder {

    private String text;

    public Button(String label) {
        this.text = label;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

}