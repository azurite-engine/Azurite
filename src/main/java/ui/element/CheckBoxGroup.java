package ui.element;

import graphics.Sprite;
import org.joml.Vector2f;
import ui.Element;
import util.Engine;
import util.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Juyas
 * @version 09.11.2021
 * @since 09.11.2021
 */
public class CheckBoxGroup extends Element {

    /**
     * The list of options.
     */
    private List<String> options;

    /**
     * The selected options in the list.
     */
    private final List<String> selected;

    private List<CheckBox> boxes = new ArrayList<>();

    protected final CheckBox.Type type;

    public CheckBoxGroup(CheckBox.Type type, List<String> options, Sprite unchecked, Sprite checked, Vector2f position) {
        this.options = options;
        this.selected = new ArrayList<>();
        this.type = type;

        float boxHeight = unchecked.getHeight();

        frame.setX(position.x());
        frame.setY(position.y());

        frame.setHeight(boxHeight * options.size());
        frame.setWidth(400);


        for (int i = 0; i < options.size(); i ++) {
            CheckBox box = new CheckBox(this, i, options.get(i), unchecked, checked, this.frame, i * boxHeight);
            boxes.add(box);
        }

        Engine.scenes().currentScene().addUIElement(this);
    }

    public void addOption(String option) {
        this.options.add(option);
    }

    public void removeOption(String option) {
        this.options.remove(option);
    }

    public List<String> getSelected() {
        return selected;
    }

    public List<String> getOptions() {
        return options;
    }

    public void select (int select) {
        CheckBox box = boxes.get(select);

        if (box.isChecked()) {
            selected.remove(box.getText());
            box.setChecked(false);
        } else {
            selected.add(box.getText());
            box.setChecked(true);
        }

        if (this.type == CheckBox.Type.RADIO_SELECT) {
            for (int i = 0; i < options.size(); i ++) {
                if (i == select) continue;
                boxes.get(i).setChecked(false);
                selected.remove(boxes.get(i).getText());
            }
        }
    }

    @Override
    public void update () {
        super.update();
    }

}