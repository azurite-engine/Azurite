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
	/** Transform for this component's gameobject */
	private Transform self;
	/** Transform for the gameobject against which collision is being tested */
	private Transform other;

	/** Was the trigger active last frame */
	private boolean wasColliding;

	/** A Debug Rect Primitive to be rendered in Debug Mode */
	private DebugRect debugRect;

	/** This Event will fire when any AABB intersects with this trigger on the frame that it intersects */
	public EventNode<EventData.TriggerEnterEvent> onTriggerEnter;
	/** This Event will fire when any AABB stops intersecting with this trigger on the frame that it exits */
	public EventNode<EventData.TriggerExitEvent> onTriggerExit;

	/**
	 * Construct a Trigger Object with a Listener that will be called when trigger is entered
	 * Add more Listeners by accessing onTriggerEnter or onTriggerExit
	 * @param onEnter
	 */
	public CollisionTrigger(EventListener<EventData.TriggerEnterEvent> onEnter) {
		onTriggerEnter = new EventNode<>();
		onTriggerExit = new EventNode<>();
		onTriggerEnter.subscribe(onEnter);
	}

	@Override
	public void start() {
		self = gameObject.getTransform(); // Parent game object's transform
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

	/** Check if this trigger is intersecting with any other AABB */
	public boolean checkCollision() {
		for (AABB a : AABB.colliders) {
			other = a.gameObject.getTransform();
			if (isColliding(other)) {
				return true;
			}
		}
		return false;
	}

	/** Will be called every frame to fire required events if applicable */
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

	/** Is this Trigger colliding with another AABB */
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
