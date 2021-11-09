package components;
 
import graphics.Color;
import graphics.RenderableComponent;
import graphics.renderer.QuadRenderBatch;
import org.joml.Vector3f;
import physics.LocationSensitive;

/**
 * <h1>Azurite</h1>
 * A Point Light Component is essentially a location in the world that emits light in
 * all directions. One can also specify both it's color and it's intensity.
 *
 * @author VoxelRifts
 */
public class PointLight extends RenderableComponent<QuadRenderBatch> implements LocationSensitive {
    /**
     * Colour of the light
     */
    public Vector3f color;

    /**
     * Intensity of the light
     * It controls how far the light's attenuation will reach
     */
    public float intensity;

    /**
     * Transform of the parent GameObject to get the position
     */
    public Vector3f lastLocation;

    /**
     * Constructor which sets color of the light by default to white.
     *
     * @param intensity float: Intensity of the light
     */
    public PointLight(float intensity) {
        this(Color.WHITE, intensity);
    }

    /**
     * @param color     Vector3f: Color of the light
     * @param intensity float: Intensity of the light
     */
    public PointLight(Color color, float intensity) {
        this.color = color.toNormalizedVec3f();
        this.intensity = intensity;
        this.order = SpriteRenderer.ORDER + 1;
    }

    @Override
    public void start() {
        this.lastLocation = gameObject.getReadOnlyLocation();
    }

    @Override
    public void remove() {
        getBatch().getRenderer().remove(this.gameObject);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void update(Vector3f changedLocationData) {
        this.lastLocation = changedLocationData;
    }

    @Override
    public boolean transformingObject() {
        return false;
    }
}
