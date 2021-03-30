package ecs;

import event.EventData;
import event.EventListener;
import event.EventNode;
import graphics.Color;
import physics.AABB;
import physics.Transform;
import util.debug.DebugPrimitive;
import util.debug.DebugRect;

public class CollisionTrigger extends Component {
	private Transform self, other;
	private Transform lastPosition;
	private boolean wasColliding;

	private DebugRect debugRect;

	public EventNode<EventData.TriggerEnterEvent> onTriggerEnter;
	public EventNode<EventData.TriggerExitEvent> onTriggerExit;

	public CollisionTrigger(EventListener<EventData.TriggerEnterEvent> onEnter) {
		onTriggerEnter = new EventNode<>();
		onTriggerExit = new EventNode<>();
		onTriggerEnter.subscribe(onEnter);
	}

	@Override
	public void start() {
		self = gameObject.getTransform(); // Parent game object's transform
		lastPosition = self;
		debugRect = new DebugRect(self.getX(), self.getY(), self.getWidth(), self.getHeight(), Color.GREEN);
	}

	@Override
	public void update(float dt) {
		collide();
		debugRect.reset(self.getX(), self.getY(), self.getWidth(), self.getHeight());
	}

	@Override
	public DebugPrimitive[] debug() {
		return new DebugPrimitive[]{debugRect};
	}

	public boolean checkCollision() {
		for (AABB a : AABB.colliders) {
			other = a.gameObject.getTransform();
			if (isColliding(other)) {
				return true;
			}
		}
		return false;
	}

	public void collide() {
		if (checkCollision()) {
			if (!wasColliding) {
				wasColliding = true;
				onTriggerEnter.onEvent(new EventData.TriggerEnterEvent());
			}
		} else {
			if (wasColliding) {
				wasColliding = false;
				onTriggerExit.onEvent(new EventData.TriggerExitEvent());
			}
		}
	}

	public boolean isColliding(Transform other) {
		float x1 = self.getX();
		float y1 = self.getY();
		float w1 = self.getWidth();
		float h1 = self.getHeight();

		float x2 = other.getX();
		float y2 = other.getY();
		float w2 = other.getWidth();
		float h2 = other.getHeight();

		return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
	}
}
