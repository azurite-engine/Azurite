package ecs;

import graphics.Color;
import org.joml.Vector3f;

/**
 * A Point Light Component is essentially a location in the world that emits light in
 * all directions. One can also specify both it's color and it's intensity.
 *
 * @author VoxelRifts
 */
public class PointLight extends Component {

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
        super(ComponentOrder.DRAW);
        this.color = color.toNormalizedVec3f();
        this.intensity = intensity;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {

    }

}
