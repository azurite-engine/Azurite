package physics;

import org.joml.Vector2f;
import physics.force.Force;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 20.06.2021
 * @since 20.06.2021
 */
public interface PhysicalEntity {

    float getMass();

    Force getForce();

    Vector2f velocity();

    void applyForce(Force force);

    void negateForce(String identifier);

}