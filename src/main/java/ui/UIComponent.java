package ui;

import graphics.Color;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public abstract class UIComponent {

    private UIContainer parent;

    private float x, y;
    private float width, height;

    private Color foregroundColor, backgroundColor;

    private boolean focussed;
    private boolean enabled;

    public boolean isFocussed() {
        return focussed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected void setFocussed(boolean focussed) {
        this.focussed = focussed;
    }

    public void requestFocus() {
        UIFocusManager.requestFocus(this);
    }

    protected void setParent(UIContainer parent) {
        this.parent = parent;
    }

    public UIContainer getParent() {
        return parent;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void update() {

    }

    public void draw() {

    }

}