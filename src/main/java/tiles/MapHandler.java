package tiles;

import util.MathUtils;

import java.util.ArrayList;

/*
    Ported from C# version
    http://www.roguebasin.com/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels#:~:text=It%20is%20an%20old%20and%20fairly%20well%20documented,a%20wall%20and%205%20or%20more%20neighbors%20were.
*/

@Deprecated
class MapHandler {
    public int MapWidth = 90;
    public int MapHeight = 21;
    public int PercentAreWalls = 50;
    public int[][] Map = new int[MapWidth][MapHeight];
    int rand = MathUtils.randomInt(0, 100);

    public MapHandler(int w, int h, int f) {
        MapWidth = w;
        MapHeight = h;
        PercentAreWalls = f;
        RandomFillMap();
        MakeCaverns();
    }

    public MapHandler() {
        RandomFillMap();
        MakeCaverns();
    }

    public MapHandler(int mapWidth, int mapHeight, int[][] map, int percentWalls) {
        this.MapWidth = mapWidth;
        this.MapHeight = mapHeight;
        this.PercentAreWalls = percentWalls;
        this.Map = new int[this.MapWidth][this.MapHeight];
        this.Map = map;
    }

    public void MakeCaverns() {
        // By initializing column in the outer loop, its only created ONCE
        for (int column = 0, row = 0; row <= MapHeight - 1; row++) {
            for (column = 0; column <= MapWidth - 1; column++) {
                Map[column][row] = PlaceWallLogic(column, row);
            }
        }
    }

    public int PlaceWallLogic(int x, int y) {
        int numWalls = GetAdjacentWalls(x, y, 1, 1);


        if (Map[x][y] == 1) {
            if (numWalls >= 4) {
                return 1;
            }
            if (numWalls < 2) {
                return 0;
            }

        } else {
            if (numWalls >= 5) {
                return 1;
            }
        }
        return 0;
    }

    public int GetAdjacentWalls(int x, int y, int scopeX, int scopeY) {
        int startX = x - scopeX;
        int startY = y - scopeY;
        int endX = x + scopeX;
        int endY = y + scopeY;

        int iX = startX;
        int iY = startY;

        int wallCounter = 0;

        for (iY = startY; iY <= endY; iY++) {
            for (iX = startX; iX <= endX; iX++) {
                if (!(iX == x && iY == y)) {
                    if (IsWall(iX, iY)) {
                        wallCounter += 1;
                    }
                }
            }
        }
        return wallCounter;
    }

    boolean IsWall(int x, int y) {
        // Consider out-of-bound a wall
        if (IsOutOfBounds(x, y)) {
            return true;
        }

        if (Map[x][y] == 1) {
            return true;
        }

        if (Map[x][y] == 0) {
            return false;
        }
        return false;
    }

    boolean IsOutOfBounds(int x, int y) {
        if (x < 0 || y < 0) {
            return true;
        } else if (x > MapWidth - 1 || y > MapHeight - 1) {
            return true;
        }
        return false;
    }

    String MapToString() {
        String returnString = String.join(" ", // Seperator between each element
                "Width:",
                MapWidth + "",
                "\tHeight:",
                MapHeight + "",
                "\t% Walls:",
                PercentAreWalls + "",
                "\n"
        );

        ArrayList<String> mapSymbols = new ArrayList<String>();
        mapSymbols.add(".");
        mapSymbols.add("#");
        mapSymbols.add("+");

        for (int column = 0, row = 0; row < MapHeight; row++) {
            for (column = 0; column < MapWidth; column++) {
                if (Map[column][row] == 0) {
                    returnString += '.';
                } else {
                    returnString += '#';
                }
            }
            returnString += "\n";
        }
        return returnString;
    }

    public int[][] getMap() {
        return Map;
    }

    public void BlankMap() {
        for (int column = 0, row = 0; row < MapHeight; row++) {
            for (column = 0; column < MapWidth; column++) {
                Map[column][row] = 0;
            }
        }
    }

    public void RandomFillMap() {
        // New, empty map
        Map = new int[MapWidth][MapHeight];

        int mapMiddle = 0; // Temp variable
        for (int row = 0; row < MapHeight; row++) {
            for (int column = 0; column < MapWidth; column++) {
                // If coordinants lie on the the edge of the map (creates a border)
                if (column == 0) {
                    Map[column][row] = 1;
                } else if (row == 0) {
                    Map[column][row] = 1;
                } else if (column == MapWidth - 1) {
                    Map[column][row] = 1;
                } else if (row == MapHeight - 1) {
                    Map[column][row] = 1;
                }
                // Else, fill with a wall a random percent of the time
                else {
                    mapMiddle = (MapHeight / 2);

                    if (row == mapMiddle) {
                        Map[column][row] = 0;
                    } else {
                        Map[column][row] = RandomPercent(PercentAreWalls);
                    }
                }
            }
        }
    }

    int RandomPercent(int percent) {
        if (percent >= MathUtils.random(1, 101)) {
            return 1;
        }
        return 0;
    }
}
