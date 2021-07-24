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
import util.Engine;

import java.util.ArrayList;

/**
 * @author Asher Haun
 */
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
        Engine.scenes().currentScene().textRenderer.add(this);
    }

    public Text (String string, float x, float y) {
        this.text = string;
        this.font = new Font();
        this.x = x;
        this.y = y;
        this.zIndex = 1;

        glyphRenderers = new ArrayList<>();

        draw();
        Engine.scenes().currentScene().textRenderer.add(this);
    }

    public void draw () {
        generateGlyphs();
    }

    public void change (String string) {
        Engine.scenes().currentScene().textRenderer.removeAllGlyphRenderers(glyphRenderers);
        System.out.println("");

        String tmp = "";
        for (int i = 0; i < this.text.length() - string.length(); i ++) {
            tmp += " ";
        }
        this.text = string + tmp;
        glyphRenderers = new ArrayList<>();

        generateGlyphs();
        Engine.scenes().currentScene().textRenderer.add(this);
    }

    private void generateGlyphs () {
        int textHeight = font.getHeight(text);
        int lineIncreases = 0;

        float drawX = x;
        float drawY = y;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                lineIncreases ++;
                /* Line feed, set x and y to draw at the next line */
                drawY = y + (font.getFontHeight() * lineIncreases);
                drawX = x;
                continue;
            }
            if (ch == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            Glyph g = font.getGlyphs().get(ch);

            glyphRenderers.add(new GlyphRenderer(new Transform(drawX, drawY, g.width, g.height), g, this, ch));

            drawX += g.width;
        }
        if (textHeight > font.getFontHeight()) {
            drawY += textHeight - font.getFontHeight();
        }
    }

    public ArrayList<GlyphRenderer> getGlyphRenderers () {
        return glyphRenderers;
    }

    public int zIndex () {
        return zIndex;
    }

    public float getX () {
        return x;
    }

    public void setX (float x) {
        this.x = x;
    }

    public float getY () {
        return y;
    }

    public void setY (float y) {
        this.y = y;
    }

    public float addY (float y) {
        return y += y;
    }

    public void addX (float x) {
        this.x = x;
    }
}
