package ui.fonts;

import graphics.Sprite;
import graphics.Texture;
import org.joml.Vector2f;

/**
 * This class represents a font glyph.
 *
 * @author Asher Haun
 */
public class Glyph extends Sprite {

    public final int width;
    public final int height;
    public final int x;
    public final int y;
    public Vector2f[] uvCoordinates;

    /**
     * Creates a font Glyph.
     *
     * @param width   Width of the Glyph
     * @param height  Height of the Glyph
     * @param x       X coordinate on the font texture
     * @param y       Y coordinate on the font texture
     */
    public Glyph(int width, int height, int x, int y) {
        super(new Texture());
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void calculateUVs (Texture texture) {
        this.texture = texture;

        float topY = (y + height) / (float)texture.getHeight();
        float rightX = (x + width) / (float)texture.getWidth();
        float leftX = x / (float)texture.getWidth();
        float bottomY = y / (float)texture.getHeight();

        uvCoordinates = new Vector2f[] {
                new Vector2f(rightX, bottomY),
                new Vector2f(rightX, topY),
                new Vector2f(leftX, topY),
                new Vector2f(leftX, bottomY)
        };
    }

    public Vector2f[] getUV () {
        return uvCoordinates;
    }

}
