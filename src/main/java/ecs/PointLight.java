package ecs;

import org.joml.Vector3f;
import physics.Transform;

public class PointLight extends Component {
	public Vector3f color;
	public float intensity;

	public Transform lastTransform;

	public PointLight(float intensity) {
		this(new Vector3f(1.0f), intensity);
	}

	public PointLight(Vector3f color, float intensity) {
		this.color = color;
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
