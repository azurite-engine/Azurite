package misc;

import ecs.GameObject;
import ecs.SpriteRenderer;
import graphics.Spritesheet;
import physics.Transform;
import util.Utils;

public class Tilesystem {
    Spritesheet sheet;
    GameObject[][] gameObjects = new GameObject[0][0];
    int w, h;

    public Tilesystem(Spritesheet s, int xTiles, int yTiles, int width, int height) {
        sheet = s;
        gameObjects = new GameObject[xTiles][yTiles];
        w = width;
        h = height;

        int i = 0;

        for (int x = 0; x < xTiles; x ++) {
            for (int y = 0; y < yTiles; y ++) {
                gameObjects[x][y] = new GameObject("Tile " + i, new Transform(x * width, y * height, width, height), 0);

                gameObjects[x][y].addComponent(new SpriteRenderer(s.getSprite(
                        Utils.randomInt(0, 6)==0?0: Utils.randomInt(17, 17)
                )));

                i ++;
            }
        }
    }

//    public int getType (int worldX, int worldY) {
//        int x = (int)worldX/w;
//        int y = (int)worldY/h;
//
//        return m.getMap()[x][y];
//    }

    public int[] getIndex (int worldX, int worldY) {
        int x = (int)worldX/w;
        int y = (int)worldY/h;

        return new int[] {x, y};
    }

    public Transform getTransform (int worldX, int worldY) {
        int x = (int)worldX/w;
        int y = (int)worldY/h;

        return gameObjects[x][y].getTransform();
    }

}