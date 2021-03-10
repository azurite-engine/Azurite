package physics;

import ecs.CharacterController;
import ecs.Component;
import org.joml.Vector2f;
import util.Line;

import java.util.ArrayList;
import java.util.List;

public class AABB extends Component {

	private Transform self, other;
	private Transform lastPosition;

	private static final List<AABB> colliders = new ArrayList<>();;

	CharacterController controller;

	private Line[] lines;

	@Override
	public void start() {
		lines = new Line[4];
		colliders.add(this);
		self = gameObject.getTransform(); // Parent game object's transform
		lastPosition = self;
		checkControllerAABB();
		lines[0] = new Line(new Vector2f(self.getPosition()), new Vector2f(self.getX() + self.getWidth(), self.getY()));
		lines[1] = new Line(new Vector2f(self.getX() + self.getWidth(), self.getY()), new Vector2f(self.getX() + self.getWidth(), self.getY() + self.getHeight()));
		lines[2] = new Line(new Vector2f(self.getX() + self.getWidth(), self.getY() + self.getHeight()), new Vector2f(self.getX(), self.getY() + self.getHeight()));
		lines[3] = new Line(new Vector2f(self.getX(), self.getY() + self.getHeight()), new Vector2f(self.getPosition()));
	}

	@Override
	public void update(float dt) {
		checkControllerAABB();
		updateLines();
	}

	private void updateLines() {
		lines[0].start.set(self.getPosition());
		lines[0].end.set(self.getX() + self.getWidth(), self.getY());
		lines[0].markDirty();

		lines[1].start.set(self.getX() + self.getWidth(), self.getY());
		lines[1].end.set(self.getX() + self.getWidth(), self.getY() + self.getHeight());
		lines[1].markDirty();

		lines[2].start.set(self.getX() + self.getWidth(), self.getY() + self.getHeight());
		lines[2].end.set(self.getX(), self.getY() + self.getHeight());
		lines[2].markDirty();

		lines[3].start.set(self.getX(), self.getY() + self.getHeight());
		lines[3].end.set(self.getPosition());
		lines[3].markDirty();
	}

	@Override
	public Line[] debugLines() {
		return lines;
	}

	public boolean checkCollision () {
		for (AABB a : colliders) {
			other = a.gameObject.getTransform();
			if (a.equals(this)) continue;
			if (collide(other)) {
				return true;
			}
		}
		return false;
	}

	public void collideX () {
		if (checkCollision()) {
			if (lastPosition.getX() < other.getX()) {
				gameObject.setTransformX(other.getX() - gameObject.getTransform().getWidth());
			} else {
				gameObject.setTransformX(other.getX() + other.getWidth());
			}
		}
	}

	public void collideY () {
		if (checkCollision()) {
			if (checkCollision()) {
				if (lastPosition.getY() < other.getY()) {
					gameObject.setTransformY(other.getY() - gameObject.getTransform().getHeight());
				} else {
					gameObject.setTransformY(other.getY() + other.getHeight());
				}
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

	private void checkControllerAABB () {
		controller = gameObject.getComponent(CharacterController.class);
		if (controller != null) {
			if (!controller.AABB_enabled) {
				controller.enableAABB();
			}
		}
	}
}
