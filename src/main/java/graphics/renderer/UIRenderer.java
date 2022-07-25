package graphics.renderer;

import graphics.Framebuffer;
import graphics.Primitive;
import graphics.Shader;
import graphics.ShaderDatatype;
import org.joml.Vector2f;
import ui.RenderableElement;
import util.Assets;
import util.Engine;
import util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to render sprites, which are rendered as {@code Primitive.QUAD}s
 * with textures. This should be used to render any renderable {@code gameObject}.
 *
 *
 */
public class UIRenderer extends Renderer {
    private static final int MAX_BATCH_SIZE = 1000;

    private final List<RenderableElement> renderableElements;

    public UIRenderer () {
        renderableElements = new ArrayList<>();
    }

    /**
     * Create a shader
     *
     * @return the created shader
     */
    @Override
    protected Shader createShader() {
        return Assets.getShader("src/assets/shaders/ui.glsl");
    }

    /**
     * Create a framebuffer
     *
     * @return the created Framebuffer
     */
    @Override
    protected Framebuffer createFramebuffer() {
        return Framebuffer.createDefault();
    }

    @Override
    protected RenderBatch createBatch(int zIndex) {
        return new RenderBatch(MAX_BATCH_SIZE, zIndex, Primitive.QUAD, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT);
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
    }

    @Override
    protected void rebuffer() {
        /**
         * NOTE TO SELF (Asher), THIS PROBABLY WON"T WORK, It only looks correct.
         *
         */
        for (RenderableElement re : renderableElements) {
            RenderBatch batch = getAvailableBatch(re.getTexture(), re.zIndex());

            Vector2f pos = re.getRenderFrame().getPosition();
            Vector2f scale = re.getRenderFrame().getScale();

            if (re.getParent() != null) {
                Log.info("PARENT");
                pos.add(re.getParent().getX(), re.getParent().getY());
            }

            Vector2f[] textureCoordinates = re.getTexCoords();

            int textureID;
            if (re.getTexture() != null) textureID = batch.addTexture(re.getTexture());
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
                batch.pushColor(re.getColor());
                batch.pushVec2(textureCoordinates[i]);
                batch.pushInt(textureID);
            }
        }
    }

    /**
     *
     * @param e UI ElementRenderer
     */
    public void add(RenderableElement e) {
        if (e == null) return;
        addElementRenderer(e);
    }

    /**
     * Prepare for rendering. Do anything like setting background here.
     */
    @Override
    protected void prepare() {}

    /**
     * Adds the ElementRenderer to a single batch, and creates a new batch if their is no space.
     *
     * @param elementRenderer elementRenderer: The ElementRenderer to be added to a batch
     */
    protected void addElementRenderer(RenderableElement elementRenderer) {
        renderableElements.add(elementRenderer);
    }

    /**
     * Remove an ElementRenderer from this renderer
     *
     * @param r ElementRenderer
     */
    public void remove(RenderableElement r) {
        if (r != null) {
            renderableElements.remove(r);
        }
    }
}
