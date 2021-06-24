package ecs;

import physics.Collider;
import physics.collision.ConvexGJKSM;
import physics.collision.Shape;
import util.Utils;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 22.06.2021
 * @since 22.06.2021
 */
public class StaticCollider extends Component implements Collider {

    //represents a series of 0s and 1s -
    //0 means not present, 1 means present
    private short collisionLayer;

    //represents a series of 0s and 1s
    //0 means no collision on that layer, 1 means collision
    private short collisionMask;

    //the collisionShape of the collider
    private final Shape collisionShape;

    public StaticCollider(Shape collisionShape, int[] layers, int[] maskedLayers) {
        this.collisionShape = collisionShape;
        this.collisionLayer = Utils.encode(layers);
        this.collisionMask = Utils.encode(maskedLayers);
        this.order = SpriteRenderer.ORDER - 1;
    }

    public StaticCollider(Shape collisionShape, int layer) {
        this.collisionShape = collisionShape;
        this.collisionLayer = Utils.encode(layer);
        this.order = SpriteRenderer.ORDER - 1;
    }

    @Override
    public Shape getCollisionShape() {
        return collisionShape;
    }

    @Override
    public boolean doesCollideWith(Collider other) {
        return ConvexGJKSM.gjksmCollision(this.collisionShape, other.getCollisionShape());
    }

    @Override
    public boolean canCollideWith(Collider other) {
        return (this.collisionLayer & other.mask()) != 0 || (this.collisionMask & other.layers()) != 0;
    }

    @Override
    public short mask() {
        return collisionMask;
    }

    @Override
    public short layers() {
        return collisionLayer;
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
    public void start() {
        collisionShape.setPosition(gameObject.getTransform().getPosition());
    }

    @Override
    public void update(float dt) {
        //the position of the object should not change at any given time, therefore its solid
    }

    @Override
    public boolean isConflictingWith(Class<? extends Component> otherComponent) {
        //there can only be one collider
        return Collider.class.isAssignableFrom(otherComponent);
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/