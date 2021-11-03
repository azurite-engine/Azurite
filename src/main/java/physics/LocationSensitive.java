package physics;

import org.joml.Vector3f;

/**
 * <h1>Azurite</h1>
 * An interface used to show interest as a {@link ecs.Component} in a changing location value of a {@link ecs.GameObject} that it belongs to.
 * Is used for ordering and improving the update loop inside a gameObject and its components.
 *
 * @author Juyas
 * @version 12.07.2021
 * @see ecs.GameObject#update(float)
 * @since 22.06.2021
 */
public interface LocationSensitive {

    /**
     * Called if the location of the corresponding object has changed in this update loop.
     * Its ensured to be called only a single time per update loop and only after all updates are made to the gameObjects location.
     * Do not change the location of the gameObject inside this method, it will be ignored!
     *
     * @param changedLocationData the new and updated transform value
     */
    void update(Vector3f changedLocationData);

}