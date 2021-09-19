package tiles;

import ecs.GameObject;
import ecs.SpriteRenderer;
import ecs.StaticCollider;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics.collision.Shapes;
import scene.Scene;
import util.Utils;

public class TilesystemSideScroll {
    Spritesheet sheet;
    GameObject[][] gameObjects = new GameObject[0][0];
    MapHandler m;

    GameObject background;

    int w, h;

    public TilesystemSideScroll(Scene scene, Spritesheet s, int xTiles, int yTiles, int width, int height, GameObject c) {
        sheet = s;
        gameObjects = new GameObject[xTiles][yTiles];
        m = new MapHandler(xTiles, yTiles, 30);
        w = width;
        h = height;

        int i = 0;

        for (int x = 0; x < xTiles; x++) {
            for (int y = 0; y < yTiles; y++) {
                gameObjects[x][y] = new GameObject(scene, "Tile " + i, new Vector3f(x * width, y * height, 0), 0);

                if (m.getMap()[x][y] == 1) {
                    //gameObjects[x][y].addComponent(new AABB());
                    gameObjects[x][y].addComponent(new StaticCollider(Shapes.axisAlignedRectangle(0, 0, width, height), 2));
                    gameObjects[x][y].addComponent(new SpriteRenderer(s.getSprite(
                            Utils.randomInt(0, 6) == 0 ? 11 : Utils.randomInt(1, 5)

                    ), new Vector2f(width, height)));

                } else {
                    if (m.getMap()[x][y] != 1 && m.getMap()[x][y - 1] == 1) {
                        if (Utils.randomInt(0, 5) == 1) {
                            gameObjects[x][y].addComponent(new SpriteRenderer(s.getSprite(19), new Vector2f(width, height))); // hanging vines
                        } else if (Utils.randomInt(0, 5) == 1) {
                            gameObjects[x][y].addComponent(new SpriteRenderer(s.getSprite(25), new Vector2f(width, height)));
                        }
                    }
                }
                i++;
            }
        }
    }

    public int getType(int worldX, int worldY) {
        int x = (int) worldX / w;
        int y = (int) worldY / h;

        return m.getMap()[x][y];
    }

    public int[] getIndex(int worldX, int worldY) {
        int x = (int) worldX / w;
        int y = (int) worldY / h;

        return new int[]{x, y};
    }

}