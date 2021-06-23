package util;

import physics.collision.Shape;

import java.util.Set;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 23.06.2021
 * @since 23.06.2021
 */
public interface SimpleStructure {

    void insert(Shape node);

    void remove(Shape node);

    Set<Shape> nearest(Shape point);

}