package physics.collision;

import ecs.RigidBody;
import org.joml.Vector2f;
import util.Tuple;

import java.util.function.BiConsumer;

/**
 * <h1>Azurite</h1>
 *
 * @author Julius Korweck
 * @version 25.06.2021
 * @since 25.06.2021
 */
public abstract class CollisionHandler implements BiConsumer<RigidBody, Tuple<Vector2f>> {

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