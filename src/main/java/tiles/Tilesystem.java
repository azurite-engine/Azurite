package tiles;

import ecs.GameObject;
import ecs.SpriteRenderer;
import graphics.Spritesheet;
import graphics.Texture;
import io.xml.XML;
import io.xml.XMLElement;
import org.joml.Vector2f;
import util.Assets;
import util.MathUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * The Tilesystem class loads a .tmx file (XML file format for the Tiled map editor) and generates a 2D array of GameObjects with SpriteRenderers corresponding to the texture determined by the .tmx file.
 * Currently only Axis-aligned maps are supported (no isometric or hexagonal maps right now).
 */
public class Tilesystem {

    ArrayList<Tileset> tilesets = new ArrayList<>();
    ArrayList<Layer> layers = new ArrayList<>();

    GameObject[][] gameObjects;
    int tileWidth, tileHeight;

    public Tilesystem(String tmxFile, int width, int height) {

        // Find CWD path
        Path tilesetPath = Paths.get(tmxFile);
        String directory = tilesetPath.getParent().toString();

        this.tileWidth = width;
        this.tileHeight = height;

        // Extract XML data
        XMLElement root = XML.parse(Assets.getDataFile(tmxFile).array());

        // Determine number of tiles on X and Y axis'
        int xTiles = Integer.parseInt(root.getAttributes().get("width"));
        int yTiles = Integer.parseInt(root.getAttributes().get("height"));

        for (XMLElement i : root.getChildren()) {
            // Determine number of tilesets and save their data
            if (i.getTag().equals("tileset")) {
                tilesets.add(new Tileset(
                        Integer.parseInt(i.getAttributes().get("firstgid")),
                        new File(directory, i.getAttributes().get("source")).toPath()
                ));
            }

            // Determine number of layers and save their data
            if (i.getTag().equals("layer")) {

                String[] mapString = i.getChildren().get(0).getValue().split(",");

                int[] map = new int[mapString.length];
                for (int j = 0; j < mapString.length; j++) {
                    map[j] = Integer.parseInt(mapString[j].trim());
                }

                layers.add(new Layer(
                        Integer.parseInt(i.getAttributes().get("id")),
                        i.getAttributes().get("name"),
                        Integer.parseInt(i.getAttributes().get("width")),
                        Integer.parseInt(i.getAttributes().get("height")),
                        map
                ));
            }
        }

        gameObjects = new GameObject[xTiles][yTiles];

        for (int y = 0; y < yTiles; y++) {
            for (int x = 0; x < xTiles; x++) {
                for (Layer layer : layers) {

                    // Get the raw ID from the tmx layer
                    int tileID = getAt(x, y, layer.width, layer.map);

                    // Determine which tileset to pull sprites from
                    Tileset ts = getTileSet(tileID);

                    // Normalize the index according to range defined by tileset tags
                    int index = getNormalizedIndex(tileID, ts);

                    gameObjects[x][y] = new GameObject(new Vector2f(x * tileWidth, y * tileHeight), 0).addComponent(
                            new SpriteRenderer(ts.spritesheet.getSprite(index), new Vector2f(width, height))
                    );
                }
            }
        }
    }

    private Tileset getTileSet(int id) {
        int currentFirstGID = 1;
        for (int i = 0; i < tilesets.size(); i++) {
            Tileset t = tilesets.get(i);
            if (currentFirstGID < id && id < t.firstgid + t.spritesheet.getSize()) {
                return t;
            }
            currentFirstGID = t.firstgid;
        }

        return tilesets.get(0);
    }

    public int getAt(int x, int y, int dimensionWidth, int[] map) {
        return map[(dimensionWidth * y) + x];
    }

    private int getNormalizedIndex(int id, Tileset t) {
        return (int) MathUtils.constrain(MathUtils.map(id, t.firstgid, t.firstgid + t.spritesheet.getSize(), 0, t.spritesheet.getSize()), 0, t.spritesheet.getSize());
    }

    public int[] getIndex(int worldX, int worldY) {
        int x = worldX / tileWidth;
        int y = worldY / tileHeight;

        return new int[]{x, y};
    }
}

class Tileset {
    protected Spritesheet spritesheet;
    protected int firstgid;
    protected Path source;

    Tileset(int firstgid, Path source) {
        this.firstgid = firstgid;
        this.source = source;

        // Parse the .tsx file to extract the spritesheet data and texture path
        XMLElement tsRoot = XML.parse(Assets.getDataFile(this.source.toString()).array());

        int tileWidth = Integer.parseInt(tsRoot.getAttributes().get("tilewidth"));
        int tileHeight = Integer.parseInt(tsRoot.getAttributes().get("tileheight"));
        int tileCount = Integer.parseInt(tsRoot.getAttributes().get("tilecount"));

        String spriteSheetPath = new File(this.source.getParent().toString(), tsRoot.getChildren().get(0).getAttributes().get("source")).toString();

        this.spritesheet = new Spritesheet(new Texture(spriteSheetPath), tileWidth, tileHeight, tileCount, 0);
    }
}

class Layer {
    protected int id;
    protected String name;
    protected int width, height;
    protected int map[] = {};

    public Layer(int id, String name, int width, int height, int[] map) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.map = map;
    }
}