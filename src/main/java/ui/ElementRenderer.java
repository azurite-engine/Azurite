package ui;
 
import graphics.Color;
import graphics.Sprite;
import graphics.Texture;
import graphics.renderer.UIRenderBatch;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.Assets;
import util.Utils;

import static graphics.Color.WHITE;

/**
 * UIComponentRenderer is the renderable part of a UI component
 * it can contain a solid color, a semi-transparent color, or a texture.
 * Sprites can be tinted by the color value.
 *
 * @author Asher Haun
 * @version 11.9.2021
 * @since 11.9.2021
 */

public class ElementRenderer extends Element {

    /**
     * The batch in which this component has been added
     */
    private UIRenderBatch batch;
    /**
     * The index at which this component is placed in the batch
     */
    private int index;

    private Vector4f color = new Color(255, 100, 100, 255).toNormalizedVec4f();

    private Sprite sprite;

//    private Vector2f size;
    private boolean isDirty; // Dirty flag, tells renderer to redraw if object components have changed

    /**
     * Create the spriteRenderer using a color vector, no sprite.
     *
     * @param color of type JOML Vector4f, range from 0-1
     */
    public ElementRenderer(Vector4f color, Vector2f size) {
        this.setColor(color);
        this.sprite = new Sprite(null);
        this.isDirty = true;
//        this.size = size;
    }

    /**
     * Create the spriteRenderer using a Color object, no sprite.
     *
     * @param color of type Color, range from 0-255
     */
    public ElementRenderer(Color color, Vector2f size) {
        // Note that type Color is normalized below in setColor()
        this.setColor(color.toNormalizedVec4f());
        this.sprite = new Sprite(null);
        this.isDirty = true;
//        this.size = size;
    }

    /**
     * Create a spriteRenderer using a sprite that is already loaded.
     * Default tint color is white (no tinting visible).
     *
     * @param sprite
     */
    public ElementRenderer(Sprite sprite, Vector2f size) {
        this.sprite = sprite;
        this.color = WHITE.toNormalizedVec4f();
        this.isDirty = true;
//        this.size = size;
    }

    /**
     * Create a spriteRenderer using an image from the fileSystem.
     *
     * @param path to the image (ie. "src/assets/images/pepper.png")
     */
    public ElementRenderer(String path, Vector2f size) {
        this.sprite = new Sprite(Assets.getTexture(path));
        this.color = WHITE.toNormalizedVec4f();
        this.isDirty = true;
//        this.size = size;
    }

    /**
     * Initialize the Component, called once after creation.
     */
    public void start() {

    }


    /**
     * Update method called every frame by parent
     *
     * @param dt Engine.deltaTime
     */
    public void update(float dt) {

    }

    public void remove() {
        getBatch().getRenderer().remove(this);
    }

    /**
     * @return type Texture of the sprite if applicable.
     */
    public Texture getTexture() {
        return sprite.getTexture();
    }

//    public Vector2f getSize() {
//        return size;
//    }

    public void setSize(Vector2f size) {
//        this.size = size;
    }

    /**
     * Set the texture of the Sprite if required.
     *
     * @param texture the new texture of this sprite
     */
    public void setTexture(Texture texture) {
        if (sprite.getTexture() != texture) {
            sprite.setTexture(texture);
            isDirty = true;
        }
    }

    /**
     * @return Vector2f array of the UV coordinates of the sprite if applicable.
     */
    public Vector2f[] getTexCoords() {
        return sprite.getTextureCoordinates();
    }

    /**
     * @return a Vector4f containing the normalized (0-1) color values (R, G, B, and A)
     */
    public Vector4f getColorVector() {
        return color;
    }

    /**
     * @return type Color in standard RGBA form in the range 0-255
     */
    public Color getColor() {
        return new Color(color.x, color.y, color.z, color.w).fromNormalized();
    }

    /**
     * Change the color by passing a Vector4f
     *
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
     *
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
     *
     * @param a alpha/opacity
     */
    public void setAlpha(float a) {
        color.w = Utils.map(a, 0, 255, 0, 1);
    }

    /**
     * Change the sprite contained in the SpriteRenderer Component.
     *
     * @param sprite
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        isDirty = true;
    }

    /**
     * Used by the renderer to determine if this sprite should be sent back to the GPU to be redrawn.
     *
     * @return true or false if the sprite or color has changes since last draw.
     */
    public boolean isDirty() {
        return isDirty;
    }

    /**
     * Mark this Sprite renderer as dirty
     */
    public void markDirty() {
        isDirty = true;
    }

    /**
     * Used by the renderer to reset the state of the SpriteRenderer to clean.
     */
    public void setClean() {
        isDirty = false;
    }

    /**
     * Sets this component's batch and index to where it has currently been added
     *
     * @param batch the batch that this component has been added to
     * @param index the index at which this component is placed in the batch
     */
    public void setLocation(UIRenderBatch batch, int index) {
        this.batch = batch;
        this.index = index;
    }

    /**
     * Get the batch in which this component has been added
     *
     * @return The batch in which this component has been added
     */
    public UIRenderBatch getBatch() {
        return batch;
    }

    /**
     * Get the index at which this component is placed in the batch
     *
     * @return The index at which this component is placed in the batch
     */
    public int getIndex() {
        return index;
    }
}
