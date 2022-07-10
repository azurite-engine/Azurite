package graphics.renderer;

import ecs.GameObject;
import ecs.PointLight;
import graphics.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.Assets;
import util.Engine;

import java.util.ArrayList;
import java.util.List;

public class LightmapRenderer extends Renderer {
    // The light data
    private final List<PointLight> lights;
    private RenderBatch batch;

    public LightmapRenderer() {
        lights = new ArrayList<>();
        noRebuffer = true;
    }

    @Override
    public void init() {
        super.init();

        batch = new RenderBatch(1, 0, Primitive.QUAD, ShaderDatatype.FLOAT2);
        batch.init();
        batch.start();
        batch.pushVec2(0, Window.getHeight());
        batch.pushVec2(Window.getWidth(), Window.getHeight());
        batch.pushVec2(Window.getWidth(), 0);
        batch.pushVec2(0, 0);
        batch.finish();
        batches.add(batch);
        // TODO: Add resize method to re-upload projection matrix @asher or @voxelrifts
    }

    /**
     * Create a shader
     *
     * @return the created shader
     */
    @Override
    protected Shader createShader() {
        return Assets.getShader("src/assets/shaders/lightmap.glsl");
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
        return null;
    }

    /**
     * Upload uniforms to the shader
     *
     * @param shader the shader
     */
    @Override
    protected void uploadUniforms(Shader shader) {
        // This is here so that all renderers can have different cameras OR no cameras at all
        shader.uploadMat4f("uProjection", Engine.window().currentScene().camera().getProjectionMatrix());
        shader.uploadVec2f("uCameraOffset", Engine.window().currentScene().camera().getPosition());

        // Set lighting uniforms
        Vector2f[] lightPositions = new Vector2f[lights.size()];
        Vector3f[] lightColors = new Vector3f[lights.size()];
        float[] lightIntensities = new float[lights.size()];

        for (int i = 0; i < lights.size(); i++) {
            PointLight light = lights.get(i);
            lightPositions[i] = new Vector2f(light.gameObject.getPositionData()[0], light.gameObject.getPositionData()[1]);
            lightColors[i] = light.color;
            lightIntensities[i] = light.intensity;
        }

        shader.uploadVec2fArray("uLightPosition", lightPositions);
        shader.uploadVec3fArray("uLightColor", lightColors);
        shader.uploadFloatArray("uIntensity", lightIntensities);
        shader.uploadFloat("uMinLighting", Engine.scenes().getMinSceneLight());
        shader.uploadInt("uNumLights", lights.size());
    }

    /**
     * Rebuffer all the data into batches
     */
    @Override
    protected void rebuffer() {
        // Will never be called since noRebuffer flag is set

    }



    /**
     * Add a gameObject to this renderer
     *
     * @param gameObject the GameObject with renderable components
     */
    @Override
    public void add(GameObject gameObject) {
        PointLight l = gameObject.getComponent(PointLight.class);
        if (l != null) {
            if (lights.contains(l)) return;
            lights.add(l);
            assert lights.size() <= 10 : "NO MORE THAN 10 LIGHTS";
        }
    }

    /**
     * Remove a gameObject from this renderer
     *
     * @param gameObject the GameObject with renderable components
     */
    @Override
    public void remove(GameObject gameObject) {
        PointLight l = gameObject.getComponent(PointLight.class);
        if (l != null) {
            lights.remove(l);
        }
    }

    /**
     * Prepare for rendering. Do anything like setting background here.
     */
    @Override
    protected void prepare() {
        Graphics.background(Color.WHITE);
    }

    public void bindLightmap() {
        framebuffer.getColorAttachment(0).bindToSlot(8);
    }
}
