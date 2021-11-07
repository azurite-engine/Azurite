package ui;

import graphics.Color;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public abstract class UIComponent {

    private UIContainer parent;

    private final UIFrame frame;

    private Color foregroundColor, backgroundColor;

    private boolean focussed;
    private boolean enabled;

    public UIComponent() {
        this.frame = new UIFrame();
        this.focussed = false;
        this.enabled = false;
        this.foregroundColor = Color.WHITE;
        this.backgroundColor = Color.WHITE;
        this.parent = null;
    }

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

    public UIFrame getFrame() {
        return frame;
    }

    public float getAbsoluteX() {
        if (getParent() == null) return getFrame().getX();
        return getParent().getAbsoluteX() + getFrame().getX();
    }

    public float getAbsoluteY() {
        if (getParent() == null) return getFrame().getY();
        return getParent().getAbsoluteY() + getFrame().getY();
    }

    public void update() {

    }

    public void draw() {

    }

}