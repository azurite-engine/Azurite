package ui;

import fonts.Font;
import graphics.Color;
import input.Mouse;
import org.lwjgl.glfw.GLFW;
import physics.collision.CollisionUtil;
import util.Observable;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public abstract class UIComponent {

    private UIContainer parent;

    /**
     * The position and dimension of this component.
     */
    private final UIFrame frame;

    /**
     * The event handler for mouse events on this component.
     * Is null until {@link this#getEventHandler()} is called for the first time to reduce workload.
     */
    private EventHandler eventHandler;

    /**
     * The font used for text in this component.
     * If the font is null - the parent font is used instead
     */
    private Font font;

    /**
     * The cursor id according to {@link CursorManager}
     */
    private int cursor;

    //observable values
    private final Observable<Color> foregroundColor;
    private final Observable<Color> backgroundColor;
    private final Observable<Boolean> focused;
    private final Observable<Boolean> enabled;
    private final Observable<Boolean> visible;

    //for keeping track, if the mouse was/is over this component in the last/current update
    private boolean mouseOverThis;

    //optional information for the layout - e.g. positioning or orientation
    private Object layoutInfo;

    private int zIndex;

    public UIComponent() {
        this.frame = new UIFrame();
        this.eventHandler = null;
        this.focused = new Observable<>(false);
        this.enabled = new Observable<>(true);
        this.visible = new Observable<>(true);
        this.foregroundColor = new Observable<>(Color.BLACK);
        this.backgroundColor = new Observable<>(Color.WHITE);
        this.parent = null;
        this.font = new Font();
        this.layoutInfo = null;
        this.zIndex = 1;
        this.mouseOverThis = false;
        this.cursor = GLFW.GLFW_ARROW_CURSOR;
    }

    //------------ ------------ update function ------------ ------------

    public final void update() {
        //to reduce redundant calculation, it gets calculated each update once
        this.mouseOverThis = CollisionUtil.inRect(Mouse.mouse, getX(), getY(), getWidth(), getHeight());
        if (isMouseOnThis())
            CursorManager.requestCursor(cursor);
        if (eventHandler != null)
            eventHandler.update();
        postUpdate();
    }

    //------------ ------------ getter and setter ------------ ------------

    //intern method for setting the parent container
    protected void setParent(UIContainer parent) {
        this.parent = parent;
    }

    public Font getFont() {
        if (this.font == null && getParent() != null)
            return getParent().getFont();
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Observable<Boolean> getEnabled() {
        return enabled;
    }

    public Observable<Boolean> getFocused() {
        return focused;
    }

    public Observable<Boolean> getVisible() {
        return visible;
    }

    public boolean isFocused() {
        return focused.getValue();
    }

    public boolean isEnabled() {
        return enabled.getValue();
    }

    public void setCursor(int id) {
        this.cursor = id;
    }

    public int getCursor() {
        return cursor;
    }

    public void setEnabled(boolean enabled) {
        this.enabled.setValue(enabled);
    }

    public void setVisible(boolean visible) {
        this.visible.setValue(visible);
    }

    public boolean isVisible() {
        return visible.getValue();
    }

    protected void setFocused(boolean focused) {
        this.focused.setValue(focused);
    }

    public void requestFocus() {
        UIFocusManager.requestFocus(this);
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
        return backgroundColor.getValue();
    }

    public Color getForegroundColor() {
        return foregroundColor.getValue();
    }

    public Observable<Color> getBackgroundColorObs() {
        return backgroundColor;
    }

    public Observable<Color> getForegroundColorObs() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor.setValue(foregroundColor);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor.setValue(backgroundColor);
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

    //------------ ------------ methods to override ------------ ------------

    public void postUpdate() {

    }

    public void draw() {

    }

}