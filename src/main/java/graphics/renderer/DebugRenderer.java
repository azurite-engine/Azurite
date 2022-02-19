package graphics.renderer;

import ecs.Component;
import ecs.GameObject;
import graphics.Framebuffer;
import graphics.Primitive;
import graphics.Shader;
import graphics.ShaderDatatype;
import util.Assets;
import util.Engine;
import util.debug.DebugLine;
import util.debug.DebugPrimitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL11.glLineWidth;

/**
 * 
 * Used to render debug materials, like collision hitboxes, raycasts, or test
 * data in general. Only supports rendering lines.
 */
public class DebugRenderer extends Renderer {
    private final List<DebugLine> lines;

    public DebugRenderer() {
        this.lines = new ArrayList<>();
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
     * @return the created fbo
     */
    @Override
    protected Framebuffer createFramebuffer() {
        return Framebuffer.createDefault();
    }

    /**
     * Create a new Batch with appropriate parameters
     *
     * @return a new batch
     */
    @Override
    protected RenderBatch createBatch(int zIndex) {
        return new RenderBatch(100, zIndex, Primitive.LINE, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4);
    }

    /**
     * Upload the required uniforms
     *
     * @param shader the shader
     */
    @Override
    protected void uploadUniforms(Shader shader) {
        shader.uploadMat4f("uProjection", Engine.window().currentScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Engine.window().currentScene().camera().getViewMatrix());
    }

    /**
     * Rebuffer all the data into batches
     */
    @Override
    protected void rebuffer() {
        for (DebugLine line : lines) {
            RenderBatch batch = getAvailableBatch(null, 0);

            batch.pushVec2(line.start);
            batch.pushColor(line.color);

            batch.pushVec2(line.end);
            batch.pushColor(line.color);
        }
    }

    /**
     * Prepare for rendering. Do anything like setting background here.
     */
    @Override
    protected void prepare() {
        glLineWidth(3);
    }

    /**
     * Add a gameObject to this renderer
     *
     * @param gameObject the gameObject
     */
    @Override
    public void add(GameObject gameObject) {
        for (Component c : gameObject.getComponents()) {
            DebugPrimitive[] primitives = c.debug();
            if (primitives != null) {
                for (DebugPrimitive primitive : primitives) {
                    Collections.addAll(lines, primitive.getLines());
                }
            }
        }
    }

    /**
     * Remove a gameObject from this renderer
     *
     * @param gameObject the gameObject
     */
    @Override
    public void remove(GameObject gameObject) {
        for (Component c : gameObject.getComponents()) {
            DebugPrimitive[] primitives = c.debug();
            if (primitives != null)
                for (DebugPrimitive primitive : primitives) {
                    for (DebugLine line : primitive.getLines())
                        lines.remove(line);
                }
        }
    }
}
