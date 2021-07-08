package ai.path;

import java.util.List;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public interface ResultPath<Position> {

    Node<Position> start();

    Node<Position> target();

    List<Node<Position>> fullPath();

}