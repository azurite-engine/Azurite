package ecs;

import fonts.Font;
import fonts.Glyph;
import fonts.GlyphRenderer;
import graphics.Color;
import graphics.renderer.TextRendererBatch;
import org.joml.Vector2f;
import physics.Transform;
import util.Engine;
import util.Logger;

import java.util.ArrayList;

/**
 * @author Asher Haun
 */
public class Text {
    private Color color = Color.WHITE;

    private Sprite sprite;

    private Transform lastTransform = new Transform();
    private boolean isDirty = false; // Dirty flag, tells renderer to redraw if object components have changed
    private boolean isSticky = false;
    int zIndex;

    ArrayList<GlyphRenderer> glyphRenderers;

    Transform transform = new Transform();
    Font font;
    CharSequence text;

    TextRendererBatch currentBatch;

    public Text (String string, Font font, float x, float y, int zIndex, boolean isSticky) {
        this.text = string;
        this.font = font;
        this.transform.setX(x);
        this.transform.setY(y);
        this.zIndex = zIndex;
        this.isSticky = isSticky;

        glyphRenderers = new ArrayList<>();

        generateGlyphs();
        Engine.scenes().currentScene().textRenderer.add(this);
        Engine.scenes().currentScene().addUiObject(this);

        currentBatch = glyphRenderers.get(0).getBatch();
    }

    public Text (String string, float x, float y) {
        this.text = string;
        this.font = new Font();
        this.transform.setX(x);
        this.transform.setY(y);
        this.zIndex = 1;

        glyphRenderers = new ArrayList<>();

        generateGlyphs();
        Engine.scenes().currentScene().textRenderer.add(this);
        Engine.scenes().currentScene().addUiObject(this);

        currentBatch = glyphRenderers.get(0).getBatch();
    }

    public void update () {
        if (!lastTransform.equals(this.transform)) {
            Vector2f movementDelta = transform.getPosition().sub(lastTransform.getPosition());
            transform.copy(lastTransform);
            for (GlyphRenderer i : glyphRenderers) {
                i.addX(movementDelta.x);
                i.addY(movementDelta.y);
            }
            for (GlyphRenderer i : glyphRenderers) {
                i.update(Engine.deltaTime());
            }
        }
    }

    public void change (String string) {
        currentBatch = glyphRenderers.get(0).getBatch();
        glyphRenderers.clear();

        this.text = string;

        generateGlyphs();
        Engine.scenes().currentScene().textRenderer.changeText(this, currentBatch);
    }

    private void generateGlyphs () {
        int textHeight = font.getHeight(text);
        int lineIncreases = 0;

        float drawX = transform.getX();
        float drawY = transform.getY();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                lineIncreases ++;
                /* Line feed, set x and y to draw at the next line */
                drawY = transform.getY() + (font.getFontHeight() * lineIncreases);
                drawX = transform.getX();
                continue;
            }
            if (ch == '\r') {
                /* Carriage return, just skip it */
                continue;
            }
            Glyph g = font.getGlyphs().get(ch);

            glyphRenderers.add(new GlyphRenderer(new Transform(drawX, drawY, g.width, g.height), g, this, ch, isSticky, this.color));

            drawX += g.width;
        }
        if (textHeight > font.getFontHeight()) {
            drawY += textHeight - font.getFontHeight();
        }
    }

    public int getBatchIndex () {
        if (glyphRenderers.size() == 0) return -1;
        return glyphRenderers.get(0).getBatchIndex();
    }

    public ArrayList<GlyphRenderer> getGlyphRenderers () {
        return glyphRenderers;
    }

    public boolean isSticky () {
        return isSticky;
    }

    /**
     * @return Transform of the gameObject
     */
    public Transform getTransform() {
        return this.transform;
    }

    /**
     * Takes a Transform as a parameter and sets this instance to a copy of that transform
     *
     * @param t
     */
    public void setTransform(Transform t) {
        this.transform = t.copy();
    }

    public int zIndex () {
        return zIndex;
    }

    public void setZindex(int z) {
        zIndex = z;
    }

    public float getX () {
        return transform.getX();
    }

    public void setX (float x) {
        transform.setX(x);
    }

    public float getY () {
        return transform.getY();
    }

    public void setY (float y) {
        transform.setY(y);
    }

    public void addY (float y) {
        transform.addY(y);
    }

    public void addX (float x) {
        transform.addX(x);
    }
}
