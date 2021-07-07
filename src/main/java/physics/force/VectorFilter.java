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

    Vector2f filter(Vector2f velocity);

    boolean isInvalid();

    default VectorFilter andThen(VectorFilter vectorFilter) {
        return new VectorFilter() {
            @Override
            public int id() {
                return vectorFilter.id();
            }

            @Override
            public Vector2f filter(Vector2f velocity) {
                return vectorFilter.filter(VectorFilter.this.filter(velocity));
            }

            @Override
            public boolean isInvalid() {
                return vectorFilter.isInvalid() || VectorFilter.this.isInvalid();
            }
        };
    }

}