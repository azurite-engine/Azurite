package ui;

import fonts.Font;
import graphics.Color;
import input.Mouse;
import physics.collision.CollisionUtil;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public abstract class UIComponent {

    private UIContainer parent;

    private final UIFrame frame;

    private Color foregroundColor, backgroundColor;

    private boolean focused;
    private boolean enabled;
    private boolean visible;

    private Font font;

    private Object layoutInfo;

    public UIComponent() {
        this.frame = new UIFrame();
        this.focused = false;
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

    public boolean isfocused() {
        return focused;
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

    protected void setfocused(boolean focused) {
        this.focused = focused;
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

    public float getX() {
        if (getParent() == null) return getFrame().getX();
        return getParent().getX() + getFrame().getX();
    }

    public float getY() {
        if (getParent() == null) return getFrame().getY();
        return getParent().getY() + getFrame().getY();
    }

    public float getWidth() {
        return getFrame().getWidth();
    }

    public float getHeight() {
        return getFrame().getHeight();
    }

    public boolean isMouseOnThis() {
        return CollisionUtil.inRect(Mouse.mouse, getX(), getY(), getWidth(), getHeight());
    }

    public void update() {

    }

    public void draw() {

    }

}