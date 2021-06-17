package ai.path;

/**
 * <h1>Study Projects</h1>
 *
 * @author Julius Korweck
 * @version 17.06.2021
 * @since 17.06.2021
 */
public interface PathfindingAlgorithm
{

    void use( PathNode... nodes );

    NodePath find(PathNode start, PathNode target );

    void set( int information, Object data );

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/