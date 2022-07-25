package graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import util.MathUtils;

/**
 * 
 * Represents a basic camera. The purpose of a digital camera capturing a
 * digital point of view
 * is to translate the 3D locations in game onto the 2 dimensions of your
 * monitor (if you come
 * from the future with holograms in it, Hi! welcome to the past!) The camera
 * does so by way of
 * linear algebra; points in 3D space can be projected onto a 2D plane using
 * matrix
 * transformations. <br>
 * <br>
 * Specifically, the digital camera used in this engine is composed of two
 * matrices: the
 * projection and the view matrices. The projection matrix is used to define an
 * in game
 * width and height to your perspective by defining how many in-game coordinate
 * units can be
 * seen horizontally and vertically on your monitor. The view matrix defines the
 * direction and
 * orientation of the camera; it's usually the matrix defined in terms of camera
 * position, pitch,
 * and yaw.
 */
public class Camera {

    public static Camera instance;

    /**
     * Caching these values here in case we have to pick up and drop objects into
     * the world with a mouse
     */
    float aspectWidth, aspectHeight;
    int viewportPosX, viewportPosY;

    /**
     * FFREE - Uses whatever resolution for aspect ratio
     * ASPECT_RATIO - It will show the entire image on the screen but will keep the
     * aspect ratio (might create black bars)
     */
    private enum Mode {
        FREE,
        ASPECT_RATIO,
    }

    public Mode mode = Mode.FREE;
    /**
     * World size otherwise known as pixel size
     */
    public Vector2f worldSize = new Vector2f(16 * 45, 16 * 45);

    /**
     * Position of the camera
     */
    public Vector2f position;
    /**
     * Projection Matrix of the camera
     */
    private Matrix4f projectionMatrix;
    private Matrix4f inverseProjectionM;

    /**
     * View Matrix of the camera
     */
    private Matrix4f viewMatrix;
    private Matrix4f inverseViewM;

    /**
     * Creates a new Camera with a certain position
     * Projection matrix is set to default: 0 to Window's width from left to right,
     * 0 to Window's height from top to bottom
     *
     * @param position initial position
     */
    public Camera(Vector2f position) {
        instance = this;
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjectionM = new Matrix4f();
        this.inverseViewM = new Matrix4f();
        adjustProjection();

    }

    /**
     * Creates a new Camera positioned at (0, 0)
     * Projection matrix is set to default: 0 to Window's width from left to right,
     * 0 to Window's height from top to bottom
     */
    public Camera() {
        instance = this;
        this.position = new Vector2f();
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjectionM = new Matrix4f();
        this.inverseViewM = new Matrix4f();
        adjustProjection();
    }

    /**
     * Recalculate Projection Matrix
     */
    public void adjustProjection() {
        projectionMatrix.identity();
        // This should be checked with physicalAspectRatio!! but its fine right now
        float displayAspectRatio = (float) Window.getWidth() / (float) Window.getHeight();
        float pixelAspectRatio = worldSize.x() / worldSize.y();

        if (mode == Mode.FREE) {
            aspectWidth = Window.getWidth();
            aspectHeight = Window.getHeight();

            GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
            projectionMatrix.ortho(0, Window.getWidth(), Window.getHeight(), 0, 0, 100f);
        } else if (mode == Mode.ASPECT_RATIO) {

            aspectWidth = Window.getWidth();
            aspectHeight = aspectWidth / pixelAspectRatio;

            if (aspectHeight > Window.getHeight()) {
                aspectHeight = Window.getHeight();
                aspectWidth = aspectHeight * pixelAspectRatio;
            }

            viewportPosX = (int) (((float) Window.getWidth() / 2.0f) - (aspectWidth / 2.0f));
            viewportPosY = (int) (((float) Window.getHeight() / 2.0f) - (aspectHeight / 2.0f));

            GL11.glViewport(viewportPosX, viewportPosY, (int) aspectWidth, (int) aspectHeight);
            projectionMatrix.ortho(0, worldSize.x, worldSize.y, 0, 0, 100f);
        }

        projectionMatrix.invert(inverseProjectionM);
    }

    /**
     * Recalculates and returns the view marix
     */
    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0, 0, -1);
        Vector3f cameraUp = new Vector3f(0, 1, 0);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20), cameraFront.add(position.x, position.y, 0),
                cameraUp);

        return this.viewMatrix;
    }

    /**
     * Get the camera's projection matrix
     */
    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    /**
     * Smoothly center the camera on to a transform
     */
    public void smoothFollow(Vector2f c) {

        float smoothing = 0.045f;
        Vector2f desiredPosition = new Vector2f(c.x - Window.getWidth() / 2f, c.y - Window.getHeight() / 2f);
        Vector2f smoothedPosition = new Vector2f(MathUtils.lerp(position.x, desiredPosition.x, smoothing),
                MathUtils.lerp(position.y, desiredPosition.y, smoothing));
        // If you notice black bars while the camera is panning, it is because floating
        // point positions can cause discrepencies, unfortunately casing the lerp to an
        // int makes the motion a little bit choppy
        if (MathUtils.dist(desiredPosition, position) < 10) {
            position = desiredPosition;
        }
        position = smoothedPosition;
    }

    /**
     * Get the camera's position
     */
    public Vector2f getPosition() {
        return this.position;
    }

    public Matrix4f getInverseProjection() {
        return inverseProjectionM;
    }

    public Matrix4f getInverseView() {
        return inverseViewM;
    }

    public Vector2f getWorldSize() {
        return worldSize;
    }

    public float getViewportSizeX() {
        return aspectWidth;
    }

    public float getViewportSizeY() {
        return aspectHeight;
    }

    public int getViewportPosX() {
        return viewportPosX;
    }

    public int getViewportPosY() {
        return viewportPosY;
    }
}
