package physics.collision;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Julius Korweck
 * @version 18.06.2021
 * @since 18.06.2021
 */
public interface Shape {

    /**
     * According to GJKSM this method is supposed to calculate the point of the shape, that is most in direction of v.
     * The general rule is, the more primitive the shape is, the more efficient this method can be.
     * This method may be described as max{v*x,x element of Shape} for any complex shape.
     *
     * @param v the direction
     * @return the point of the shape that is most in the direction of v
     */
    Vector2f supportPoint(Vector2f v);

}