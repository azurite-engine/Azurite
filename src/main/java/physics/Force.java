package physics;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 20.06.2021
 * @since 20.06.2021
 */
public interface Force {

    String identifier();

    boolean update(float dt);

    void reset();

    Vector2f direction();

}