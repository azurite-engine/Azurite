package physics;

import ecs.Component;
import event.EventNode;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class AABB extends Component {
	public final Vector2f offset;
	public final float halfWidth;
	public final float halfHeight;

	private Transform lastTransform;

	private EventNode<AABBCollisionData> collisionEvent;

	private static final List<AABB> colliders;
	static int i = 0;

	static {
		colliders = new ArrayList<>();
	}

	public AABB(Vector2f centre, float width, float height, boolean shouldResolve) {
		this.offset = centre;
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
		collisionEvent = new EventNode<>();
		lastTransform = new Transform();

		if (shouldResolve) {
			collisionEvent.subscribe(data -> {
				gameObject.getTransform().position.add(data.resolution);
			});
		}
	}

	@Override
	public void start() {
		colliders.add(this);
		gameObject.getTransform().copy(lastTransform);
	}

	@Override
	public void update(float dt) {
		if (!gameObject.getTransform().equals(lastTransform)) {
			gameObject.getTransform().copy(lastTransform);

			offset.set(gameObject.getTransform().getPosition());
		}
	}

	private float up() {
		return offset.y - halfHeight;
	}
	private float down() {
		return offset.y + halfHeight;
	}
	private float left() {
		return offset.x - halfWidth;
	}
	private float right() {
		return offset.x + halfWidth;
	}

	public static void checkCollisions() {
		for (AABB a : colliders) {
			for (AABB b : colliders) {
				if (a.equals(b)) continue;
				if (a.left() < b.right() && a.right() > b.left()
						&& a.up() < b.down() && a.down() > b.up()) {

					Vector2f resolution = new Vector2f();
					a.collisionEvent.onEvent(new AABBCollisionData(resolution));
				}
			}
		}
	}
}
