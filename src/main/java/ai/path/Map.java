package ai.path;

/**
 * A Map describes the complete state of a finite graph
 * and offers a start and target node to be processed by a pathfinding algorithm.
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public interface Map<Position> {

    /**
     * The start node for the pathfinding algorithm
     *
     * @return the start node
     */
    Node<Position> start();

    /**
     * The target node for the pathfinding algorithm
     *
     * @return the target node
     */
    Node<Position> target();

}