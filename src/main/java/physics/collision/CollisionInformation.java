package physics.collision;

/**
 * <h1>Azurite</h1>
 *
 * @author Julius Korweck
 * @version 02.09.2021
 * @since 02.09.2021
 */
public class CollisionInformation {

    private Object data;
    private boolean collision;

    public CollisionInformation(Object data, boolean collision) {
        this.data = data;
        this.collision = collision;
    }

    public final Object get() {
        return data;
    }

    public final boolean collision() {
        return collision;
    }

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/