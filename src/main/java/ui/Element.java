package ui;

import input.Mouse;
import org.lwjgl.glfw.GLFW;
import ui.fonts.Font;
import util.MathUtils;
import util.Observable;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public abstract class Element {

    private Container parent;

    /**
     * The position and dimension of this component.
     */
    protected Frame frame;

    /**
     * The event handler for mouse events on this component.
     * Is null until {@link this#getEventHandler()} is called for the first time to reduce workload.
     */
    protected EventHandler eventHandler;

    /**
     * The font used for text in this element.
     * If the font is null - the parent font is used instead
     */
    protected Font font;

    /**
     * The cursor id according to {@link CursorManager}
     */
    protected int cursor;

    //observable values
    protected final Observable<Boolean> focused;
    protected final Observable<Boolean> enabled;
    protected final Observable<Boolean> visible;

    //for keeping track, if the mouse was/is over this component in the last/current update
    protected boolean mouseOverThis;

    //optional information for the layout - e.g. positioning or orientation
    protected Object layoutInfo;

    protected int zIndex;

    public Element() {
        this.frame = new Frame();
        this.eventHandler = null;
        this.focused = new Observable<>(false);
        this.enabled = new Observable<>(true);
        this.visible = new Observable<>(true);
        this.parent = null;
        this.font = new Font();
        this.layoutInfo = null;
        this.zIndex = 1;
        this.mouseOverThis = false;
        this.cursor = GLFW.GLFW_ARROW_CURSOR;
    }

    public Element (Frame frame) {
        this();
        this.frame =  frame;
    }

    //------------ ------------ update function ------------ ------------

    public void update() {
        //to reduce redundant calculation, it gets- calculated each update once
        this.mouseOverThis = MathUtils.inRect(Mouse.mouse, getX(), getY(), getWidth(), getHeight());
        if (isMouseOnThis())
            CursorManager.requestCursor(this.cursor);
        if (eventHandler != null)
            eventHandler.update();
        postUpdate();
    }

    //------------ ------------ getter and setter ------------ ------------

    //intern method for setting the parent container
    protected void setParent(Container parent) {
        this.parent = parent;
    }

    /**
     * The font to use for this component.
     *
     * @return the font for this component or the parent font, if this component doesnt have its own.
     */
    public Font getFont() {
        if (this.font == null && getParent() != null)
            return getParent().getFont();
        return font;
    }

    /**
     * Set the font for this component.
     * Setting the font to <code>null</code> means this component will use the font of its parent container.
     *
     * @param font the new font
     * @see #getParent()#getFont()
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * The observable for the enabled state of the component.
     *
     * @return the observable for enabled
     */
    public Observable<Boolean> getEnabled() {
        return enabled;
    }

    /**
     * The observable for the focused state of the component.
     *
     * @return the observable for focused
     */
    public Observable<Boolean> getFocused() {
        return focused;
    }

    /**
     * The observable for the visible state of the component.
     *
     * @return the observable for visible
     */
    public Observable<Boolean> getVisible() {
        return visible;
    }

    /**
     * A focussed component might react to input differently.
     * e.g. a textfield might only receive input if it is focussed.
     *
     * @return whether the component is current focused
     */
    public boolean isFocused() {
        return focused.getValue();
    }

    /**
     * A disabled component shall not receive input and might displayed in a greyed out style.
     *
     * @return whether the component is enabled.
     */
    public boolean isEnabled() {
        return enabled.getValue();
    }

    /**
     * Set a cursor for this component. The cursor will change for time the mouse is over this component.
     *
     * @param id the id of the cursor as specified in {@link CursorManager}
     * @see CursorManager
     */
    public void setCursor(int id) {
        this.cursor = id;
    }

    /**
     * Get the current cursor.
     *
     * @return the current cursor
     */
    public int getCursor() {
        return cursor;
    }

    /**
     * Change the enabled state of this component.
     *
     * @param enabled the new state
     * @see #isEnabled()
     */
    public void setEnabled(boolean enabled) {
        this.enabled.setValue(enabled);
    }

    /**
     * Change the visibility state of this component.
     *
     * @param visible the new state
     * @see #isVisible()
     */
    public void setVisible(boolean visible) {
        this.visible.setValue(visible);
    }

    /**
     * An invisible component shall not be drawn to the screen.
     *
     * @return whether the component is visible
     */
    public boolean isVisible() {
        return visible.getValue();
    }

    /**
     * This method shall not be used without thought.
     * Please use {@link #requestFocus()} instead for most use cases.
     *
     * @param focused the new state
     * @see FocusManager
     */
    protected void setFocused(boolean focused) {
        this.focused.setValue(focused);
    }

    /**
     * This method requests the focus for this component from {@link FocusManager}.
     * The currently focused component will lose its focus and this component will gain it.
     *
     * @see #isFocused()
     */
    public void requestFocus() {
        FocusManager.requestFocus(this);
    }

    public Container getParent() {
        return parent;
    }

    /**
     * Check whether this component has an eventhandler.
     *
     * @return true, if and only if this component has an eventhandler
     */
    public boolean hasEventHandler() {
        return eventHandler != null;
    }

    /**
     * Get and/or create an eventhandler for this method.
     * Do not call this, if you just wanna check the eventhandler, use {@link #hasEventHandler()} for that.
     *
     * @return the eventhandler of this component.
     */
    public EventHandler getEventHandler() {
        if (eventHandler == null)
            eventHandler = new EventHandler(this);
        return eventHandler;
    }



    /**
     * The {@link Frame} of this component.
     * Contains the position relative to its parent and its size.
     *
     * @return the {@link Frame} of this component
     */
    public Frame getFrame() {
        return frame;
    }

    /**
     * Set optional layout information.
     *
     * @param layoutInfo the data required for some layouts
     */
    public void setLayoutInfo(Object layoutInfo) {
        this.layoutInfo = layoutInfo;
    }

    /**
     * Get optional layout information.
     *
     * @return layout information if it has been set for this component, otherwise null
     */
    public Object getLayoutInfo() {
        return layoutInfo;
    }

    /**
     * The z-index, primarily used for the rendering process.
     *
     * @return the z-index
     */
    public int zIndex() {
        return zIndex;
    }

    /**
     * Get the absolute X coordinate of this component.
     * To get relative coordinates use {@link #getFrame()}.
     *
     * @return the absolute X coordinate
     */
    public float getX() {
        if (getParent() == null) return getFrame().getX();
        return getParent().getX() + getFrame().getX();
    }

    /**
     * Get the absolute Y coordinate of this component.
     * To get relative coordinates use {@link #getFrame()}.
     *
     * @return the absolute Y coordinate
     */
    public float getY() {
        if (getParent() == null) return getFrame().getY();
        return getParent().getY() + getFrame().getY();
    }

    /**
     * The width of this component.
     *
     * @return the width of this component
     * @see #getFrame()#getWidth()
     */
    public float getWidth() {
        return getFrame().getWidth();
    }

    /**
     * The height of this component.
     *
     * @return the height of this component
     * @see #getFrame()#getHeight()
     */
    public float getHeight() {
        return getFrame().getHeight();
    }

    /**
     * Tells you if the mouse is currently hovering this component.
     *
     * @return true, if and only if the mouse is on this component
     */
    public boolean isMouseOnThis() {
        return this.mouseOverThis;
    }

    //------------ ------------ methods to override ------------ ------------

    public void postUpdate() {

    }

    public void draw() {

    }

}