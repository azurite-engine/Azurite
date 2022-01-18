package ai.path;


import ai.path.vector2f.Vector2fMap;
import ai.path.vector2f.Vector2fNode;
import org.joml.Vector2f;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Juyas
 * @version 14.07.2021
 * @since 14.07.2021
 */
public class PathfindingTest {

    private Vector2fMap map1;
    private String expectedPath1;

    @Before
    public void setUp() {
        //a map to find a path trough
        char[][] map1 = new char[][]
                {       //0    1    2    3    4    5    6    7    8    9    10   11   12   13
                        {'X', 'X', 'S', 'X', '-', 'X', 'X', '-', '-', '-', '-', '-', '-', '-'}, //0
                        {'X', '-', '-', '-', '-', 'X', 'X', '-', 'X', 'X', '-', 'X', 'X', 'X'}, //1
                        {'X', '-', 'X', 'X', '-', '-', '-', '-', 'X', 'X', '-', '-', '-', 'T'}, //2
                        {'X', '-', 'X', 'X', '-', 'X', 'X', '-', '-', '-', '-', 'X', 'X', '-'}, //3
                        {'X', '-', '-', '-', '-', '-', '-', '-', 'X', 'X', '-', '-', '-', '-'}  //4
                };
        //solution split into x and y coord array - same indices make a point coord
        int[] iSolution = new int[]{0, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2};
        int[] jSolution = new int[]{2, 2, 3, 4, 4, 5, 6, 7, 7, 8, 9, 10, 10, 11, 12, 13};
        //create the node map for map1
        Vector2fNode[][] nodeMap = createNodeMap(map1);
        this.map1 = new Vector2fMap(nodeMap[0][2], nodeMap[2][13]);
        this.expectedPath1 = createSolutionString(nodeMap, iSolution, jSolution);
    }

    private String createSolutionString(Vector2fNode[][] nodeMap, int[] iSolution, int[] jSolution) {
        List<Node<Vector2f>> nodeList = new ArrayList<>();
        for (int i = 0; i < iSolution.length; i++) {
            nodeList.add(nodeMap[iSolution[i]][jSolution[i]]);
        }
        return pathToString(nodeList);
    }

    //HELPER METHOD - map a charMap to a nodeMap, where every X is ignored and any other character becomes a node,
    //which a path to its neighbors with fix costs
    private Vector2fNode[][] createNodeMap(char[][] charMap) {
        //create the node map for charMap
        Vector2fNode[][] nodeMap = new Vector2fNode[charMap.length][];
        for (int i = 0, map1Length = charMap.length; i < map1Length; i++) {
            char[] line = charMap[i];
            nodeMap[i] = new Vector2fNode[line.length];
            for (int j = 0, lineLength = line.length; j < lineLength; j++) {
                char c = line[j];
                if (c == 'X') {
                    nodeMap[i][j] = null;
                    continue;
                }
                nodeMap[i][j] = new Vector2fNode(new Vector2f(i, j));
            }
        }
        //find neighbors
        float fixCost = 1.0f;
        for (int i = 0; i < nodeMap.length; i++) {
            for (int j = 0; j < nodeMap[i].length; j++) {
                Vector2fNode node = nodeMap[i][j];
                if (node == null) continue;
                //add neighbor nodes, if there is one
                if (i > 0) {
                    Vector2fNode up = nodeMap[i - 1][j];
                    if (up != null)
                        node.addPathTo(up, fixCost, false);
                }
                if (i < nodeMap.length - 1) {
                    Vector2fNode down = nodeMap[i + 1][j];
                    if (down != null)
                        node.addPathTo(down, fixCost, false);
                }
                if (j > 0) {
                    Vector2fNode left = nodeMap[i][j - 1];
                    if (left != null)
                        node.addPathTo(left, fixCost, false);
                }
                if (j < nodeMap[i].length - 1) {
                    Vector2fNode right = nodeMap[i][j + 1];
                    if (right != null)
                        node.addPathTo(right, fixCost, false);
                }
            }
        }
        return nodeMap;
    }

    //HELPER METHOD
    private String pathToString(List<Node<Vector2f>> resultPath) {
        StringBuilder builder = new StringBuilder();
        resultPath.forEach(node -> builder.append(node.position().toString()));
        return builder.toString();
    }

    @Test
    public void preconditions() {
        //check preconditions of the test
        Assert.assertNotNull(map1);
        Assert.assertNotNull(map1.start());
        Assert.assertNotNull(map1.target());
        Assert.assertNotEquals(map1.start(), map1.target());
        //a test map should not end where it starts, this will confuse the algorithms in most cases
        Assert.assertFalse(map1.start().paths().stream().anyMatch(path -> path.end().equals(map1.target())));
        Assert.assertFalse(expectedPath1.isEmpty());
    }

    @Test
    public void dijkstra1() {
        //test dijkstra pathfinding
        Optional<ResultPath<Vector2f>> dijkstra = Pathfinding.dijkstra(map1);
        Assert.assertTrue(dijkstra.isPresent());
        ResultPath<Vector2f> path = dijkstra.get();
        Assert.assertNotNull(path);
        Assert.assertNotNull(path.fullPath());
        Assert.assertFalse(path.fullPath().isEmpty());
        String pathString = pathToString(path.fullPath());
        Assert.assertEquals(expectedPath1, pathString);
    }

    @Test
    public void astar1() {
        //test astar pathfinding
        //the hcost is defined by the distance squared which should work fine for astar
        Optional<ResultPath<Vector2f>> astar = Pathfinding.astar(map1, Vector2f::distanceSquared);
        Assert.assertTrue(astar.isPresent());
        ResultPath<Vector2f> path = astar.get();
        Assert.assertNotNull(path);
        Assert.assertNotNull(path.fullPath());
        Assert.assertFalse(path.fullPath().isEmpty());
        String pathString = pathToString(path.fullPath());
        Assert.assertEquals(expectedPath1, pathString);
    }
}