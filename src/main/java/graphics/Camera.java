package graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics.Transform;
import util.Utils;

public class Camera {

	private Matrix4f projectionMatrix, viewMatrix;
	public Vector2f position;
	
	public Camera (Vector2f position) {
		this.position = position;
		this.projectionMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		adjustProjection();
	}
	
	public Camera () {
		this.position = new Vector2f();
		this.projectionMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		adjustProjection();
	}
	
	public void adjustProjection () {
		projectionMatrix.identity();
		// Top Left origin
		projectionMatrix.ortho(0, Window.getWidth(), Window.getHeight(), 0, 0, 100);
	}
	
	public Matrix4f getViewMatrix () {
		Vector3f cameraFront = new Vector3f(0, 0, -1);
		Vector3f cameraUp = new Vector3f(0, 1, 0);
		this.viewMatrix.identity();
		viewMatrix.lookAt(new Vector3f(position.x, position.y, 20), cameraFront.add(position.x, position.y, 0), cameraUp);
		
		return this.viewMatrix;
	}

	public Matrix4f getProjectionMatrix () {
		return this.projectionMatrix;		
	}

	public void smoothFollow (Transform centerOn) {
		Transform c = centerOn;

		float smoothing = 0.045f;
		Vector2f desiredPosition = new Vector2f(c.getX() - Window.getWidth()/2,c.getY() - Window.getHeight()/2);
		Vector2f smoothedPosition = new Vector2f(Utils.lerp(position.x, desiredPosition.x, smoothing), Utils.lerp(position.y, desiredPosition.y, smoothing));
//		if (!(desiredPosition.x - position.x < 13))
//			smoothedPosition.x = Utils.round(smoothedPosition.x);
//
//		if (!(desiredPosition.y - position.y < 13))
//			smoothedPosition.y = Utils.round(smoothedPosition.y);

		// Lines in beteen sprites on tilemaps are caused by non pixel perfect (floating point) camera positions, unfortunately rounding the position can cause slight jitteryness.

		if (Utils.dist(desiredPosition, position) < 20)
			position = desiredPosition;
		position = smoothedPosition;
	}

	public Vector2f getPosition() {
		return this.position;
	}
}
