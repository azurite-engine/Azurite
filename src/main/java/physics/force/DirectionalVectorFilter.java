package physics.force;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 05.07.2021
 * @since 05.07.2021
 */
public class DirectionalVectorFilter implements VectorFilter {

    private final Vector2f directionNormal;
    private final int id;
    private boolean invalid = false;

    public DirectionalVectorFilter(Vector2f filterDirection, int id) {
        if (!filterDirection.isFinite() || filterDirection.lengthSquared() == 0) {
            invalid = true;
            this.directionNormal = new Vector2f();
        } else {
            this.directionNormal = filterDirection.normalize(new Vector2f());
        }
        this.id = id;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public Vector2f filter(Vector2f force) {
        float dot = directionNormal.dot(force);
        if (invalid || dot < 0) {
            invalid = true;
            return force;
        }
        if (dot == 0)
            return force;
        Vector2f sub = force.sub(directionNormal.mul(dot, new Vector2f()), new Vector2f());
        if (!sub.isFinite()) System.out.println("lul: " + sub + " < " + force + " - " + directionNormal + " * " + dot);
        return sub;
    }

    @Override
    public boolean isInvalid() {
        return invalid;
    }

}