package ecs;

import graphics.Color;
import org.joml.Vector2f;
import physics.PhysicalEntity;
import physics.Transform;
import physics.TransformSensitive;
import physics.collision.Collider;
import physics.collision.CollisionHandler;
import physics.collision.CollisionUtil;
import physics.collision.Collisions;
import physics.collision.shape.PrimitiveShape;
import physics.collision.shape.Quadrilateral;
import physics.force.CombinedForce;
import physics.force.Force;
import util.Utils;
import util.debug.DebugLine;
import util.debug.DebugPrimitive;

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
public class RigidBody extends Component implements Collider, PhysicalEntity, TransformSensitive {

    //represents a series of 0s and 1s -
    //0 means not present, 1 means present
    private short collisionLayer;

    //represents a series of 0s and 1s
    //0 means no collision on that layer, 1 means collision
    private short collisionMask;

    //the collisionShape of the collider
    private final PrimitiveShape collisionShape;

    //the physical mass of the body
    private float mass;

    //the current velocity of the body
    private final Vector2f velocity;

    //the forces acting on the body and accelerating it
    private final CombinedForce bodyForce;

    //used to feed with objects this body is colliding with
    private CollisionHandler collisionHandler = Collisions.solid();

    public RigidBody(PrimitiveShape collisionShape, int[] layers, int[] maskedLayers, float physicalMass) {
        super(Collider.class, PhysicalEntity.class); //this class type should be unique in a gameObject
        this.collisionShape = collisionShape;
        this.collisionLayer = Utils.encode(layers);
        this.collisionMask = Utils.encode(maskedLayers);
        this.mass = physicalMass;
        this.velocity = new Vector2f();
        this.bodyForce = new CombinedForce("undefined");
        this.order = SpriteRenderer.ORDER - 1;
    }

    public RigidBody(PrimitiveShape collisionShape, int layer) {
        super(Collider.class, PhysicalEntity.class); //this class type should be unique in a gameObject
        this.collisionShape = collisionShape;
        this.collisionLayer = Utils.encode(layer);
        this.mass = 1;
        this.velocity = new Vector2f();
        this.bodyForce = new CombinedForce("undefined");
        this.order = SpriteRenderer.ORDER - 1;
    }

    public void setCollisionType(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
        this.collisionHandler.setParentComponent(this);
    }

    @Override
    public PrimitiveShape getCollisionShape() {
        return collisionShape;
    }

    @Override
    public boolean doesCollideWith(Collider other) {
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
        //make the bodyforce unique by using the gameobjects name
        this.bodyForce.setIdentifier(gameObject.name());
        this.bodyForce.setMass(this.mass);
        this.collisionHandler.setParentComponent(this);
        this.collisionShape.setPosition(gameObject.getReadOnlyTransform().getPosition());
    }

    @Override
    public void update(float dt) {
        //lets give the forces a chance to update
        bodyForce.update(dt);
        //then assume they are fine and let them accelerate our velocity
        velocity.add(bodyForce.direction());
        //let the velocity then modify our current position - push to buffer
        gameObject.positionBuffer().add(velocity);
    }

    @Override
    public void handleCollision(Collider otherCollider) {
        //static collisions should be handled by the static object
        if (otherCollider instanceof StaticCollider)
            otherCollider.handleCollision(this);
            //collisions with other rigidbodys will be handled here
        else collisionHandler.accept((RigidBody) otherCollider);
        recentCollision = true;
    }

    boolean recentCollision = false;
    boolean colliding = false;

    @Override
    public void resetCollision() {
        if (!recentCollision && colliding)
            colliding = false;
        if (recentCollision && !colliding)
            colliding = true;
        recentCollision = false;
    }

    public boolean isColliding() {
        return colliding;
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

    @Override
    public void update(Transform changedTransform) {
        //update the shape according to changes made to transform
        collisionShape.setPosition(gameObject.getReadOnlyTransform().getPosition());

        //debug only
        if (collisionShape instanceof Quadrilateral) {
            Quadrilateral rect = (Quadrilateral) collisionShape;
            Vector2f[] points = rect.getAbsolutePoints();
            for (int i = 0; i < 4; i++) {
                lines[i].start.set(points[i]);
                lines[i].end.set(points[(i + 1) % 4]);
                lines[i].markDirty();
            }
            lines[4].start.set(rect.centroid());
            lines[4].end.set(rect.centroid().add(velocity().mul(50, new Vector2f()), new Vector2f()));
            lines[4].markDirty();
        }

    }

    //debug
    DebugLine[] lines = new DebugLine[5];
    DebugPrimitive primitive = new DebugPrimitive(lines);

    @Override
    public DebugPrimitive[] debug() {
        if (collisionShape instanceof Quadrilateral) {
            Quadrilateral rect = (Quadrilateral) collisionShape;
            Vector2f[] points = rect.getAbsolutePoints();
            for (int i = 0; i < 4; i++) {
                lines[i] = new DebugLine(points[i], points[(i + 1) % 4], Color.BLUE);
            }
            lines[4] = new DebugLine(rect.centroid(), rect.centroid().add(velocity().mul(50, new Vector2f()), new Vector2f()), Color.YELLOW);
            return new DebugPrimitive[]{primitive};
        }
        return null;
    }

}