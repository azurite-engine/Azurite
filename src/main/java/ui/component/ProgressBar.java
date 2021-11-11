package ui.component;

import ui.UIAlignment;
import ui.UIComponent;
import util.Utils;

/**
 * @author Juyas
 * @version 09.11.2021
 * @since 09.11.2021
 */
public class ProgressBar extends UIComponent implements ValueHolder {

    /**
     * current state of the ProgressBar between 0 and 1
     */
    private float value;

    /**
     * In which direction the progressbar is filling up
     */
    private UIAlignment orientation;

    public ProgressBar() {
        this.value = 0;
        this.orientation = UIAlignment.RIGHT;
    }

    public UIAlignment getOrientation() {
        return orientation;
    }

    public void setOrientation(UIAlignment orientation) {
        this.orientation = orientation;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public void setValue(float newValue) {
        this.value = Utils.constrain(newValue, 0, 1);
    }

}