package ai.path;

import java.util.List;

/**
 * The result of a pathfinding algorithm containing a full path from start to target.
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public interface ResultPath<Position> {

    /**
     * The start of the found path.
     *
     * @return the starting node
     */
    Node<Position> start();

    /**
     * The target of the found path
     *
     * @return the target node
     */
    Node<Position> target();

    /**
     * The full path containing all nodes in order from start to target.
     * This may include start and target as well.
     *
     * @return the full path
     */
    List<Node<Position>> fullPath();

}