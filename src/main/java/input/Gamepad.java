package input;

import util.Log;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class containing everything related to gamepad input.
 * <br>
 * https://github.com/LWJGL/lwjgl3-wiki/wiki/2.6.3-Input-handling-with-GLFW#joystick-input
 */
public class Gamepad {

    /**
     * @return Returns an int with the number of connected controllers.
     */
    public static int controllersAvailable() {
        int total = 0;
        for (int i = 0; i < 10; i++) {
            if (glfwJoystickPresent(i)) {
                total++;
            }
        }
        return total;
    }

    /**
     * @param controllerId An int representing the ID of a gamepad or controller
     * @param button       An int representing the button to be checked.
     * @return Returns a boolean true if the button is pressed, otherwise, it returns false.
     */
    public static boolean buttonPressed(int controllerId, int button) {
        try {
            return glfwGetJoystickButtons(controllerId).get(button) == 1;
        } catch (NullPointerException e) {
            Log.fatal("no controller attached on " + controllerId + ".", 1);
            return false;
        }
    }

    /**
     * @param controllerId An int representing the ID of a gamepad or controller
     * @param axis         An int representing the axis to be checked.
     * @return Returns a float representing the directional state of the axis.
     */
    public static float axis(int controllerId, int axis) {
        try {
            float x = glfwGetJoystickAxes(controllerId).get(axis);
            if (Math.abs(x) < 0.1) {
                x = 0;
            }
            return x;
        } catch (NullPointerException e) {
            Log.fatal("no controller attached on " + controllerId + ".", 1);
            return 0;
        }
    }
}
