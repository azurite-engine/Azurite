package ui.component;

import ui.UIAlignment;
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

    /**
     * The orientation of the text in the CheckBox.
     * default is RIGHT - meaning the box to check is left and the text is right.
     */
    private UIAlignment textAlignment = UIAlignment.RIGHT;

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public UIAlignment getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(UIAlignment textAlignment) {
        this.textAlignment = textAlignment;
    }

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