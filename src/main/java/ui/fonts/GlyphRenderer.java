package ui.fonts;

import ui.Text;
import graphics.Color;
import graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.MathUtils;
import util.Transform;
import ui.fonts.Glyph;

import static graphics.Color.BLUE;

/**
 * @author Asher Haun
 */
public class GlyphRenderer {

    private Vector4f color;

    private Glyph glyph;
    private char character;
    private Text parentText;

    private Transform localTransform;
    private Transform lastTransform = new Transform();
    private boolean isDirty = false; // Dirty flag, tells renderer to redraw if object has changed

    private boolean isSticky = false;

    /**
     * Create a spriteRenderer using a sprite that is already loaded.
     * Default tint color is white (no tinting visible).
     * @param glyph
     */
    public GlyphRenderer(Transform transform, Glyph glyph, Text parentText, char c, boolean isSticky, Color color) {
        this.localTransform = transform;
        this.glyph = glyph;
        this.color = BLUE.toNormalizedVec4f();
        this.parentText = parentText;
        this.character = c;
        this.isSticky = isSticky;
        this.color = color.toNormalizedVec4f();
        this.isDirty = true;
    }

    /**
     * Update method called every frame by parent 
     * @param dt Engine.deltaTime
     */
    public void update(float dt) {
        if (!this.lastTransform.equals(this.localTransform)) {
            this.localTransform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    public void updatePosition (Vector2f delta) {
        localTransform.addX(delta.x());
        localTransform.addY(delta.y());
    }

    public char getCharacter () {
        return character;
    }

    /**
     * @return type Texture of the sprite if applicable.
     */
    public Texture getTexture() {
        return glyph.getTexture();
    }


    /**
     * @return Vector2f array of the UV coordinates of the sprite if applicable.
     */
    public Vector2f[] getTexCoords() {
        return glyph.getUV();
    }

    /**
     * @return a Vector4f containing the normalized (0-1) color values (R, G, B, and A)
     */
    public Vector4f getColorVector() {
        return color;
    }

    public Transform getLocalTransform () {
        return localTransform;
    }

    public void addX (float x) {
        localTransform.addX(x);
    }

    public void addY (float x) {
        localTransform.addY(x);
    }

    /**
     * @return type Color in standard RGBA form in the range 0-255
     */
    public Color getColor() {
        return new Color(color.x, color.y, color.z, color.w).fromNormalized();
    }

    /**
     * Change the color by passing a Vector4f
     * @param color vector, values should be in the range of 0-1
     */
    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.color = color;
            isDirty = true;
        }
    }

    /**
     * Change the color by passing a Color object, converting it to a normalized Vector4f.
     * @param color should be in range of 0-255
     */
    public void setColor(Color color) {
        if (!this.color.equals(color.toNormalizedVec4f())) {
            this.color = color.toNormalizedVec4f();
            isDirty = true;
        }
    }

    /**
     * Change the alpha/opacity of the sprite and/or color
     * @param a alpha/opacity
     */
    public void setAlpha (float a) {
        color.w = MathUtils.map(a, 0, 255, 0, 1);
    }

    /**
     * Used by the renderer to determine if this glyph should be sent back to the GPU to be redrawn.
     * @return true or false if the sprite or color has changes since last draw.
     */
    public boolean isDirty() {
        return isDirty;
    }

    /**
     * Used by the renderer to reset the state of the GlyphRenderer to clean.
     */
    public void setClean() {
        isDirty = false;
    }

    /**
     * Used by the renderer to reset the state of the GlyphRenderer to dirty.
     */
    public void setDirty() {
        isDirty = true;
    }

    public Text getParentText () {
        return parentText;
    }

    public boolean isSticky () {
        return isSticky;
    }
}
