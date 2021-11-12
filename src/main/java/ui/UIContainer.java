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

    /**
     * The layout of the component.
     * It will affect the {@link UIFrame} of all contained components and put them in some order.
     */
    private UILayout layout;

    /**
     * The list of all contained {@link UIComponent}'s
     */
    private final List<UIComponent> components;

    /**
     * Ensures on each update, that all contained components are inside this container by their {@link UIFrame}
     *
     * @see UIFrame#ensureEnclosure(UIFrame)
     */
    private boolean enclosureInsurance = true;

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

    /**
     * Get the layout of this container.
     *
     * @return the layout of the container
     */
    public UILayout getLayout() {
        return layout;
    }

    /**
     * Change the layout of the container - null will result in the {@link AbsoluteLayout}.
     *
     * @param layout the new layout
     */
    public void setLayout(UILayout layout) {
        this.layout = layout == null ? new AbsoluteLayout() : layout;
    }

    /**
     * Get the list of all components
     *
     * @return a list containing all components in this container
     */
    public List<UIComponent> getComponents() {
        return components;
    }

    /**
     * En- or Disable enclosureInsurance.
     *
     * @param eI the new value
     * @see this#enclosureInsurance
     */
    public void setEnclosureInsurance(boolean eI) {
        this.enclosureInsurance = eI;
    }

    /**
     * Adds a new component to this container.
     * Note, that a component can only have one parent container and therefore cannot be added to more than one container.
     *
     * @param component the component to add
     * @return whether the component was successfully added; false if and only if the component already has a parent
     */
    public boolean addComponent(UIComponent component) {
        //only one parent allowed
        if (component.getParent() != null) return false;
        components.add(component);
        component.setParent(this);
        return true;
    }

    /**
     * Removes a component and resets its parent if it has been removed.
     *
     * @param component the component to remove
     * @return true, if the component was removed, false if the component was not contained in this container
     */
    public boolean removeComponent(UIComponent component) {
        boolean remove = components.remove(component);
        if (remove) component.setParent(null);
        return remove;
    }

    @Override
    public void postUpdate() {
        if (!isEnabled()) return;
        layout.updateComponents(this);
        components.forEach(UIComponent::update);
        if (enclosureInsurance)
            components.forEach(comp -> comp.getFrame().ensureEnclosure(getFrame()));
    }

    @Override
    public void draw() {
        if (!isVisible()) return;
        super.draw();
        components.stream().filter(UIComponent::isVisible).forEach(UIComponent::draw);
    }

}