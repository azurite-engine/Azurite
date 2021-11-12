package ui;

import input.Mouse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class Layer {

    private boolean active;
    private final List<Component> components;
    private final Frame layerFrame;

    public Layer() {
        this.components = new ArrayList<>();
        this.active = true;
        this.layerFrame = new Frame();
    }

    public Layer(float x, float y, float width, float height) {
        this.components = new ArrayList<>();
        this.active = true;
        this.layerFrame = new Frame(x, y, width, height);
    }

    public void update() {
        if (!active) return;
        //first make sure all top level components are enclosed before updating
        components.forEach(comp -> comp.getFrame().ensureEnclosure(layerFrame));
        components.forEach(Component::update);
        if (!layerFrame.isInFrame(Mouse.mouse) || components.stream().noneMatch(Component::isMouseOnThis))
            CursorManager.resetCursor();
    }

    public void draw() {
        if (!active) return;
        components.stream().filter(Component::isVisible).forEach(Component::draw);
    }

    public Frame getLayerFrame() {
        return layerFrame;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (!active) CursorManager.resetCursor();
    }

    public boolean isActive() {
        return active;
    }

    public void registerComponent(Component component) {
        this.components.add(component);
    }

    public void unregisterComponent(Component component) {
        this.components.remove(component);
    }

}