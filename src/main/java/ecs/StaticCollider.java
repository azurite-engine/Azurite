package ecs;

import graphics.Color;
import org.joml.Vector2f;
import physics.collision.Collider;
import physics.collision.CollisionHandler;
import physics.collision.CollisionUtil;
import physics.collision.Collisions;
import physics.collision.shape.PrimitiveShape;
import physics.collision.shape.Quadrilateral;
import util.Tuple;
import util.Utils;
import util.debug.DebugLine;
import util.debug.DebugPrimitive;

import java.util.Optional;

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
    private final PrimitiveShape collisionShape;

    //used to feed with rigidBodies this static block is colliding with
    private CollisionHandler collisionHandler = Collisions.solid();

    public StaticCollider(PrimitiveShape collisionShape, int[] layers, int[] maskedLayers) {
        this.collisionShape = collisionShape;
        this.collisionLayer = Utils.encode(layers);
        this.collisionMask = Utils.encode(maskedLayers);
        this.order = SpriteRenderer.ORDER - 1;
    }

    public StaticCollider(PrimitiveShape collisionShape, int layer) {
        this.collisionShape = collisionShape;
        this.collisionLayer = Utils.encode(layer);
        this.order = SpriteRenderer.ORDER - 1;
    }

    public void setCollisionHandler(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
        this.collisionHandler.setParentComponent(this);
    }

    @Override
    public PrimitiveShape getCollisionShape() {
        return collisionShape;
    }

    @Override
    public Optional<Tuple<Vector2f>> doesCollideWith(Collider other) {
        return CollisionUtil.gjksmCollision(this.collisionShape, other.getCollisionShape());
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
        this.collisionShape.setPosition(gameObject.getReadOnlyTransform().getPosition());
        this.collisionHandler.setParentComponent(this);
    }

    @Override
    public void update(float dt) {
        //the position of the object should not change at any given time, therefore its solid
    }

    @Override
    public void handleCollision(Collider otherCollider, Tuple<Vector2f> gjkSimplex) {
        if (otherCollider instanceof RigidBody)
            collisionHandler.accept((RigidBody) otherCollider, gjkSimplex);
    }

    @Override
    public void resetCollision() {

    }

    @Override
    public boolean isConflictingWith(Class<? extends Component> otherComponent) {
        //there can only be one collider
        return Collider.class.isAssignableFrom(otherComponent);
    }

    @Override
    public DebugPrimitive[] debug() {
        if (collisionShape instanceof Quadrilateral) {
            Quadrilateral rect = (Quadrilateral) collisionShape;
            Vector2f[] points = rect.getAbsolutePoints();
            DebugLine[] lines = new DebugLine[4];
            for (int i = 0; i < 4; i++) {
                lines[i] = new DebugLine(points[i], points[(i + 1) % 4], Color.GREEN);
            }
            DebugPrimitive primitive = new DebugPrimitive(lines);
            return new DebugPrimitive[]{primitive};
        }
        return null;
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/