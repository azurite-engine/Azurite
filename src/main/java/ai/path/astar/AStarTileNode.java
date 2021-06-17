package ai.path.astar;

import ai.path.PathNode;

import java.util.LinkedList;
import java.util.List;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 17.06.2021
 * @since 17.06.2021
 */
class AStarTileNode implements AStarDataNode, PathNode {

    public static final int DEFAULT_H_COST = -1;

    private final int x;
    private final int y;

    private AStarTileNode predecessor;

    private boolean passable;

    private int gcost;
    private int hcost = DEFAULT_H_COST;
    private int fcost;

    public AStarTileNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public List<AStarTileNode> getNeighbors(AStarTileNode[][] map, boolean diagonal, int offsetX, int offsetY) {
        List<AStarTileNode> neighbors = new LinkedList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (j == 0 && i == 0) continue; //its the same node

                //only allow diagonal if diagonal is true
                if (i == 0 || j == 0 || diagonal) {
                    //ensure bounds
                    int nx = x + i - offsetX;
                    int ny = y + i - offsetY;
                    if (map.length > nx && nx >= 0 && map[0].length > ny && ny >= 0) {
                        if (map[nx][ny].isPassable()) {
                            neighbors.add(map[nx][ny]);
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    public AStarTileNode getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(AStarTileNode previous) {
        this.predecessor = previous;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean isBlocked() {
        return !isPassable();
    }

    public int getX() {
        return x;
    }

    public boolean isPassable() {
        return passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    public int getGcost() {
        return gcost;
    }

    public void setGcost(int gcost) {
        this.gcost = gcost;
    }

    public int getHcost() {
        return hcost;
    }

    public void setHcost(int hcost) {
        this.hcost = hcost;
    }

    public int getFcost() {
        return fcost;
    }

    public void setFcost(int fcost) {
        this.fcost = fcost;
    }

    @Override
    public String toString() {
        return "(" + x + ',' + y + "){" +
                "g=" + gcost +
                ", h=" + hcost +
                ", f=" + fcost +
                '}';
    }

    @Override
    public int compareTo(AStarDataNode o) {
        return Integer.compare(this.getFcost(), o.getFcost()) * 2 + Integer.compare(this.getHcost(), o.getHcost());
    }
}