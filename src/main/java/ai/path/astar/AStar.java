package ai.path.astar;

import ai.path.NodePath;
import ai.path.PathNode;
import ai.path.PathfindingAlgorithm;

import java.util.function.BiFunction;

/**
 * <h1>Study Projects</h1>
 *
 * @author Julius Korweck
 * @version 17.06.2021
 * @since 17.06.2021
 */
public class AStar implements PathfindingAlgorithm {

    public static final int DATA_BOOL_DIAGONALLY_MOVES_ALLOWED = 1;
    public static final int DATA_INT_STRAIGHT_MOVE_COSTS = 2;
    public static final int DATA_INT_DIAGONAL_MOVE_COSTS = 3;

    public static final boolean DEFAULT_DIAGONALLY_MOVES_ALLOWED = true;
    public static final int DEFAULT_STRAIGHT_MOVE_COSTS = 10;
    public static final int DEFAULT_DIAGONAL_MOVE_COSTS = 14;

    private PathNode[][] nodes = null;

    private int offsetX = 0, offsetY = 0;

    private boolean diagonal = DEFAULT_DIAGONALLY_MOVES_ALLOWED;
    private int straightCosts = DEFAULT_STRAIGHT_MOVE_COSTS;
    private int diagonalCosts = DEFAULT_DIAGONAL_MOVE_COSTS;

    private BiFunction<AStarDataNode, AStarDataNode, Integer> hCostFunction = this::hCost;

    public void overwriteHCostFunction(BiFunction<AStarDataNode, AStarDataNode, Integer> hCostFunction) {
        this.hCostFunction = hCostFunction;
    }

    @Override
    public void use(PathNode... nodes) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (PathNode node : nodes) {
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
        for (PathNode node : nodes) {
            this.nodes[node.getX() + offsetX][node.getY() + offsetY] = node;
        }
    }

    @Override
    public void set(int information, Object data) {
        switch (information) {
            case DATA_INT_STRAIGHT_MOVE_COSTS:
                this.straightCosts = (int) data;
            case DATA_INT_DIAGONAL_MOVE_COSTS:
                this.diagonalCosts = (int) data;
            case DATA_BOOL_DIAGONALLY_MOVES_ALLOWED:
                this.diagonal = (boolean) data;
        }
    }

    @Override
    public NodePath find(PathNode start, PathNode target) {
        if (nodes == null)
            return null;
        AStarPathfinding pathfinding = new AStarPathfinding(diagonal, straightCosts, diagonalCosts, offsetX, offsetY);
        pathfinding.setHCostFunction(hCostFunction);
        return pathfinding.findPath(nodes, start.getX(), start.getY(), target.getX(), target.getY());
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

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/