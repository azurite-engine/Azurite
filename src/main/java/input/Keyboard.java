package input;

import event.EventData;
import event.Events;
import graphics.Window;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class containing everything related to keyboard input. 
 * Implementations of GLFW key callbacks are all found here. getKey, getKeyDown, getKeyUp, etc...
 */
public class Keyboard {

    /**
     * A bitfield array to store button states.
     * First bit is key down
     * Second bit is key up
     * Third bit is key held
     */
    public static byte[] keystateBitfields;

    static {
        keystateBitfields = new byte[400];
    }

    /**
     * Subscribes to key events and sets up GLFW key callback.
     */
    public static void setupCallbacks() {
        glfwSetKeyCallback(Window.glfwWindow(), (w, keycode, scancode, action, mods) -> {
            switch (action) {
                case GLFW_PRESS: {
                    setKeyDownBit(keycode);
                    resetKeyUpBit(keycode);
                    resetKeyHeldBit(keycode);
                    break;
                }

                case GLFW_RELEASE: {
                    resetKeyDownBit(keycode);
                    setKeyUpBit(keycode);
                    resetKeyHeldBit(keycode);
                    break;
                }

                case GLFW_REPEAT: {
                    resetKeyDownBit(keycode);
                    resetKeyUpBit(keycode);
                    setKeyHeldBit(keycode);
                    break;
                }
            }

            Events.keyEvent.onEvent(new EventData.KeyEventData(keycode, scancode, action, mods));
        });
    }

    private static void setKeyDownBit(int keycode) {
        keystateBitfields[keycode] |= 0b00000001;
    }

    private static void resetKeyDownBit(int keycode) {
        keystateBitfields[keycode] &= 0b11111110;
    }

    private static void setKeyUpBit(int keycode) {
        keystateBitfields[keycode] |= 0b00000010;
    }

    private static void resetKeyUpBit(int keycode) {
        keystateBitfields[keycode] &= 0b11111101;
    }

    private static void setKeyHeldBit(int keycode) {
        keystateBitfields[keycode] |= 0b00000100;
    }

    private static void resetKeyHeldBit(int keycode) {
        keystateBitfields[keycode] &= 0b11111011;
    }

    /**
     * @param keycode keycode representing the key to be checked
     * @return Returns true if the key is currently pressed or held, otherwise returns false
     */
    public static boolean getKey(int keycode) {
        return glfwGetKey(Window.glfwWindow(), keycode) != GLFW_RELEASE;
    }

    /**
     * Returns true if a key is was just pressed, then returns false until the key is released and pressed again.
     *
     * @param keycode key-code representing the key to be checked.
     * @return Returns true if the key was just pressed, otherwise returns false.
     */
    public static boolean getKeyDown(int keycode) {
        return ((keystateBitfields[keycode] & 0b00000001) /*>> 0*/) != 0;
    }

    public static boolean keyDownOrHold(int keycode) {
        return getKeyDown(keycode) || getKeyHeld(keycode);
    }

    /**
     * Returns true if a key was just released.
     *
     * @param keycode key-code representing the key to be checked.
     * @return Returns true if the key was just released, otherwise returns false.
     */
    public static boolean getKeyUp(int keycode) {
        return ((keystateBitfields[keycode] & 0b00000010) >> 1) != 0;
    }

    /**
     * Returns true if a key was held.
     *
     * @param keycode key-code representing the key to be checked.
     * @return Returns true if the key was held, otherwise returns false.
     */
    public static boolean getKeyHeld(int keycode) {
        return ((keystateBitfields[keycode] & 0b00000100) >> 2) != 0;
    }
}