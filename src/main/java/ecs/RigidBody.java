package ecs;

import org.joml.Vector2f;
import physics.CombinedForce;
import physics.Force;
import physics.collision.ConvexGJKSM;
import physics.collision.GJKSMShape;

/**
 * <h1>Azurite</h1>
 * <p>
 * Represents a Rigidbody with a fixed shape but changeable position.
 * Primarily used to apply physics and check collisions.
 *
 * @author Juyas
 * @version 19.06.2021
 * @since 19.06.2021
 */
public class RigidBody extends Component {

    public static final int TOTAL_LAYERS = Short.SIZE - 1;

    public static final short FIRST_LAYER = 0b100000000000000;

    //represents a series of 0s and 1s -
    //0 means not present, 1 means present
    private short collisionLayer;
    //represents a series of 0s and 1s
    //0 means no collision on that layer, 1 means collision
    private short collisionMask;

    //the collisionShape of the collider
    private final GJKSMShape collisionShape;

    //the force/forces applied to this body
    private final CombinedForce bodyForce;

    public RigidBody(GJKSMShape collisionShape, int[] layers, int[] maskedLayers) {
        this.collisionShape = collisionShape;
        this.collisionLayer = layerBitmask(layers);
        this.collisionMask = layerBitmask(maskedLayers);
        this.bodyForce = new CombinedForce();
    }

    public RigidBody(GJKSMShape collisionShape, int layer) {
        this.collisionShape = collisionShape;
        this.collisionLayer = intToLayerBits(layer);
        this.bodyForce = new CombinedForce();
    }

    public GJKSMShape getCollisionShape() {
        return collisionShape;
    }

    //helper method to translate a given layer into bit format
    private short intToLayerBits(int layer) {
        return (short) (FIRST_LAYER >> layer);
    }

    //helper method to translate given layers into bit format
    private short layerBitmask(int[] layers) {
        short allLayers = 0;
        for (int layer : layers) {
            allLayers |= FIRST_LAYER >> layer;
        }
        return allLayers;
    }

    /**
     * Determines whether a rigidbody intersects with another rigidbody IGNORING the collision layers.
     *
     * @param other the other rigidbody
     * @return true if and only if the two objects do collide
     */
    public boolean doesCollideWith(RigidBody other) {
        return ConvexGJKSM.gjksmCollision(this.collisionShape, other.collisionShape);
    }

    /**
     * Determines whether a rigidbody could potentially intersect with another rigidbody ONLY by their collision layers.
     *
     * @param other the other rigidbody
     * @return true if and only if both objects could potentially collide
     */
    public boolean canCollideWith(RigidBody other) {
        return (this.collisionLayer & other.collisionMask) != 0 || (this.collisionMask & other.collisionLayer) != 0;
    }

    /**
     * Determines whether a collision mask for a specified layer is enabled.
     *
     * @param layer the layer from 0 to TOTAL_LAYER-1, the behaviour for layers outside the range is undefined
     * @return true if and only if the mask for the specified layer is enabled
     */
    public boolean hasMask(int layer) {
        return (intToLayerBits(layer) & collisionMask) != 0;
    }

    /**
     * Determines whether the object exists in a specified layer.
     *
     * @param layer the layer from 0 to TOTAL_LAYER-1, the behaviour for layers outside the range is undefined
     * @return true if and only if the object exists in the specified layer
     */
    public boolean isOnLayer(int layer) {
        return (intToLayerBits(layer) & collisionLayer) != 0;
    }

    /**
     * Change the object's presence in a specified layer.
     * Making an object present in a specified layer will it enable to collide
     * with all objects including the specified layer in their collision mask.
     *
     * @param layer  the layer from 0 to TOTAL_LAYER-1, the behaviour for layers outside the range is undefined
     * @param active whether the object should be present (true) or not (false)
     */
    public void setLayer(int layer, boolean active) {
        this.collisionLayer = (short) (active ? (this.collisionLayer | intToLayerBits(layer)) : this.collisionLayer & ~intToLayerBits(layer));
    }

    /**
     * Changes the collision mask entry for a specified layer.
     * Enabling the mask for layer n will allow this object to collide with any object set in the target layer n vise versa.
     *
     * @param layer  the layer from 0 to TOTAL_LAYER-1, the behaviour for layers outside the range is undefined
     * @param active whether the mask should enabled (true) or disabled (false)
     */
    public void setMask(int layer, boolean active) {
        this.collisionMask = (short) (active ? (this.collisionMask | intToLayerBits(layer)) : this.collisionMask & ~intToLayerBits(layer));
    }

    /**
     * Shortcut method for {@link this#getBodyForce()#applyForce(Force)}.
     *
     * @param force the force that should be additionally applied to this body
     */
    public void applyForce(Force force) {
        getBodyForce().applyForce(force);
    }

    public CombinedForce getBodyForce() {
        return bodyForce;
    }

    @Override
    public void start() {
    }

    @Override
    public void update(float dt) {
        Vector2f position = gameObject.getTransform().getPosition();
        bodyForce.update(dt);
        position.add(bodyForce.direction());
    }

}