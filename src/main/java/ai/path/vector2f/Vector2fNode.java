package ai.path.vector2f;

import ai.path.Node;
import ai.path.Path;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public class Vector2fNode extends Node<Vector2f> {


    private final Vector2f position;
    private final List<Path<Vector2f>> paths;

    public Vector2fNode(Vector2f position) {
        this.position = position;
        this.paths = new ArrayList<>();
    }

    public void addPathTo(Vector2fNode node, float cost, boolean bidirectional) {
        this.paths.add(new Vector2fPath(this, node, cost));
        if (bidirectional)
            node.addPathTo(this, cost, false);
    }

    @Override
    public Vector2f position() {
        return position;
    }

    @Override
    public List<Path<Vector2f>> paths() {
        return paths;
    }

}