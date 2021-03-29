package physics;

import ecs.CharacterController;
import ecs.Component;
import graphics.Color;
import util.debug.DebugPrimitive;
import util.debug.DebugRect;

import java.util.ArrayList;
import java.util.List;

public class AABB extends Component {

	private Transform self, other;
	private Transform lastPosition;

	public static final List<AABB> colliders = new ArrayList<>();

	CharacterController controller;

	private DebugRect debugRect;

	@Override
	public void start() {
		colliders.add(this);
		self = gameObject.getTransform(); // Parent game object's transform
		lastPosition = self;
		checkControllerAABB();
		debugRect = new DebugRect(self.getX(), self.getY(), self.getWidth(), self.getHeight(), Color.GREEN);
	}

	@Override
	public void update(float dt) {
		checkControllerAABB();
		debugRect.reset(self.getX(), self.getY(), self.getWidth(), self.getHeight());
	}

	@Override
	public DebugPrimitive[] debug() {
		return new DebugPrimitive[]{debugRect};
	}

	public boolean checkCollision() {
		for (AABB a : colliders) {
			other = a.gameObject.getTransform();
			if (a.equals(this)) continue;
			if (collide(other)) {
				return true;
			}
		}
		return false;
	}

	public void collideX() {
		if (checkCollision()) {
			if (lastPosition.getX() < other.getX()) {
				gameObject.setTransformX(other.getX() - gameObject.getTransform().getWidth());
			} else {
				gameObject.setTransformX(other.getX() + other.getWidth());
			}
		}
	}

	public void collideY() {
		if (checkCollision()) {
			if (lastPosition.getY() < other.getY()) {
				gameObject.setTransformY(other.getY() - gameObject.getTransform().getHeight());
			} else {
				gameObject.setTransformY(other.getY() + other.getHeight());
			}
		}
	}

	public boolean collide(Transform other) {
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

	private void checkControllerAABB() {
		controller = gameObject.getComponent(CharacterController.class);
		if (controller != null) {
			if (!controller.AABB_enabled) {
				controller.enableAABB();
			}
		}
	}
}
