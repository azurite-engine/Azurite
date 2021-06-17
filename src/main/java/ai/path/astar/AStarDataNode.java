package ai.path.astar;

/**
 * <h1>Study Projects</h1>
 *
 * @author Julius Korweck
 * @version 17.06.2021
 * @since 17.06.2021
 */
public interface AStarDataNode extends Comparable<AStarDataNode>
{

    int getX();

    int getY();

    int getGcost();

    int getHcost();

    int getFcost();

}

/***********************************************************************************************
 *
 *                  All rights reserved, SpaceParrots UG (c) copyright 2021
 *
 ***********************************************************************************************/