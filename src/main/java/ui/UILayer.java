package ui;

import input.Mouse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class UILayer {

    private boolean active;
    private final List<UIComponent> components;
    private final UIFrame layerFrame;

    public UILayer() {
        this.components = new ArrayList<>();
        this.active = true;
        this.layerFrame = new UIFrame();
    }

    public UILayer(float x, float y, float width, float height) {
        this.components = new ArrayList<>();
        this.active = true;
        this.layerFrame = new UIFrame(x, y, width, height);
    }

    public void update() {
        if (!active) return;
        //first make sure all top level components are enclosed before updating
        components.forEach(comp -> comp.getFrame().ensureEnclosure(layerFrame));
        components.forEach(UIComponent::update);
        if (!layerFrame.isInFrame(Mouse.mouse) || components.stream().noneMatch(UIComponent::isMouseOnThis))
            CursorManager.resetCursor();
    }

    public void draw() {
        if (!active) return;
        components.stream().filter(UIComponent::isVisible).forEach(UIComponent::draw);
    }

    public UIFrame getLayerFrame() {
        return layerFrame;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (!active) CursorManager.resetCursor();
    }

    public boolean isActive() {
        return active;
    }

    public void registerComponent(UIComponent component) {
        this.components.add(component);
    }

    public void unregisterComponent(UIComponent component) {
        this.components.remove(component);
    }

}