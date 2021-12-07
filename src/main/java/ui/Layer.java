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
    private final List<Element> elements;
    private final Frame layerFrame;

    public Layer() {
        this.elements = new ArrayList<>();
        this.active = true;
        this.layerFrame = new Frame();
    }

    public Layer(float x, float y, float width, float height) {
        this.elements = new ArrayList<>();
        this.active = true;
        this.layerFrame = new Frame(x, y, width, height);
    }

    public void update() {
        if (!active) return;
        //first make sure all top level components are enclosed before updating
        elements.forEach(comp -> comp.getFrame().ensureEnclosure(layerFrame));
        elements.forEach(Element::update);
        if (!layerFrame.isInFrame(Mouse.mouse) || elements.stream().noneMatch(Element::isMouseOnThis))
            CursorManager.resetCursor();
    }

    public void draw() {
        if (!active) return;
        elements.stream().filter(Element::isVisible).forEach(Element::draw);
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

    public void registerComponent(Element element) {
        this.elements.add(element);
    }

    public void unregisterComponent(Element element) {
        this.elements.remove(element);
    }

}