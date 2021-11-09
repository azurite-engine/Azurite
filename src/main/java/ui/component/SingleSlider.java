package ui.component;

import ui.UIComponent;
import util.Utils;

/**
 * @author Juyas
 * @version 09.11.2021
 * @since 09.11.2021
 */
public class SingleSlider extends UIComponent implements ValueHolder {

    /**
     * the maximum and minimum of the slider
     */
    private float minScale, maxScale;

    /**
     * the current scale of the slider between min=0 and max=1
     */
    private float currentScale;

    /**
     * whether the numbers of min, max and current should be shown below the start, end and slider node.
     */
    private boolean showNumbers;

    public SingleSlider(float minScale, float maxScale) {
        this.minScale = minScale;
        this.maxScale = maxScale;
        this.currentScale = 0;
    }

    public void setNumbersShown(boolean showNumbers) {
        this.showNumbers = showNumbers;
    }

    public boolean numbersShown() {
        return showNumbers;
    }

    public float getCurrentScale() {
        return Utils.map(currentScale, 0, 1, minScale, maxScale);
    }

    public float getCurrentRelativeScale() {
        return currentScale;
    }

    public float getMaxScale() {
        return maxScale;
    }

    public float getMinScale() {
        return minScale;
    }

    public void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
    }

    public void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
    }

    public void setMinScale(float minScale) {
        this.minScale = minScale;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {

    }

    @Override
    public float getValue() {
        return getCurrentScale();
    }

    @Override
    public void setValue(float newValue) {
        setCurrentScale(Utils.map(newValue, minScale, maxScale, 0, 1));
    }
}