package ecs;

import org.joml.Vector2f;
import physics.force.Force;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The {@link Dynamics} manage movement of a {@link GameObject}.
 *
 * @author Juyas
 * @version 06.12.2021
 * @since 06.12.2021
 */
public class Dynamics extends Component {

    public static final int DEFAULT_FORCE_CAPACITY = 10;

    private final Collection<Force> forces;
    private final Vector2f velocity;

    public Dynamics() {
        super(ComponentOrder.TRANSFORM);
        this.forces = new ArrayList<>(DEFAULT_FORCE_CAPACITY);
        this.velocity = new Vector2f(0, 0);
    }

    /**
     * The current directional velocity of the physical entity.
     *
     * @return the current directional velocity
     */
    public final Vector2f velocity() {
        return velocity;
    }

    /**
     * Applies another force to the physical entity that will be added to the physical entities effective force.
     *
     * @param force a new force effecting this entity
     */
    public final void applyForce(Force force) {
        forces.add(force);
    }

    /**
     * Removes all forces matching a given identifier.
     * They will no longer effect the force of the entity.
     *
     * @param identifier the identifier to find all matching forces
     * @return true if any force got removed
     */
    public final boolean removeForce(String identifier) {
        return forces.removeIf(f -> f.identifier().equals(identifier));
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        //reset velocity
        velocity.set(0, 0);
        //update dynamics & recalculate velocity
        for (Force f : forces) {
            f.update(dt);
            velocity.add(f.direction());
        }

        //apply dynamics
        setPosition(position().add(velocity()));
    }


}