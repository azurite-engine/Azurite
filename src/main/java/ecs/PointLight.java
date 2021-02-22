package ecs;

import graphics.Color;
import org.joml.Vector3f;
import physics.Transform;

/**
 * A Point Light Component.
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
	 * Transform of the parent GameObject to get the position
	 */
	public Transform lastTransform;

	/**
	 * Constructor which sets color of the light by default to white.
	 * @param intensity float: Intensity of the light
	 */
	public PointLight(float intensity) {
		this(Color.WHITE, intensity);
	}

	/**
	 * @param color Vector3f: Color of the light
	 * @param intensity float: Intensity of the light
	 */
	public PointLight(Color color, float intensity) {
		this.color = color.toNormalizedVec3f();
		this.intensity = intensity;
	}

	@Override
	public void start() {
		this.lastTransform = gameObject.getTransform().copy();
	}

	@Override
	public void update(float dt) {
		if (!this.lastTransform.equals(this.gameObject.getTransform())) {
			this.gameObject.getTransform().copy(this.lastTransform);
		}
	}
}
