package physics.force;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 20.06.2021
 * @since 20.06.2021
 */
public abstract class BasicForce implements Force {

    private final String identifier;
    private Vector2f normalizedDirection;
    private float baseStrength;
    private float currentStrength;

    public BasicForce(String identifier, Vector2f direction) {
        this.normalizedDirection = direction.normalize(new Vector2f());
        this.baseStrength = direction.length();
        this.currentStrength = baseStrength;
        this.identifier = identifier;
    }

    @Override
    public String identifier() {
        return identifier;
    }

    public void setNormalizedDirection(Vector2f normalizedDirection) {
        this.normalizedDirection = normalizedDirection;
    }

    public void setBaseStrength(float baseStrength) {
        this.baseStrength = baseStrength;
    }

    public void setCurrentStrength(float currentStrength) {
        this.currentStrength = currentStrength;
    }

    public Vector2f getNormalizedDirection() {
        return normalizedDirection;
    }

    public float getCurrentStrength() {
        return currentStrength;
    }

    public float getBaseStrength() {
        return baseStrength;
    }

    @Override
    public void reset() {
        this.currentStrength = baseStrength;
    }

    @Override
    public Vector2f direction() {
        return normalizedDirection.mul(currentStrength, new Vector2f());
    }

}