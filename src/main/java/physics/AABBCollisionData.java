package physics;

import event.EventData;
import org.joml.Vector2f;

public class AABBCollisionData extends EventData {
	public final Vector2f resolution;

	public AABBCollisionData(Vector2f resolution) {
		this.resolution = resolution;
	}
}
