package graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import util.MathUtils;

/**
 * 
 * Represents a basic camera. The purpose of a digital camera capturing a digital point of view
 * is to translate the 3D locations in game onto the 2 dimensions of your monitor (if you come
 * from the future with holograms in it, Hi! welcome to the past!) The camera does so by way of
 * linear algebra; points in 3D space can be projected onto a 2D plane using matrix
 * transformations. <br><br>
 * Specifically, the digital camera used in this engine is composed of two matrices: the
 * projection and the view matrices. The projection matrix is used to define an in game
 * width and height to your perspective by defining how many in-game coordinate units can be
 * seen horizontally and vertically on your monitor. The view matrix defines the direction and
 * orientation of the camera; it's usually the matrix defined in terms of camera position, pitch,
 * and yaw.
 */
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



    /**
     * Position of the camera
     */
    public Vector2f position;
    /**
     * Projection Matrix of the camera
     */
    private Matrix4f projectionMatrix;
    /**
     * View Matrix of the camera
     */
    private Matrix4f viewMatrix;

    /**
     * Creates a new Camera with a certain position
     * Projection matrix is set to default: 0 to Window's width from left to right, 0 to Window's height from top to bottom
     *
     * @param position initial position
     */
    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    /**
     * Creates a new Camera positioned at (0, 0)
     * Projection matrix is set to default: 0 to Window's width from left to right, 0 to Window's height from top to bottom
     */
    public Camera() {
        this.position = new Vector2f();
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    /**
     * Recalculate Projection Matrix
     */
    public void adjustProjection() {
        projectionMatrix.identity();
        //This should be checked with physicalAspectRatio!! but its fine right now
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

    /**
     * Recalculates and returns the view marix
     */
    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0, 0, -1);
        Vector3f cameraUp = new Vector3f(0, 1, 0);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20), cameraFront.add(position.x, position.y, 0), cameraUp);

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
        Vector2f smoothedPosition = new Vector2f(MathUtils.lerp(position.x, desiredPosition.x, smoothing), MathUtils.lerp(position.y, desiredPosition.y, smoothing));
        // If you notice black bars while the camera is panning, it is because floating point positions can cause discrepencies, unfortunately casing the lerp to an int makes the motion a little  bit choppy
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
}
