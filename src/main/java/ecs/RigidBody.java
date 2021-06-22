package ecs;

import org.joml.Vector2f;
import physics.Collider;
import physics.PhysicalEntity;
import physics.collision.ConvexGJKSM;
import physics.collision.GJKSMShape;
import physics.force.CombinedForce;
import physics.force.Force;
import util.Utils;

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
public class RigidBody extends Component implements Collider, PhysicalEntity {

    //represents a series of 0s and 1s -
    //0 means not present, 1 means present
    private short collisionLayer;

    //represents a series of 0s and 1s
    //0 means no collision on that layer, 1 means collision
    private short collisionMask;

    //the collisionShape of the collider
    private final GJKSMShape collisionShape;

    //the physical mass of the body
    private float mass;

    //the current velocity of the body
    private final Vector2f velocity;

    //the forces acting on the body and accelerating it
    private final CombinedForce bodyForce;

    public RigidBody(GJKSMShape collisionShape, int[] layers, int[] maskedLayers, float physicalMass) {
        this.collisionShape = collisionShape;
        this.collisionLayer = Utils.encode(layers);
        this.collisionMask = Utils.encode(maskedLayers);
        this.mass = physicalMass;
        this.velocity = new Vector2f();
        this.bodyForce = new CombinedForce("undefined");
    }

    public RigidBody(GJKSMShape collisionShape, int layer) {
        this.collisionShape = collisionShape;
        this.collisionLayer = Utils.encode(layer);
        this.mass = 1;
        this.velocity = new Vector2f();
        this.bodyForce = new CombinedForce("undefined");
    }

    @Override
    public GJKSMShape getCollisionShape() {
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
        //make the bodyforce unique by using the gameobjects name
        this.bodyForce.setIdentifier(gameObject.name);
        bodyForce.setMass(this.mass);
    }

    @Override
    public void update(float dt) {
        //lets give the forces a chance to update
        bodyForce.update(dt);
        //then assume they are fine and let them accelerate our velocity
        velocity.add(bodyForce.direction());
        //let the velocity then modify our current position
        gameObject.getTransform().getPosition().add(velocity);
        //last but not least update the shape around the object
        collisionShape.setPosition(gameObject.getTransform().getPosition());
    }

    public void setMass(float mass) {
        this.mass = mass;
        bodyForce.setMass(mass);
    }

    @Override
    public float getMass() {
        return mass;
    }

    @Override
    public Force getForce() {
        return bodyForce;
    }

    @Override
    public Vector2f velocity() {
        return velocity;
    }

    @Override
    public void applyForce(Force force) {
        bodyForce.applyForce(force);
    }

    @Override
    public void negateForce(String identifier) {
        bodyForce.removeForces(identifier);
    }

    @Override
    public boolean isConflictingWith(Class<? extends Component> otherComponent) {
        //there can only be one collider and only one physics calculation
        return Collider.class.isAssignableFrom(otherComponent) || PhysicalEntity.class.isAssignableFrom(otherComponent);
    }
}