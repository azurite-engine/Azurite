package ui;

import graphics.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

import java.util.HashMap;

/**
 * @author Juyas
 * @version 10.11.2021
 * @since 10.11.2021
 */
public class CursorManager {

    private final HashMap<Integer, Long> cursors;
    private int defaultCursor = GLFW.GLFW_ARROW_CURSOR;
    private int currentCursor = -1;

    private static final CursorManager instance = new CursorManager();

    public static CursorManager getInstance() {
        return instance;
    }

    private CursorManager() {
        this.cursors = new HashMap<>();
    }

    public void setDefaultCursor(int defaultCursor) {
        this.defaultCursor = defaultCursor;
    }

    public int getDefaultCursor() {
        return defaultCursor;
    }

    public void loadCursor(int cursorID) {
        long l = GLFW.glfwCreateStandardCursor(cursorID);
        cursors.put(cursorID, l);
    }

    public int loadCustomCursor(int x, int y, GLFWImage image) {
        long l = GLFW.glfwCreateCursor(image, x, y);
        int id = 0;
        while (cursors.containsKey(id)) {
            id++;
        }
        cursors.put(id, l);
        return id;
    }

    public void activateCursor(int cursorID) {
        if (currentCursor == cursorID) return;
        if (!cursors.containsKey(cursorID)) loadCursor(cursorID);

        GLFW.glfwSetCursor(Window.glfwWindow(), cursors.get(cursorID));
        this.currentCursor = cursorID;

    }

    public void deactivateCursor() {
        activateCursor(defaultCursor);
    }

    public static void requestCursor(int id) {
        getInstance().activateCursor(id);
    }

    public static void resetCursor() {
        getInstance().deactivateCursor();
    }

}