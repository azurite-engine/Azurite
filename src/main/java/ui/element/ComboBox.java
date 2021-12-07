package ui.element;

import ui.Element;

import java.util.List;

/**
 * @author Juyas
 * @version 09.11.2021
 * @since 09.11.2021
 */
public class ComboBox extends Element implements TextHolder, ValueHolder {

    /**
     * The list of options.
     */
    private List<String> options;

    /**
     * the selected option
     */
    private int selected;

    /**
     * Whether the comboBox is currently expanded
     */
    private boolean expanded;

    /**
     * The currently displayed text.
     */
    private String currentText;

    public ComboBox(List<String> options, int selected, String shownText) {
        this.options = options;
        this.selected = selected;
        this.currentText = shownText;
        this.expanded = false;
    }

    public ComboBox(List<String> options) {
        this(options, -1, null);
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
        if (selected >= this.options.size())
            this.selected = -1;
    }

    public void setSelected(int selected) {
        this.selected = Math.abs(selected) % options.size();
        this.currentText = options.get(this.selected);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getSelected() {
        return selected;
    }

    public void setCurrentText(String currentText) {
        setText(currentText);
    }

    public String getCurrentText() {
        return currentText;
    }

    @Override
    public String getText() {
        return currentText;
    }

    @Override
    public void setText(String newText) {
        this.currentText = newText;
        this.selected = options.indexOf(newText);
    }

    @Override
    public float getValue() {
        return getSelected();
    }

    @Override
    public void setValue(float newValue) {
        setSelected((int) newValue);
    }

}