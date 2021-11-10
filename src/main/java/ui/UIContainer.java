package ui;

import ui.layout.AbsoluteLayout;
import ui.layout.UILayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class UIContainer extends UIComponent {

    private final UILayout layout;
    private final List<UIComponent> components;

    public UIContainer() {
        this(null);
    }

    public UIContainer(UILayout layout) {
        this.layout = layout == null ? new AbsoluteLayout() : layout;
        this.components = new ArrayList<>();
    }

    public UIContainer(float x, float y, float w, float h, UILayout layout) {
        getFrame().set(x, y, w, h);
        this.layout = layout == null ? new AbsoluteLayout() : layout;
        this.components = new ArrayList<>();
    }

    public UILayout getLayout() {
        return layout;
    }

    public List<UIComponent> getComponents() {
        return components;
    }

    public boolean addComponent(UIComponent component) {
        //only one parent allowed
        if (component.getParent() != null) return false;
        components.add(component);
        component.setParent(this);
        return true;
    }

    public boolean removeComponent(UIComponent component) {
        boolean remove = components.remove(component);
        if (remove) component.setParent(null);
        return remove;
    }

    @Override
    public void update() {
        if (!isEnabled()) return;
        layout.updateComponents(this);
        components.forEach(UIComponent::update);
        components.forEach(comp -> comp.getFrame().ensureEnclosure(getFrame()));
    }

    @Override
    public void draw() {
        if (!isVisible()) return;
        components.stream().filter(UIComponent::isVisible).forEach(UIComponent::draw);
    }

}