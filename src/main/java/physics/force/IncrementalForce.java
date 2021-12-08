package physics.force;

import org.joml.Vector2f;

/**
 * @author Juyas
 * @version 07.12.2021
 * @since 07.12.2021
 */
public class IncrementalForce implements Force {

    private final Vector2f direction;
    private final String identifier;
    private final float increment;
    private final float limit;

    private float currentIncrement;
    private Vector2f current;

    public IncrementalForce(String identifier, Vector2f direction, float increment, float limit) {
        this.direction = direction;
        this.identifier = identifier;
        this.increment = increment;
        this.limit = limit;
        this.current = direction;
    }

    @Override
    public String identifier() {
        return identifier;
    }

    @Override
    public boolean update(float dt) {
        if (currentIncrement >= limit) return false;
        currentIncrement += increment;
        current = direction.mul(currentIncrement, new Vector2f());
        return true;
    }

    @Override
    public Vector2f direction() {
        return current;
    }

    public void reset() {
        currentIncrement = 1;
    }

}