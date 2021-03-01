package ecs;

import org.joml.Vector4f;
import physics.Transform;

/**
 * Currently Unused. Will be used in shadow casting.
 */
public class RectBounds extends Component {
	private Vector4f[] edges;

	private Transform lastTransform;

	@Override
	public void start() {
		this.gameObject.getTransform().copy(this.lastTransform);

		edges = new Vector4f[4];
		resetEdges();
	}

	@Override
	public void update(float dt) {
		if (!this.lastTransform.equals(this.gameObject.getTransform())) {
			this.gameObject.getTransform().copy(this.lastTransform);
			resetEdges();
		}
	}

	private void resetEdges() {
		// TL to TR
		edges[0].set(lastTransform.position.x, lastTransform.position.y,
				lastTransform.position.x + lastTransform.scale.x, lastTransform.position.y);
		// TR TO BR
		edges[1].set(lastTransform.position.x + lastTransform.scale.x, lastTransform.position.y,
				lastTransform.position.x + lastTransform.scale.x, lastTransform.position.y + lastTransform.scale.y);
		// BR to BL
		edges[2].set(lastTransform.position.x + lastTransform.scale.x, lastTransform.position.y + lastTransform.scale.y,
				lastTransform.position.x, lastTransform.position.y);
		// BL to TL
		edges[3].set(lastTransform.position.x, lastTransform.position.y + lastTransform.scale.y,
				lastTransform.position.x, lastTransform.position.y + lastTransform.scale.y);
	}
}
