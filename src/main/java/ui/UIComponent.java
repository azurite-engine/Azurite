package ui;

import fonts.Font;
import graphics.Color;
import input.Mouse;
import org.lwjgl.glfw.GLFW;
import physics.collision.CollisionUtil;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public abstract class UIComponent {

    private UIContainer parent;

    private final UIFrame frame;

    private EventHandler eventHandler;

    private Color foregroundColor, backgroundColor;
    private Font font;

    private int cursor;

    private boolean focused;
    private boolean enabled;
    private boolean visible;

    private boolean mouseOverThis;

    private int zIndex;

    private Object layoutInfo;

    public UIComponent() {
        this.frame = new UIFrame();
        this.eventHandler = null;
        this.focused = false;
        this.enabled = true;
        this.visible = true;
        this.foregroundColor = Color.WHITE;
        this.backgroundColor = Color.WHITE;
        this.parent = null;
        this.font = new Font();
        this.layoutInfo = null;
        this.zIndex = 1;
        this.mouseOverThis = false;
        this.cursor = GLFW.GLFW_ARROW_CURSOR;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public boolean isFocused() {
        return focused;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setCursor(int id) {
        this.cursor = id;
    }

    public int getCursor() {
        return cursor;
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

    protected void setFocused(boolean focused) {
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

    public boolean hasEventHandler() {
        return eventHandler != null;
    }

    public EventHandler getEventHandler() {
        if (eventHandler == null)
            eventHandler = new EventHandler(this);
        return eventHandler;
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

    public float zIndex() {
        return zIndex;
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
        return this.mouseOverThis;
    }

    public void update() {
        //to reduce redundant calculation, it gets calculated each update once
        this.mouseOverThis = CollisionUtil.inRect(Mouse.mouse, getX(), getY(), getWidth(), getHeight());
        if (isMouseOnThis())
            CursorManager.requestCursor(cursor);
        if (eventHandler != null)
            eventHandler.update();
    }

    public void draw() {

    }

}