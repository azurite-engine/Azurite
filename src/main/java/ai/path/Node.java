package ai.path;

import java.util.List;

/**
 * A {@link Node} describes a single node of a finite graph and does knows all paths to its neighbors.
 * It is a container for any external position data for using in a {@link Map} for a pathfinding algorithm.
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public abstract class Node<Position> {

    //a marker used for pathfinding algorithms - can be anything depending on the algorithm
    private Marker<Position> marker = null;

    /**
     * The external position data contained in this node.
     * The exact value of this method is ignored by the pathfinding algorithms,
     * but may be used to calculate relationship data between nodes using an external algorithm.
     *
     * @return the position data chosen for this node
     */
    public abstract Position position();

    /**
     * All paths starting at this node.
     *
     * @return all paths starting at this node
     */
    public abstract List<Path<Position>> paths();

    public final Marker<Position> getMarker() {
        return marker;
    }

    public final void setMarker(Marker<Position> marker) {
        this.marker = marker;
    }

    public final boolean hasMarker() {
        return marker != null;
    }

    interface Marker<T> {
    }

}