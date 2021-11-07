package ui.layout;

import ui.UIComponent;
import ui.UIContainer;
import ui.UIFrame;

import java.util.List;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class BoxLayout implements UILayout {

    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    private final Orientation orientation;

    public BoxLayout(Orientation orientation) {
        this.orientation = orientation == null ? Orientation.VERTICAL : orientation;
    }

    @Override
    public void updateComponents(UIContainer container) {
        float value = orientation == Orientation.VERTICAL ? container.getFrame().getHeight() : container.getFrame().getWidth();
        List<UIComponent> components = container.getComponents();
        int size = components.size();
        float compSize = value / size;
        for (int i = 0; i < size; i++) {
            UIFrame frame = components.get(i).getFrame();
            if (orientation == Orientation.VERTICAL) {
                frame.setHeight(compSize);
                frame.setX(compSize * i);
            } else {
                frame.setWidth(compSize);
                frame.setY(compSize * i);
            }
        }
    }

}