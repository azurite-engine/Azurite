package ecs.components;

import ecs.Component;
import graphics.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics.Transform;
import util.Scene;
import util.Utils;

public class Camera extends Component {

    private Matrix4f projectionMatrix, viewMatrix;


    public Camera () {
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();

        Window.currentScene.setCamera(this);
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
        Transform transform = this.gameObject.getTransform();
        viewMatrix.lookAt(new Vector3f(transform.position.x, transform.position.y, 20), cameraFront.add(transform.position.x, transform.position.y, 0), cameraUp);

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix () {
        return this.projectionMatrix;
    }

    public void smoothFollow (Transform centerOn) {
        Transform transform = this.gameObject.getTransform();

        float smoothing = 0.045f;
        Vector2f desiredPosition = new Vector2f(centerOn.getX() - Window.getWidth()/2, centerOn.getY() - Window.getHeight()/2);
        Vector2f smoothedPosition = new Vector2f(
                        Utils.lerp(this.gameObject.getTransform().position.x, desiredPosition.x, smoothing),
                        Utils.lerp(this.gameObject.getTransform().position.y, desiredPosition.y, smoothing));

        //TODO: This method is using a square root and is thus slow as hell if used at large scale. Consider updating this to use a squared distance instead.
        if (Utils.dist(desiredPosition, transform.position) < 10) {
            this.gameObject.setTransformX(desiredPosition.x);
            this.gameObject.setTransformY(desiredPosition.y);
        }

        this.gameObject.setTransformX(smoothedPosition.x);
        this.gameObject.setTransformY(smoothedPosition.y);
    }
}
