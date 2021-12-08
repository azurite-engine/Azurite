package ai.path;

import java.util.*;
import java.util.function.BiFunction;

/**
 * This class contains currently 2 pathfinding methods:
 * - a-star (A*)
 * - dijkstra
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 08.07.2021
 */
public class Pathfinding {

    /**
     * A flexible implementation of the dijkstra pathfinding algorithm.
     * It uses a finite possibly directional weighted graph structure
     * to find the shortest possible path from a given start node to a given target node.
     * This method will find the global shortest possible path, if there is one,
     * but it might be less efficient for large graphs compared to A* (a-star).
     *
     * @param map        a map containing a graph of weighted node references
     * @param <Position> any class describing a node - untouched by the algorithm - only for the result to make sense
     * @return an optional containing the shortest path if there is one
     */
    public static <Position> Optional<ResultPath<Position>> dijkstra(Map<Position> map) {
        //special case might break algorithm, so better exit directly
        if (map.start().equals(map.target())) return Optional.of(new DijkstraPath<>(map.start()));

        PriorityQueue<DijkstraPath<Position>> queue = new PriorityQueue<>((a, b) -> Float.compare(a.cost, b.cost));
        Node<Position> current = map.start();
        queue.offer(new DijkstraPath<>(current));
        while (!queue.isEmpty()) {
            DijkstraPath<Position> poll = queue.poll();
            current = poll.target();
            //if the current shortest path is our target path, we are done here
            if (current.equals(map.target()))
                return Optional.of((DijkstraPath<Position>) current.getMarker());
            //mark the path as the shortest possible
            poll.blacked = true;
            for (Path<Position> path : current.paths()) {
                Node<Position> successor = path.end();
                if (successor.hasMarker()) {
                    DijkstraPath<?> marker = (DijkstraPath<?>) successor.getMarker();
                    //blacked nodes can be ignored, their shortest path is already known
                    if (marker.blacked) continue;
                    //if the new path is better, use it
                    if (marker.cost > poll.cost) {
                        //queue should only contain active and relevant paths
                        queue.remove(marker);
                        DijkstraPath<Position> newPath = new DijkstraPath<>(poll, successor, path.cost());
                        queue.offer(newPath);
                        successor.setMarker(newPath);
                    }
                } else {
                    //add path, because there is none yet known for this node
                    DijkstraPath<Position> newPath = new DijkstraPath<>(poll, successor, path.cost());
                    queue.offer(newPath);
                    successor.setMarker(newPath);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * A fully modifiable version of the A* (a-star) algorithm.
     * It uses a finite possibly directional weighted graph structure
     * to find the shortest possible path from a given start node to a given target node.
     * This method will not check every possible path, so if the weights are not equally distributed,
     * it may lead to results, that are not the best solution to the problem.
     *
     * @param map        a map containing a graph of weighted node references
     * @param hCost      the hCost algorithm which should make a guess about how far posA is away from posB.
     *                   It is required, that the calculation never results in a value less than 0
     *                   and it is preferred to be exactly 0 if both positions are identical.
     *                   If possible, it is usually a good approach to measure the distance between both positions
     *                   or a value that is proportional to that distance.
     * @param <Position> any class describing a node - untouched by the algorithm - only for the result to make sense
     * @return an optional containing the found path if there is one
     */
    public static <Position> Optional<ResultPath<Position>> astar(Map<Position> map, BiFunction<Position, Position, Float> hCost) {
        PriorityQueue<Node<Position>> openList = new PriorityQueue<>(Pathfinding::compareAStarNode);
        final Node<Position> start = map.start();
        final Node<Position> target = map.target();
        AStarMarker<Position> startMarker = new AStarMarker<>();
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
            if (curr.equals(target)) return Optional.of(createAStarPath(map.start(), map.target()));
            //expand the current node by running over all adjacent nodes, calculating their costs and enqueue them
            AStarMarker<Position> currentMarker = (AStarMarker<Position>) curr.getMarker();
            for (Path<Position> positionPath : curr.paths()) {
                AStarMarker<Position> nextMarker;
                Node<Position> next = positionPath.end();
                //on the first visit, a node may not have a marker
                boolean hasMarker = next.hasMarker();
                if (hasMarker) {
                    nextMarker = (AStarMarker<Position>) next.getMarker();
                    //if the cell already has been processed, go on
                    if (nextMarker.closed) continue;
                } else next.setMarker(nextMarker = new AStarMarker<>());
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
        return Optional.empty();
    }

    private static <Position> int compareAStarNode(Node<Position> node1, Node<Position> node2) {
        AStarMarker<Position> marker1 = (AStarMarker<Position>) node1.getMarker();
        AStarMarker<Position> marker2 = (AStarMarker<Position>) node2.getMarker();
        return Float.compare(marker1.fcost(), marker2.fcost()) * 2 + Float.compare(marker1.hcost, marker2.hcost);
    }

    //helper method for astar algorithm - this method creates the result path via backtrace
    private static <Position> ResultPath<Position> createAStarPath(Node<Position> start, Node<Position> target) {
        List<Node<Position>> fullPath = new ArrayList<>();
        fullPath.add(target);
        Node<Position> c = target;
        //backtrace all marked predecessors
        while (!c.equals(start)) {
            AStarMarker<Position> marker = (AStarMarker<Position>) c.getMarker();
            c = marker.predecessor;
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

    //helper class describing a path to a specific node in the dijkstra algorithm
    private static class DijkstraPath<Position> implements ResultPath<Position>, Node.Marker<Position> {
        float cost;
        List<Node<Position>> path;
        private boolean blacked = false; //true means this path is the shortest one

        DijkstraPath(Node<Position> node) {
            this.path = new ArrayList<>();
            this.path.add(node);
            this.cost = 0;
        }

        DijkstraPath(DijkstraPath<Position> prev, Node<Position> newNode, float additionalCost) {
            path = new ArrayList<>();
            path.addAll(prev.path);
            path.add(newNode);
            this.cost = prev.cost + additionalCost;
        }

        @Override
        public Node<Position> start() {
            return path.get(0);
        }

        @Override
        public Node<Position> target() {
            return path.get(path.size() - 1);
        }

        @Override
        public List<Node<Position>> fullPath() {
            return path;
        }
    }

    private static class AStarMarker<Position> implements Node.Marker<Position> {
        float gcost = Float.POSITIVE_INFINITY;
        float hcost = Float.POSITIVE_INFINITY;
        boolean closed = false;
        Node<Position> predecessor = null;

        float fcost() {
            return gcost + hcost;
        }
    }

}