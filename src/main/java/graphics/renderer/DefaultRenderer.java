package graphics.renderer;

import ecs.GameObject;
import ecs.SpriteRenderer;
import graphics.*;
import org.joml.Vector2f;
import util.Assets;
import util.Engine;
import util.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <h1>Azurite</h1>
 * Used to render sprites, which are rendered as {@code Primitive.QUAD}s
 * with textures. This should be used to render any renderable {@code gameObject}.
 */
public class DefaultRenderer extends Renderer {
    private static final int MAX_BATCH_SIZE = 1000;

    private final List<SpriteRenderer> sprites;

    public DefaultRenderer() {
        sprites = new ArrayList<>();
    }

    /**
     * Create a shader
     *
     * @return the created shader
     */
    @Override
    protected Shader createShader() {
        return Assets.getShader("src/assets/shaders/default.glsl");
    }

    /**
     * Create a framebuffer
     *
     * @return the created Framebuffer
     */
    @Override
    protected Framebuffer createFramebuffer() {
        return Framebuffer.createWithColorAttachment();
    }

    /**
     * Create a new Batch with appropriate parameters
     *
     * @param zIndex
     * @return a new batch
     */
    @Override
    protected RenderBatch createBatch(int zIndex) {
        return new RenderBatch(MAX_BATCH_SIZE, zIndex, Primitive.QUAD,
                ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT);
    }

    /**
     * Upload uniforms to the shader
     *
     * @param shader the shader
     */
    @Override
    protected void uploadUniforms(Shader shader) {
        shader.uploadIntArray("uTextures", textureSlots);

        // This is here so that all renderers can have different cameras OR no cameras at all
        shader.uploadMat4f("uProjection", Engine.window().currentScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Engine.window().currentScene().camera().getViewMatrix());
        shader.uploadInt("uLightmap", 8);
    }

    /**
     * Rebuffer all the data into batches
     */
    @Override
    protected void rebuffer() {
        for (SpriteRenderer sprite : sprites) {
            RenderBatch batch = getAvailableBatch(sprite.getTexture(), sprite.gameObject.zIndex());

            Vector2f pos = sprite.gameObject.getReadOnlyPosition();
            Vector2f scale = sprite.getSize();
            Vector2f[] textureCoordinates = sprite.getTexCoords();

            int textureID;
            if (sprite.getTexture() != null) textureID = batch.addTexture(sprite.getTexture());
            else textureID = 0;

            // Push verts to the batch
            float xAdd = 1.0f;
            float yAdd = 1.0f;
            for (int i = 0; i < 4; i++) {
                switch (i) {
                    case 1: yAdd = 0.0f; break;
                    case 2: xAdd = 0.0f; break;
                    case 3: yAdd = 1.0f; break;
                }

                float scaledX = (xAdd * scale.x);
                float scaledY = (yAdd * scale.y);

                batch.pushVec2(pos.x + scaledX, pos.y + scaledY);
                batch.pushColor(sprite.getColor());
                batch.pushVec2(textureCoordinates[i]);
                batch.pushInt(textureID);
            }
        }
    }

    /**
     * Add a gameObject to this renderer
     *
     * @param gameObject the GameObject with renderable components
     */
    @Override
    public void add(GameObject gameObject) {
        SpriteRenderer spr = gameObject.getComponent(SpriteRenderer.class);
        if (spr != null) {
            sprites.add(spr);
        }
    }

    /**
     * Remove a gameObject from this renderer
     *
     * @param gameObject the GameObject with renderable components
     */
    @Override
    public void remove(GameObject gameObject) {
        SpriteRenderer spr = gameObject.getComponent(SpriteRenderer.class);
        if (spr != null) {
            sprites.remove(spr);
        }
    }

    /**
     * Prepare for rendering. Do anything like setting background here.
     */
    @Override
    protected void prepare() {
        Graphics.background(Graphics.defaultBackground);
    }
}
