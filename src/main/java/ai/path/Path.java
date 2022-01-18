package ai.path;

/**
 * Describes a path from one node to another with a fixed path cost value.
 * This path is considered a direct path for the pathfinding algorithm.
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public interface Path<Position> {

    /**
     * The node where the path starts.
     *
     * @return the starting node
     */
    Node<Position> start();

    /**
     * The node where the path ends.
     *
     * @return the ending node
     */
    Node<Position> end();

    /**
     * The costs to take this path. An artificial value to order multiple paths by costs
     * and to find a the path with the lowest cost which is considered the shortest.
     *
     * @return the cost to take this path
     */
    float cost();

}