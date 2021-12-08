package ai.path.vector2f;

import ai.path.Node;
import ai.path.Path;
import org.joml.Vector2f;

/**
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public class Vector2fPath implements Path<Vector2f> {

    private final Node<Vector2f> start;
    private final Node<Vector2f> end;
    private final float cost;

    public Vector2fPath(Node<Vector2f> start, Node<Vector2f> end, float cost) {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }

    @Override
    public Node<Vector2f> start() {
        return start;
    }

    @Override
    public Node<Vector2f> end() {
        return end;
    }

    @Override
    public float cost() {
        return cost;
    }

}