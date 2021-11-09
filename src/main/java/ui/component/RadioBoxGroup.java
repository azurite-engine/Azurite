package ui.component;

import ui.UIAlignment;
import ui.UIComponent;

import java.util.List;

/**
 * @author Juyas
 * @version 09.11.2021
 * @since 09.11.2021
 */
public class RadioBoxGroup extends UIComponent {

    /**
     * The list of options.
     */
    private final List<String> options;

    /**
     * The selected option in the list.
     * No option selected is -1.
     */
    private int selected;

    /**
     * The orientation of the text in each radio box.
     * default is RIGHT - meaning the box to check is left and the text is right.
     */
    private UIAlignment textAlignment = UIAlignment.RIGHT;

    public RadioBoxGroup(List<String> options, int preselected) {
        this.options = options;
        this.selected = preselected;
    }

    public RadioBoxGroup(List<String> options) {
        this(options, -1);
    }

    public void addOption(String option) {
        this.options.add(option);
    }

    public void removeOption(String option) {
        this.options.remove(option);
    }

    public void setTextAlignment(UIAlignment textAlignment) {
        this.textAlignment = textAlignment;
    }

    public UIAlignment getTextAlignment() {
        return textAlignment;
    }

    public int getSelected() {
        return selected;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getSelectedOption() {
        return options.get(getSelected());
    }

    public void setSelected(int selected) {
        this.selected = Math.abs(selected) % options.size();
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {

    }

}