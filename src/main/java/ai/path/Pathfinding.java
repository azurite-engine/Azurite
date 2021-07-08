package ai.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public class Pathfinding {

    public static <Position> ResultPath<Position> dijkstra(Map<Position> map) {
        //TODO
        return null;
    }

    /**
     * A fully modifiable version of the A* (a-star) algorithm.
     * It uses a finite possibly directional weighted graph structure
     * to find the shortest possible path from a given start node to a given target node.
     *
     * @param map        a map containing a graph of weighted node references
     * @param hCost      the hCost algorithm which should make a guess about how far posA is away from posB.
     *                   It is required, that the calculation never results in a value less than 0
     *                   and it is preferred to be exactly 0 if both positions are identical.
     *                   If possible, it is usually a good approach to measure the distance between both positions
     *                   or a value that is proportional to that distance.
     * @param <Position> any class describing a node - untouched by the algorithm - only for the result to make sense
     * @return a valid resultPath from start to target as defined in map or <code>null</code> if the target node cannot be reached
     */
    public static <Position> ResultPath<Position> astar(Map<Position> map, BiFunction<Position, Position, Float> hCost) {
        PriorityQueue<Node<Position>> openList = new PriorityQueue<>(Pathfinding::compareAStarNode);
        final Node<Position> start = map.start();
        final Node<Position> target = map.target();
        AStarMarker startMarker = new AStarMarker();
        startMarker.gcost = 0;
        startMarker.hcost = hCost.apply(map.start().position(), target.position());
        startMarker.closed = true; //start node does not have to be worked on
        start.setMarker(startMarker);
        openList.offer(start);
        //repeat until the target node is found
        while (!openList.isEmpty()) {
            //the next node to expand
            Node<Position> curr = openList.poll();
            //if the target node is found, the algorithm is done, no further calculation is necessary
            if (curr.equals(target)) return createAStarPath(map.start(), map.target());
            //expand the current node by running over all adjacent nodes, calculating their costs and enqueue them
            AStarMarker currentMarker = (AStarMarker) curr.getMarker();
            for (Path<Position> positionPath : curr.paths()) {
                AStarMarker nextMarker;
                Node<Position> next = positionPath.end();
                //on the first visit, a node may not have a marker
                boolean hasMarker = next.hasMarker();
                if (hasMarker) {
                    nextMarker = (AStarMarker) next.getMarker();
                    //if the cell already has been processed, go on
                    if (nextMarker.closed) continue;
                } else next.setMarker(nextMarker = new AStarMarker());
                float gCost = currentMarker.gcost + positionPath.cost();
                //whether the current node is known to the queue
                boolean inQueue = hasMarker && openList.contains(next);
                //if gcost is higher anyways, no need to waste time here
                if (inQueue && gCost >= nextMarker.gcost) continue;
                nextMarker.predecessor = curr;
                nextMarker.gcost = gCost;
                //the hcost is infinite, if its a new marker - new marker check does not work,
                //since the marker can be added but not proceeded the first time
                if (Float.isInfinite(nextMarker.hcost))
                    nextMarker.hcost = hCost.apply(next.position(), target.position());
                //enqueue if necessary
                if (!inQueue)
                    openList.offer(next);
            }
            currentMarker.closed = true;
        }
        //no path found
        return null;
    }

    private static <Position> int compareAStarNode(Node<Position> node1, Node<Position> node2) {
        AStarMarker marker1 = (AStarMarker) node1.getMarker();
        AStarMarker marker2 = (AStarMarker) node2.getMarker();
        return Float.compare(marker1.fcost(), marker2.fcost()) * 2 + Float.compare(marker1.hcost, marker2.hcost);
    }

    //helper method for astar algorithm - this method creates the result path via backtrace
    private static <Position> ResultPath<Position> createAStarPath(Node<Position> start, Node<Position> target) {
        List<Node<Position>> fullPath = new ArrayList<>();
        fullPath.add(target);
        Node<Position> c = target;
        //backtrace all marked predecessors
        while (!c.equals(start)) {
            AStarMarker marker = (AStarMarker) c.getMarker();
            c = (Node<Position>) marker.predecessor;
            fullPath.add(c);
        }
        //reverse order to make it start from the beginning
        Collections.reverse(fullPath);
        return new ResultPath<Position>() {
            @Override
            public Node<Position> start() {
                return start;
            }

            @Override
            public Node<Position> target() {
                return target;
            }

            @Override
            public List<Node<Position>> fullPath() {
                return fullPath;
            }
        };
    }

    private static class AStarMarker {
        float gcost = Float.POSITIVE_INFINITY;
        float hcost = Float.POSITIVE_INFINITY;
        boolean closed = false;
        Node<?> predecessor = null;

        float fcost() {
            return gcost + hcost;
        }
    }

}