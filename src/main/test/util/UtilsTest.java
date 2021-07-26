package util;

import graphics.Camera;
import org.joml.Vector2f;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

public class UtilsTest {

    @Test
    public void worldToScreenCoords() {
        Camera camera = new Camera();
        camera.getProjectionMatrix().identity();
        camera.getProjectionMatrix().ortho(0, 1920, 1020, 0, 0, 100);

        Vector2f v = new Vector2f(1920/2f, 0);

        /*                  right  left   top    bottom                     */
        boolean[] bounds = {false, false, false, false};

        bounds[0] = 1.0f == Utils.worldToScreenCoords(v, camera).x;
        v.x = -1920/2f;
        bounds[1] = -1.0f == Utils.worldToScreenCoords(v, camera).x;
        v.x = 0;

        v.y = 1020/2f;
        bounds[2] = -1.0f == Utils.worldToScreenCoords(v, camera).y;
        v.y = -1020/2f;
        bounds[3] = 1.0f == Utils.worldToScreenCoords(v, camera).y;

        int i = 0;
        for (boolean b : bounds) {
            assertTrue(String.valueOf(i), b);
            i++;
        }
    }
}
