package ui;

import fonts.Font;
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
    private boolean visible;

    private Font font;

    private Object layoutInfo;

    public UIComponent() {
        this.frame = new UIFrame();
        this.focussed = false;
        this.enabled = true;
        this.visible = true;
        this.foregroundColor = Color.WHITE;
        this.backgroundColor = Color.WHITE;
        this.parent = null;
        this.font = new Font();
        this.layoutInfo = null;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
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

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
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

    public void setLayoutInfo(Object layoutInfo) {
        this.layoutInfo = layoutInfo;
    }

    public Object getLayoutInfo() {
        return layoutInfo;
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