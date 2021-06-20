package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 * <p>
 * The GJKSM shape implementation of a rectangle.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class Rectangle extends GJKSMShape {

    private final Vector2f[] relatives;
    private Vector2f[] absolutes;

    @Override
    public void adjust() {
        for (int i = 0; i < 4; i++) {
            absolutes[i] = position().add(relatives[i], new Vector2f());
        }
    }

    public Rectangle(Vector2f a, Vector2f b, Vector2f c, Vector2f d) {
        this.relatives = new Vector2f[]{new Vector2f(a), new Vector2f(b), new Vector2f(c), new Vector2f(d)};
        this.absolutes = new Vector2f[4];
    }

    @Override
    public Vector2f supportPoint(Vector2f v) {
        return ConvexGJKSM.maxDotPoint(absolutes, v);
    }
}