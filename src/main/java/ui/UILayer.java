package ui;

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

    public UILayer() {
        this.components = new ArrayList<>();
        this.active = true;
    }

    public void update() {
        if (!active) return;
        components.forEach(UIComponent::update);
    }

    public void draw() {
        if (!active) return;
        components.forEach(UIComponent::draw);
    }

    public void setActive(boolean active) {
        this.active = active;
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