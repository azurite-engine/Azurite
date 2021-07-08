package ai.path;

import java.util.List;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public interface Node<Position> {

    Position position();

    List<Path<Position>> paths();

    void setMarker(Marker<Position> o);

    Marker<Position> getMarker();

    default boolean hasMarker() {
        return getMarker() != null;
    }

    interface Marker<T> {
    }

}