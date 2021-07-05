package physics.force;

import org.joml.Vector2f;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 05.07.2021
 * @since 05.07.2021
 */
public interface VectorFilter {

    int id();

    Vector2f filter(Vector2f force);

    boolean isInvalid();

    default VectorFilter andThen(VectorFilter forceFilter) {
        return new VectorFilter() {
            @Override
            public int id() {
                return forceFilter.id();
            }

            @Override
            public Vector2f filter(Vector2f force) {
                return forceFilter.filter(VectorFilter.this.filter(force));
            }

            @Override
            public boolean isInvalid() {
                return forceFilter.isInvalid() || VectorFilter.this.isInvalid();
            }
        };
    }

}