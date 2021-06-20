package physics;

import org.joml.Vector2f;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 20.06.2021
 * @since 20.06.2021
 */
public class CombinedForce implements Force {

    private final Set<Force> subForces;
    private Vector2f lastBodyForce;
    private boolean recalculateForce;

    public CombinedForce() {
        this.subForces = new HashSet<>();
        this.recalculateForce = true;
        this.lastBodyForce = new Vector2f(0, 0);
    }

    public void removeForces(String identifier) {
        subForces.removeIf(force -> force.identifier().equals(identifier));
        recalculateForce = true;
    }

    public void applyForce(Force force) {
        this.subForces.add(force);
        recalculateForce = true;
    }

    @Override
    public String identifier() {
        return "CombinedForce";
    }

    @Override
    public boolean update(float dt) {
        boolean updated = subForces.stream().map(f -> f.update(dt)).reduce(Boolean::logicalOr).orElse(false);
        if (updated || recalculateForce) {
            reset();
            return true;
        }
        return false;
    }

    @Override
    public void reset() {
        Optional<Vector2f> reduce = subForces.stream().map(Force::direction).reduce((vec1, vec2) -> vec1.add(vec2, new Vector2f()));
        lastBodyForce = reduce.orElse(new Vector2f(0, 0));
    }

    @Override
    public Vector2f direction() {
        return lastBodyForce;
    }

}