package ui;

import ui.layout.AbsoluteLayout;
import ui.layout.ContainerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class Container extends Element {

    /**
     * The layout of the component.
     * It will affect the {@link Frame} of all contained components and put them in some order.
     */
    private ContainerLayout layout;

    /**
     * The list of all contained {@link Element}'s
     */
    private final List<Element> elements;

    /**
     * Ensures on each update, that all contained components are inside this container by their {@link Frame}
     *
     * @see Frame#ensureEnclosure(Frame)
     */
    private boolean enclosureInsurance = true;

    public Container() {
        this(null);
    }

    public Container(ContainerLayout layout) {
        this.layout = layout == null ? new AbsoluteLayout() : layout;
        this.elements = new ArrayList<>();
    }

    public Container(float x, float y, float w, float h, ContainerLayout layout) {
        getFrame().set(x, y, w, h);
        this.layout = layout == null ? new AbsoluteLayout() : layout;
        this.elements = new ArrayList<>();
    }

    /**
     * Get the layout of this container.
     *
     * @return the layout of the container
     */
    public ContainerLayout getLayout() {
        return layout;
    }

    /**
     * Change the layout of the container - null will result in the {@link AbsoluteLayout}.
     *
     * @param layout the new layout
     */
    public void setLayout(ContainerLayout layout) {
        this.layout = layout == null ? new AbsoluteLayout() : layout;
    }

    /**
     * Get the list of all components
     *
     * @return a list containing all components in this container
     */
    public List<Element> getElements() {
        return elements;
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
     * @param element the component to add
     * @return whether the component was successfully added; false if and only if the component already has a parent
     */
    public boolean addElement(Element element) {
        //only one parent allowed
        if (element.getParent() != null) return false;
        elements.add(element);
        element.setParent(this);
        return true;
    }

    /**
     * Removes a component and resets its parent if it has been removed.
     *
     * @param element the component to remove
     * @return true, if the component was removed, false if the component was not contained in this container
     */
    public boolean removeComponent(Element element) {
        boolean remove = elements.remove(element);
        if (remove) element.setParent(null);
        return remove;
    }

    @Override
    public void postUpdate() {
        if (!isEnabled()) return;
        layout.updateComponents(this);
        elements.forEach(Element::update);
        if (enclosureInsurance)
            elements.forEach(comp -> comp.getFrame().ensureEnclosure(getFrame()));
    }

    @Override
    public void draw() {
        if (!isVisible()) return;
        super.draw();
        elements.stream().filter(Element::isVisible).forEach(Element::draw);
    }

}