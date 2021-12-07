package ai.path.vector2f;

import ai.path.Map;
import ai.path.Node;
import org.joml.Vector2f;

/**
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public class Vector2fMap implements Map<Vector2f> {

    private final Node<Vector2f> start;
    private final Node<Vector2f> target;

    public Vector2fMap(Node<Vector2f> start, Node<Vector2f> target) {
        this.start = start;
        this.target = target;
    }

    @Override
    public Node<Vector2f> start() {
        return start;
    }

    @Override
    public Node<Vector2f> target() {
        return target;
    }

}