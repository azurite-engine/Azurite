package ui;

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

    public UIContainer(UILayout layout) {
        this.layout = layout;
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
        layout.updateComponents(this);
        components.forEach(UIComponent::update);
    }

    @Override
    public void draw() {
        components.stream().filter(UIComponent::isVisible).forEach(UIComponent::draw);
    }

}