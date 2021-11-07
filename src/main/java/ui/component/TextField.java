package ui.component;

import ui.UIComponent;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class TextField extends UIComponent implements TextHolder {

    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {

    }

}