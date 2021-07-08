package ai.path;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public interface Path<Position> {

    Node<Position> start();

    Node<Position> end();

    float cost();

}