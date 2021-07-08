package ai.path;

import java.util.List;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public abstract class Node<Position> {

    //a marker only used for pathfinding algorithms - can be anythings depending on the algorithm
    private Marker<Position> marker = null;

    public abstract Position position();

    public abstract List<Path<Position>> paths();

    interface Marker<T> {
    }

    public final void setMarker(Marker<Position> marker) {
        this.marker = marker;
    }

    public final Marker<Position> getMarker() {
        return marker;
    }

    public final boolean hasMarker() {
        return marker != null;
    }

}