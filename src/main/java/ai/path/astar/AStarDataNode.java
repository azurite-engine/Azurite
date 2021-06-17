package ai.path.astar;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 17.06.2021
 * @since 17.06.2021
 */
public interface AStarDataNode extends Comparable<AStarDataNode> {

    int getX();

    int getY();

    int getGcost();

    int getHcost();

    int getFcost();

}