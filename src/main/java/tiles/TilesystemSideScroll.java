package tiles;

import ecs.GameObject;
import ecs.PolygonCollider;
import ecs.SpriteRenderer;
import graphics.Spritesheet;
import org.joml.Vector2f;
import physics.collision.Shapes;
import scene.Scene;
import util.MathUtils;

/**
 * 
 * An example class of a platformer stage and it's tilemap. Eventually I think this should
 * be expanded to a more general API, maybe even with a {@link Scene} dedicated to an
 * in-engine way of constructing them (Mario Maker flavoured Azurite?), but that's likely
 * a future plan; I, the mortal JavaDocumentator, don't have insight into the minds of
 * the owners of this game engine (go ask Asher, idk).
 * 
 * lol see {@link Tilesystem}, this is now deprectated :D 
 * -Asher
 * 
 * @see Scene
 * @see Tilesystem
 */

@Deprecated
public class TilesystemSideScroll {
    Spritesheet sheet;
    GameObject[][] gameObjects = new GameObject[0][0];
    MapHandler m;

    GameObject background;

    int w, h;

    public TilesystemSideScroll(Spritesheet s, int xTiles, int yTiles, int width, int height, GameObject c, int[] layers) {
        sheet = s;
        gameObjects = new GameObject[xTiles][yTiles];
        m = new MapHandler(xTiles, yTiles, 30);
        w = width;
        h = height;

        int i = 0;

        for (int x = 0; x < xTiles; x++) {
            for (int y = 0; y < yTiles; y++) {
                gameObjects[x][y] = new GameObject("Tile " + i, new Vector2f(x * width, y * height), 0);

                if (m.getMap()[x][y] == 1) {
                    gameObjects[x][y].addComponent(new PolygonCollider(Shapes.axisAlignedRectangle(0, 0, width, height)).layer(layers));
                    gameObjects[x][y].addComponent(new SpriteRenderer(s.getSprite(
                            MathUtils.randomInt(0, 6) == 0 ? 11 : MathUtils.randomInt(1, 5)

                    ), new Vector2f(width, height)));

                } else {
                    if (m.getMap()[x][y] != 1 && m.getMap()[x][y - 1] == 1) {
                        if (MathUtils.randomInt(0, 5) == 1) {
                            gameObjects[x][y].addComponent(new SpriteRenderer(s.getSprite(19), new Vector2f(width, height))); // hanging vines
                        } else if (MathUtils.randomInt(0, 5) == 1) {
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