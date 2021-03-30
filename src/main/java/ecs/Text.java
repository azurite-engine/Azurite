package ecs;

import fonts.Font;
import fonts.Glyph;
import fonts.GlyphRenderer;
import graphics.Color;
import graphics.Texture;
import graphics.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics.Transform;

import java.util.ArrayList;

public class Text {
    private Vector4f color = Color.RED.toNormalizedVec4f();

    private Sprite sprite;

    private Transform lastTransform;
    private boolean isDirty = false; // Dirty flag, tells renderer to redraw if object components have changed

    int zIndex;

    ArrayList<GlyphRenderer> glyphRenderers;

    float x, y;
    Font font;
    CharSequence text;

    public Text (String string, Font font, float x, float y, int zIndex) {
        this.text = string;
        this.font = font;
        this.x = x;
        this.y = y;
        this.zIndex = zIndex;

        glyphRenderers = new ArrayList<>();

        draw();
        Window.currentScene.textRenderer.add(this);
    }

    public Text (String string, float x, float y) {
        this.text = string;
        this.font = new Font();
        this.x = x;
        this.y = y;
        this.zIndex = 1;

        glyphRenderers = new ArrayList<>();

        draw();
        Window.currentScene.textRenderer.add(this);
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
                drawY += font.getFontHeight();
                drawX = x;
                continue;
            }
            if (ch == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            Glyph g = font.getGlyphs().get(ch);

            glyphRenderers.add(new GlyphRenderer(new Transform(drawX, drawY, g.width, g.height), g, this));

            drawX += g.width;
        }
    }

    public void change (String string) {
        this.text = string;
        glyphRenderers = new ArrayList<>();
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
                drawY += font.getFontHeight();
                drawX = x;
                continue;
            }
            if (ch == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            Glyph g = font.getGlyphs().get(ch);

            glyphRenderers.add(new GlyphRenderer(new Transform(drawX, drawY, g.width, g.height), g, this));

            drawX += g.width;
        }
    }

    public ArrayList<GlyphRenderer> getGlyphRenderers () {
        return glyphRenderers;
    }

    public int zIndex () {
        return zIndex;
    }
}
