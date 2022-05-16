package ui;

import ui.fonts.Font;
import ui.fonts.Glyph;
import ui.fonts.GlyphRenderer;
import graphics.Color;
import graphics.HSLColor;
import graphics.renderer.TextRenderer;
import org.joml.Vector2f;
import util.Engine;
import util.MathUtils;
import util.Transform;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author Asher Haun
 * Represents a renderable string containing a TTF font reference, color data, and font size.
 */
public class Text {

    private Transform lastTransform = new Transform();
    private Transform transform = new Transform();
    private int zIndex;

    private boolean isSticky = false;
    private boolean isCentered = false;

    private ArrayList<GlyphRenderer> glyphRenderers;
    private Color color = Color.WHITE;

    private Font font;
    private CharSequence text;

    /**
     * @param string the text to be rendered.
     * @param font the {@link ui.fonts.Font} object that contains your preferred .ttf font file.
     * @param color the {@link graphics.Color} object that contains your prefered RGBA color.
     * @param x the X position of the Text object.
     * @param y the Y position of the Text object.
     * @param zIndex the Z-Index of the Text Object (currently can only be set on creation, this will change in the future).
     * @param isSticky boolean determining whether the text sticks in one position regardless of the camera position (isSticky = true) or if it will move in world space.
     * @param isCentered boolean determining whether the text is left aligned or center aligned. This can be changed after creation using .setCentered().
     */
    public Text (String string, Font font, Color color, float x, float y, int zIndex, boolean isSticky, boolean isCentered) {
        this.text = string;
        // String sizes are automatically chopped off at a certain length due to rendering speed and memory limitations.
//        if (string.length() >= TextRenderer.getMaxBatchSize()) {
//            Logger.logInfo("The String \"" + string.substring(0, 7) + "...\" passed is longer than the allowed string size for text: " + TextRenderer.getMaxBatchSize());
//            this.text = string.substring(0, TextRenderer.getMaxBatchSize() - 4) + "...";
//        }

        this.font = font;
        this.color = color;

        this.transform.setPosition(new Vector2f(x, y));
        this.lastTransform.setPosition(new Vector2f(x, y));
        this.zIndex = zIndex;

        this.isSticky = isSticky;
        this.isCentered = isCentered;

        glyphRenderers = new ArrayList<>();

        generateGlyphs();
        Engine.scenes().currentScene().textRenderer.add(this);
        Engine.scenes().currentScene().addText(this);
    }

    /**
     * @param string the text to be rendered.
     * @param font the {@link ui.fonts.Font} object that contains your preferred .ttf font file.
     * @param color the {@link graphics.Color} object that contains your prefered RGBA color.
     * @param x the X position of the Text object.
     * @param y the Y position of the Text object.
     */
    public Text (String string, Font font, Color color, float x, float y) {
        this(string, font, color, x, y, 1, true, false);
    }

    /**
     * @param string the text to be rendered.
     * @param color the {@link graphics.Color} object that contains your prefered RGBA color.
     * @param x the X position of the Text object.
     * @param y the Y position of the Text object.
     */
    public Text (String string, Color color, float x, float y) {
        this(string, new Font(), color, x, y, 1, true, false);
    }

    /**
     * @param string the text to be rendered.
     * @param x the X position of the Text object.
     * @param y the Y position of the Text object.
     */
    public Text (String string, float x, float y) {
        this(string, new Font(), Color.BLUE, x, y, 1, false, false);
    }

    /**
     * Update method called for every text object by the scene.Scene.updateUI() method.
     * This should not be called by general users.
     */
    public void update () {
        if (!lastTransform.equals(this.transform)) {
            Vector2f movementDelta = new Vector2f(transform.getX() - lastTransform.getX(), transform.getY() - lastTransform.getY());

            for (GlyphRenderer i : glyphRenderers) {
                i.updatePosition(movementDelta);
            }
            for (GlyphRenderer i : glyphRenderers) {
                i.update(Engine.deltaTime());
            }
        }
        lastTransform.setX(transform.getX());
        lastTransform.setY(transform.getY());
    }

    /**
     * This method is called when the user wants to modify the string in the Text object.
     * This can be called anytime after object creation.
     * @param string the text to change the current string to.
     */
    public void change (String string) {
        glyphRenderers.clear();
        this.text = string + " ";
        generateGlyphs();
    }

    public String getText () {
        return (String) this.text;
    }

    private char ch;
    private float lineWidth = 0;
    /**
     * Calculates the width of a single line of text based on the Glyph size for each character contained in the CharSequence (a lower level representation of String).
     * @return the width in pixels of the line.
     */
    private float calculateLineWidth (CharSequence line) {
        float drawX = 0;

        for (int i = 0; i < line.length(); i++) {
            ch = line.charAt(i);

            if (ch == '\r') continue;

            drawX += font.getGlyphs().get(ch).width;
            lineWidth = drawX;
        }

        return lineWidth;
    }

