package ui.layout;

import ui.UIContainer;

import java.util.Arrays;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class BorderLayout implements UILayout {

    public enum Orientation {
        CENTER(1),
        TOP(2),
        BOTTOM(4),
        LEFT(8),
        RIGHT(16);

        private final int num;

        Orientation(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public static int toNum(Orientation... orientations) {
            if (orientations == null || orientations.length == 0) return 0;
            return Arrays.stream(orientations).map(Orientation::getNum).reduce(Integer::sum).get();
        }

        public static boolean isCentered(int num) {
            return num % 2 == 1;
        }

    }

    @Override
    public void updateComponents(UIContainer container) {
        //TODO
    }

}