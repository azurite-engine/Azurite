package ai.path.old_astar;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 17.06.2021
 * @since 17.06.2021
 */
public interface PathfindingAlgorithm {

    void use(PathNode... nodes);

    NodePath find(PathNode start, PathNode target);

    void set(int information, Object data);

}