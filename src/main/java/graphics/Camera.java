package graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import physics.Transform;
import util.Utils;

public class Camera {


	/**
	 * FILL - Tries to fill the entire window with image
	 * EXTENDED - If the image is of larger resolution it will extend it beyond window boundaries
	 * ASPECT_RATIO - It will show the entire image on the screen but will keep the aspect ratio (might create black bars)
	 */
	private enum Mode{
		FILL,
		EXTENDED,
		ASPECT_RATIO,
	}
	public Mode mode = Mode.FILL;
	/**
	 * Custom game resolution separate from device(window) resolution
	 */
	public Vector2f resolution = new Vector2f(2000, 1080);
	public Vector2f position;
	/**
	 * Resolution separate from devies resolution (Window) to determine should the image be stretched or filled or else according to Mode set
	 */


	private Matrix4f projectionMatrix, viewMatrix;
	
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


	/**
	 * THIS SHOULD BE CALLED WHEN WINDOW RESIZE IS CALLED!!
	 * Added three options:
	 * EXTENDED, FILL AND ASPECT
	 */
	public void adjustProjection () {
		projectionMatrix.identity();

		float windowAspectRatio = (float) Window.getWidth() / Window.getHeight();
		float physicalAspectRatio = resolution.x() / resolution.y();


		if(mode == Mode.FILL) {

			projectionMatrix.ortho(0, Window.getWidth(), Window.getHeight(), 0, 0, 100);

		}else if(mode == Mode.EXTENDED){

			GL11.glViewport(0, 0, (int)resolution.x, (int)resolution.y);
			projectionMatrix.ortho(0, Window.getWidth(), Window.getHeight(), 0, 0, 100);

		}else if(mode == Mode.ASPECT_RATIO){

			float aspectWidth = Window.getWidth();
			float aspectHeight = aspectWidth / physicalAspectRatio;

			if(aspectHeight > Window.getHeight()){
				aspectHeight = Window.getHeight();
				aspectWidth = aspectHeight * physicalAspectRatio;
			}

			int vpX = (int)(((float)Window.getWidth() / 2.0f) - (aspectWidth / 2.0f));
			int vpY = (int)(((float)Window.getHeight() / 2.0f) - (aspectHeight / 2.0f));

			GL11.glViewport(vpX, vpY, (int)aspectWidth, (int)aspectHeight);
			projectionMatrix.ortho(0, Window.getWidth(), Window.getHeight(), 0, 0, 100);

		}



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
		if (Utils.dist(desiredPosition, position) < 10) {
			position = desiredPosition;
		}
		position = smoothedPosition;
	}

	public Vector2f getPosition() {
		return this.position;
	}
}
