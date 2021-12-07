package ecs;

import physics.collision.Collider;
import physics.collision.CollisionInformation;
import physics.collision.CollisionUtil;
import physics.collision.shape.PrimitiveShape;
import util.Utils;

/**
 * @author Juyas
 * @version 06.12.2021
 * @since 06.12.2021
 */
public class PolygonCollider extends Component implements Collider {

    /**
     * The collision shape of the collider.
     *
     * @see PrimitiveShape
     */
    private PrimitiveShape shape;

    /**
     * A short representing a binary series of 0s and 1s.
     * 0 means not present, 1 means present.
     * Beeing present means, beeing present on the collision layer
     * and allow collision with all objects having a present mask on this layer.
     */
    private short collisionLayer;

    /**
     * A short representing a binary series of 0s and 1s.
     * 0 means not present, 1 means present.
     * Beeing present means, beeing able to collide with all objects present on that layer,
     * while its not required to be present on the layer.
     */
    private short collisionMask;

    /**
     * @see Collider#passive()
     */
    private final boolean passive;

    public PolygonCollider(PrimitiveShape shape, boolean passive) {
        super(ComponentOrder.POST_TRANSFORM);
        setShape(shape);
        this.passive = passive;
    }

    public PolygonCollider(PrimitiveShape shape) {
        this(shape, false);
    }

    public void setShape(PrimitiveShape shape) {
        if (shape == null) throw new IllegalArgumentException("The shape of a collider shall not be null");
        this.shape = shape;
    }

    @Override
    public PrimitiveShape getShape() {
        return shape;
    }

    @Override
    public CollisionInformation detectCollision(Collider collider) {
        return CollisionUtil.gjksmCollision(this.getShape(), collider.getShape());
    }

    @Override
    public boolean canCollideWith(Collider other) {
        return (this.collisionLayer & other.mask()) != 0 || (this.collisionMask & other.layers()) != 0;
    }

    @Override
    public short layers() {
        return collisionLayer;
    }

    @Override
    public short mask() {
        return collisionMask;
    }

    @Override
    public boolean hasMask(int layer) {
        return (Utils.encode(layer) & mask()) != 0;
    }

    @Override
    public boolean isOnLayer(int layer) {
        return (Utils.encode(layer) & layers()) != 0;
    }

    @Override
    public void setLayer(int layer, boolean active) {
        this.collisionLayer = (short) (active ? (layers() | Utils.encode(layer)) : layers() & ~Utils.encode(layer));
    }

    @Override
    public void setMask(int layer, boolean active) {
        this.collisionMask = (short) (active ? (this.collisionMask | Utils.encode(layer)) : this.collisionMask & ~Utils.encode(layer));
    }

    @Override
    public boolean passive() {
        return passive;
    }

    @Override
    public void update(float dt) {
        shape.setPosition(position());
    }
}