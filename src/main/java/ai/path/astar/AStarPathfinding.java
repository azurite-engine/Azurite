package ai.path.astar;

import ai.path.NodePath;
import ai.path.PathNode;

import java.util.*;
import java.util.function.BiFunction;

/**
 * <h1>Study Projects</h1>
 *
 * @author Julius Korweck
 * @version 17.06.2021
 * @since 17.06.2021
 */
class AStarPathfinding
{

    private final boolean diagonal;
    private final int straightCosts;
    private final int diagonalCosts;

    private BiFunction<AStarDataNode, AStarDataNode, Integer> hCost;

    //using a priorityQueue to keep the order while using an efficient structure
    private final PriorityQueue<AStarNode> openList;
    //since this is only for the purpose of "contains"-checks, a set is sufficient
    private final Set<AStarNode> closedList;

    private int offsetX, offsetY;

    private AStarNode[][] nodeMap;

    public AStarPathfinding( boolean canGoDiagonally, int straightCosts, int diagonalCosts, int offsetX, int offsetY )
    {
        this.diagonal = canGoDiagonally;
        this.straightCosts = straightCosts;
        this.diagonalCosts = diagonalCosts;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.openList = new PriorityQueue<>();
        this.closedList = new HashSet<>();
    }

    public void setHCostFunction( BiFunction<AStarDataNode, AStarDataNode, Integer> hCost )
    {
        this.hCost = hCost;
    }

    //map has to be a square or rect -> map[0].length == map[n].length
    public NodePath findPath(PathNode[][] map, int startX, int startY, int targetX, int targetY )
    {
        int x_length = map.length;
        int y_length = map[0].length;
        nodeMap = new AStarNode[x_length][y_length];
        //converting the nodes to AStarNodes to work on
        for ( int i = 0; i < x_length; i++ )
        {
            for ( int j = 0; j < y_length; j++ )
            {
                AStarNode node = new AStarNode( i, j );
                node.setPassable( !map[i][j].isBlocked() );
                nodeMap[i][j] = node;
            }
        }
        return aStar( nodeMap[startX][startY], nodeMap[targetX][targetY] );
    }

    //the aStar algorithm core - loop the openList until its empty or the target node is found
    private NodePath aStar(AStarNode startNode, AStarNode targetNode )
    {
        openList.offer( startNode );
        while ( !openList.isEmpty() )
        {
            //take the next "best" node with the lowest score, the priorityQueue ensures that its sorted like this
            AStarNode curr = openList.poll();

            //if the current node is our target, we found our goal!
            if ( curr == targetNode )
                return path( startNode, targetNode );

            //curr is calculated, so don't do that again
            closedList.add( curr );

            //calculate neighbor nodes for current
            expandNode( nodeMap, curr, targetNode, openList, closedList );
        }
        return new NodePath( startNode );
    }

    private void expandNode(AStarNode[][] map, AStarNode current, AStarNode target, PriorityQueue<AStarNode> openList, Set<AStarNode> closedList )
    {
        List<AStarNode> neighbors = current.getNeighbors( map, diagonal, offsetX, offsetY );
        for ( AStarNode successor : neighbors )
        {
            //if node is already done, go next
            if ( closedList.contains( successor ) )
                continue;

            //1 for straight; 2 for diagonal
            int move = Math.abs( successor.getX() - current.getX() ) + Math.abs( successor.getY() - current.getY() );
            int g = current.getGcost() + ( move == 1 ? straightCosts : diagonalCosts );

            //if the new path is worse then an existing one go next
            if ( openList.contains( successor ) && g >= successor.getGcost() ) continue;

            //make the new predecessor of the successor the current note, since the path is better
            successor.setPredecessor( current );
            successor.setGcost( g );

            //calc or get H costs - they should not change over the calculation
            int h = successor.getHcost() == AStarNode.DEFAULT_H_COST ? hCost.apply( successor, target ) : successor.getHcost();
            successor.setFcost( g + h );
            successor.setHcost( h );

            //add to openList if necessary
            if ( !openList.contains( successor ) )
                openList.offer( successor );
        }
    }

    //converting the nodeMap backwards to a path object
    private NodePath path(AStarNode startNode, AStarNode targetNode )
    {
        List<AStarNode> path = new LinkedList<>();
        while ( startNode != targetNode )
        {
            path.add( targetNode );
            targetNode = targetNode.getPredecessor();
        }
        NodePath result = new NodePath( path.get( path.size() - 1 ) );
        NodePath tmp = result;
        for ( int i = path.size() - 2; i >= 0; i-- )
        {
            NodePath next = new NodePath( path.get( i ) );
            tmp.setNext( next );
            tmp = next;
        }
        return result;
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/