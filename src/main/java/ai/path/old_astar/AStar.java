package ai.path.old_astar;

import java.util.function.BiFunction;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 17.06.2021
 * @since 17.06.2021
 */
public class AStar implements PathfindingAlgorithm {

    //information codes used in set(info,val)
    public static final int DATA_BOOL_DIAGONALLY_MOVES_ALLOWED = 1;
    public static final int DATA_INT_STRAIGHT_MOVE_COSTS = 2;
    public static final int DATA_INT_DIAGONAL_MOVE_COSTS = 3;
    public static final int DATA_BOOL_USE_TILE_OPTIMIZATION = 4;

    //default values of algorithm settings
    public static final boolean DEFAULT_DIAGONALLY_MOVES_ALLOWED = true;
    public static final int DEFAULT_STRAIGHT_MOVE_COSTS = 10;
    public static final int DEFAULT_DIAGONAL_MOVE_COSTS = 14;
    public static final boolean DEFAULT_USE_TILE_OPTIMIZATION = true;

    //tile-optimized version ONLY
    //the optimized pre-build map
    private PathNode[][] nodes = null;
    //the raw nodes in the given order in use(nodes...)
    private PathNode[] unsorted_nodes;

    //tile-optimized version ONLY
    //required for optimization, to reduce the size of the map without touching the values
    private int offsetX = 0, offsetY = 0;

    //tile-optimized version ONLY
    //defines whether the path can do diagonal moves in the grid
    private boolean diagonal = DEFAULT_DIAGONALLY_MOVES_ALLOWED;

    //tile-optimized version ONLY
    //defines how much a straight move to the side, up or down costs
    private int straightCosts = DEFAULT_STRAIGHT_MOVE_COSTS;

    //tile-optimized version ONLY
    //defines how much a diagonal move costs
    private int diagonalCosts = DEFAULT_DIAGONAL_MOVE_COSTS;

    //determines whether the algorithm for tile-based maps should be used
    private boolean optimizedTileBased = DEFAULT_USE_TILE_OPTIMIZATION;

    private BiFunction<AStarDataNode, AStarDataNode, Integer> hCostFunction = this::hCost;

    public void overwriteHCostFunction(BiFunction<AStarDataNode, AStarDataNode, Integer> hCostFunction) {
        this.hCostFunction = hCostFunction;
    }

    @Override
    public void use(PathNode... nodes) {
        this.unsorted_nodes = nodes;
        orderNodes();
    }

    //prepare the nodes for the tile-optimized version
    private void orderNodes() {
        //only for tile-based optimization mode
        if (!optimizedTileBased) return;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (PathNode node : unsorted_nodes) {
            if (maxX < node.getX())
                maxX = node.getX();
            if (maxY < node.getY())
                maxY = node.getY();
            if (minX > node.getX())
                minX = node.getX();
            if (minY > node.getY())
                minY = node.getY();
        }
        offsetX = minX;
        offsetY = minY;
        this.nodes = new PathNode[maxX - offsetX + 1][maxY - offsetY + 1];
        for (PathNode node : unsorted_nodes) {
            this.nodes[node.getX() + offsetX][node.getY() + offsetY] = node;
        }
    }

    @Override
    public void set(int information, Object data) {
        switch (information) {
            case DATA_INT_STRAIGHT_MOVE_COSTS:
                this.straightCosts = (int) data;
                break;
            case DATA_INT_DIAGONAL_MOVE_COSTS:
                this.diagonalCosts = (int) data;
                break;
            case DATA_BOOL_DIAGONALLY_MOVES_ALLOWED:
                this.diagonal = (boolean) data;
                break;
            case DATA_BOOL_USE_TILE_OPTIMIZATION:
                if (optimizedTileBased == (boolean) data) break;
                optimizedTileBased = (boolean) data;
                if (optimizedTileBased) {
                    orderNodes();
                } else nodes = null;
                break;
        }
    }

    @Override
    public NodePath find(PathNode start, PathNode target) {
        if (optimizedTileBased) {
            AStarTilePathfinding pathfinding = new AStarTilePathfinding(diagonal, straightCosts, diagonalCosts, offsetX, offsetY);
        pathfinding.setHCostFunction(hCostFunction);
            return pathfinding.findPath(nodes, start.getX(), start.getY(), target.getX(), target.getY());
        } else {
            //TODO more generalized search - non-tiled-based
            return null;
        }
    }

    //standard method for hCost with constant move costs
    private int hCost(AStarDataNode successor, AStarDataNode target) {
        int dx = Math.abs(successor.getX() - target.getX());
        int dy = Math.abs(successor.getY() - target.getY());
        //straight go x and straight go y and add up
        if (!diagonal)
            return (dx + dy) * straightCosts;
        else
            return Math.min((dx + dy) * straightCosts,
                    //diagonal move until only dx or dy moves are left
                    Math.min(dx, dy) * diagonalCosts + Math.abs(dx - dy) * straightCosts);
    }

}