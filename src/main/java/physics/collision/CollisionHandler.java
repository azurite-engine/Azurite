package physics.collision;

import ecs.RigidBody;

import java.util.function.BiConsumer;

/**
 * <h1>Azurite</h1>
 * Handles collisions and possibly reacts to them.
 * Can be used to call a collision event as well.
 *
 * @author Juyas
 * @version 25.06.2021
 * @see Collisions
 * @since 25.06.2021
 */
public abstract class CollisionHandler implements BiConsumer<RigidBody, CollisionInformation> {

    protected Collider parentComponent;

    public CollisionHandler() {
        this.parentComponent = null;
    }

    public void setParentComponent(Collider parentComponent) {
        this.parentComponent = parentComponent;
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/