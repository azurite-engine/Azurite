package ecs;

import fonts.Font;
import fonts.Glyph;
import graphics.Color;
import graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics.Transform;

import java.util.ArrayList;

public class Text {
    private Vector4f color = new Color(255, 100, 100, 255).toNormalizedVec4f();

    private Sprite sprite;

    private Transform lastTransform;
    private boolean isDirty = false; // Dirty flag, tells renderer to redraw if object components have changed

    ArrayList<GameObject> gameObjects;

    float x, y;
    Font font;
    CharSequence text;

    public Text (String string, Font font, float x, float y) {
        this.text = string;
        this.font = font;
        this.x = x;
        this.y = y;
        gameObjects = new ArrayList<>();
    }

    public void start () {
        draw();
    }

    public void draw () {
        int textHeight = font.getHeight(text);

        float drawX = x;
        float drawY = y;
        if (textHeight > font.getFontHeight()) {
            drawY += textHeight - font.getFontHeight();
        }

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                /* Line feed, set x and y to draw at the next line */
                drawY -= font.getFontHeight();
                drawX = x;
                continue;
            }
            if (ch == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            Glyph g = font.getGlyphs().get(ch);

            Texture texture = font.getTexture();

            float topY = (g.y + g.height) / (float)texture.getHeight();
            float rightX = (g.x + g.width) / (float)texture.getWidth();
            float leftX = g.x / (float)texture.getWidth();
            float bottomY = g.y / (float)texture.getHeight();

            Vector2f[] uv = {
                    new Vector2f(rightX, bottomY),
                    new Vector2f(rightX, topY),
                    new Vector2f(leftX, topY),
                    new Vector2f(leftX, bottomY)
            };

            Sprite s = new Sprite(font.getTexture(), uv);
            gameObjects.add(new GameObject("" + ch, new Transform(drawX, drawY, g.width, g.height), 1).addComponent(new SpriteRenderer(s)));


//            System.out.println("" + ch);

//            renderer.drawTextureRegion(texture, drawX, drawY, g.x, g.y, g.width, g.height, c);
            drawX += g.width;
        }
    }
}
