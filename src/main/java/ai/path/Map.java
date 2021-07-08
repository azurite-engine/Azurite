package ai.path;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public interface Map<Position> {

    Node<Position> start();

    Node<Position> target();

}