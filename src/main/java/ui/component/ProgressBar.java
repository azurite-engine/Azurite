package ui.component;

import ui.Alignment;
import ui.Component;
import util.Utils;

/**
 * @author Juyas
 * @version 09.11.2021
 * @since 09.11.2021
 */
public class ProgressBar extends Component implements ValueHolder {

    /**
     * current state of the ProgressBar between 0 and 1
     */
    private float value;

    /**
     * In which direction the progressbar is filling up
     */
    private Alignment orientation;

    public ProgressBar() {
        this(0, Alignment.RIGHT);
    }

    public ProgressBar(float value, Alignment orientation) {
        this.value = value;
        this.orientation = orientation;
    }

    public Alignment getOrientation() {
        return orientation;
    }

    public void setOrientation(Alignment orientation) {
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