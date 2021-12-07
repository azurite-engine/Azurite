package physics.collision;

/**
 * A class to pass down information about a collision detection for further response.
 *
 * @author Juyas
 * @version 02.09.2021
 * @since 07.12.2021
 */
public class CollisionInformation {

    private Object data;
    private boolean collision;

    public CollisionInformation(Object data, boolean collision) {
        this.data = data;
        this.collision = collision;
    }

    public final Object get() {
        return data;
    }

    public final boolean collision() {
        return collision;
    }

}