    /**
     * Creates the glyphs (essentially sprites) for each character in the string at the appropriate position relative to the anchor point and text alignment.
     */
    int textHeight;
    float maxTextWidth;
    private void generateGlyphs () {

        float[] lineLengths = new float[1];
        if (isCentered) {
            // Split the CharSequence/string at each line break "\n"
            Pattern pattern = Pattern.compile("\n");
            CharSequence[] lines = pattern.split(text);

            // Create and fill and array of line lengths, in pixels for each line of text.
            lineLengths = new float[lines.length];
            for (int i = 0; i < lines.length; i++) {
                lineLengths[i] = calculateLineWidth(lines[i]);
            }

            // Determine which line is the longest
            for (float i : lineLengths) {
                if (i > maxTextWidth) {
                    maxTextWidth = i;
                }
            }
        }

        textHeight = font.getHeight(text);
        int lineIncreases = 0;

        // Get the anchor point of the Text object
        Transform t = transform.copy();
        float drawX = t.getX();
        float drawY = t.getY();

        for (int i = 0; i < text.length(); i++) {
            // String sizes are automatically chopped off at a certain length due to rendering speed and memory limitations.
            if (i >= TextRenderer.getMaxBatchSize() - 3) {
                // Replace the last three characters of the string with "..."
                if (i < TextRenderer.getMaxBatchSize()) {
                    ch = '.';
                } else break;
            } else ch = text.charAt(i);

            if (ch == '\n') {
                // Line break, set x and y to draw at the next line and continue since there is nothing to draw.
                lineIncreases ++;

                drawY = t.getY() + (font.getFontHeight() * lineIncreases);
                drawX = t.getX();

                continue;
            }

            // Carriage return - cannot be drawn.
            if (ch == '\r') continue;

            // Add the Glyph that corresponds to the current character to the arrayList of glyphRenders.
            Glyph g = font.getGlyphs().get(ch);

            if (!isCentered) {
                glyphRenderers.add(new GlyphRenderer(new Transform(drawX, drawY, g.width, g.height), g, this, ch, isSticky, this.color));
            } else {
                glyphRenderers.add(new GlyphRenderer(new Transform(MathUtils.round(drawX - lineLengths[lineIncreases]/2), drawY, g.width, g.height), g, this, ch, isSticky, this.color));
            }

            drawX += g.width;
            lineWidth = 0;
        }
        if (textHeight > font.getFontHeight()) {
            drawY += textHeight - font.getFontHeight();
        }
    }

    public ArrayList<GlyphRenderer> getGlyphRenderers () {
        return glyphRenderers;
    }

    /**
     * Set the color of the entire Text object.
     * @param color RGBA Color object.
     */
    public void setColor (Color color) {
        this.color = color;
        for (GlyphRenderer g : this.glyphRenderers) {
            g.setColor(this.color);
        }
    }

    /**
     * Applies a rainbow effect to the Text, gradually changing the color of each Glyph in a rainbow.
     */
    public void rainbowify () {
        for (int i = 0; i < this.glyphRenderers.size(); i ++) {
            GlyphRenderer g = this.glyphRenderers.get(i);

            g.setColor(new HSLColor(MathUtils.map(i, 0, this.glyphRenderers.size(), 0, 360), 100, 50, 1).toRGBColor());
        }
    }

// TODO, add support to change this after creation. (this should be quite easy, I am just tired rn)
//    public boolean isSticky () {
//        return isSticky;
//    }

    /**
     * @return boolean value of isCentered.
     */
    public boolean isCentered() {
        return isCentered;
    }

    /**
     * Allows the user to change the text alignment after creation.
     * @param centered boolean isCentered.
     */
    public void setCentered (boolean centered) {
        if (centered == isCentered) return;
        isCentered = centered;
        change((String) text);
    }

    public int zIndex () {
        return zIndex;
    }

    public float getX () {
        return transform.getX();
    }

    public float getY () {
        return transform.getY();
    }

    public float getHeight () {
        return textHeight;
    }

    public float getLongestLineWidth () {
        return maxTextWidth;
    }

    public void setX (float x) {
        transform.setX(x);
    }

    public void setY (float y) {
        transform.setY(y);
    }

    /**
     * Allows the user to change the position of the Text object.
     * @param position Vector2f containing new position data.
     */
    public void setPosition (Vector2f position) {
        setX(position.x());
        setY(position.y());
    }

    /**
     * Allows the user to change the position of the Text object.
     * @param x x position
     * @param y y position
     */
    public void setPosition (float x, float y) {
        setX(x);
        setY(y);
    }

    /**
     * Add to the existing Y position incrementally each time this method is called.
     * @param y amount to increment Y position by.
     */
    public void addY (float y) {
        transform.addY(y);
    }

    /**
     * Add to the existing X position incrementally each time this method is called.
     * @param x amount to increment X position by.
     */
    public void addX (float x) {
        transform.addX(x);
    }

}
