package fonts;

import org.joml.Vector2f;

public class CharInfo {
    public int sourceX;
    public int sourceY;
    public int width;
    public int height;

    public Vector2f[] textureCoordinates = new Vector2f[4];

    public CharInfo(int sourceX, int sourceY, int width, int height) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.width = width;
        this.height = height;
    }

    public void calculateTextureCoordinates(int fontWidth, int fontHeight) {
        float x0 = (float)sourceX / (float)fontWidth;
        float x1 = (float)(sourceX + width) / (float)fontWidth;
        float y0 = (float)(sourceY - height) / (float)fontHeight;
        float y1 = (float)(sourceY) / (float)fontHeight;

        textureCoordinates[0] = new Vector2f(x0, y1);
        textureCoordinates[1] = new Vector2f(x1, y0);
    }
}
