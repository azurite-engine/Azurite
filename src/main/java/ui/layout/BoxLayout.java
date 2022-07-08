package ui.layout;

import ui.Element;
import ui.Container;
import ui.Frame;

import java.util.List;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class BoxLayout implements ContainerLayout {

    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    private final Orientation orientation;

    public BoxLayout(Orientation orientation) {
        this.orientation = orientation == null ? Orientation.VERTICAL : orientation;
    }

    @Override
    public void updateComponents(Container container) {
        float value = orientation == Orientation.VERTICAL ? container.getHeight() : container.getWidth();
        List<Element> elements = container.getElements();
        int size = elements.size();
        float compSize = value / size;
        for (int i = 0; i < size; i++) {
            Frame frame = elements.get(i).getFrame();
            if (orientation == Orientation.VERTICAL) {
                frame.setWidth(container.getWidth());
                frame.setHeight(compSize);
                frame.setX(0);
                frame.setY(compSize * i);
            } else {
                frame.setWidth(compSize);
                frame.setHeight(container.getHeight());
                frame.setX(compSize * i);
                frame.setY(0);
            }
        }
    }

}