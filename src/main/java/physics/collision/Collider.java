package physics.collision;

import physics.collision.shape.PrimitiveShape;

import java.util.Set;

/**
 * Describes an object that can collide with others.
 *
 * @author Juyas
 * @version 08.07.2021
 * @since 21.06.2021
 */
public interface Collider {

    /**
     * The shape of the collider.
     *
     * @return the shape of the collider
     */
    PrimitiveShape getShape();

    /**
     * Determines whether a collider intersects with another collider IGNORING the collision layers.
     *
     * @param collider the other collider
     * @return an object containing the result of collision detection
     */
    CollisionInformation detectCollision(Collider collider);

    /**
     * Determines whether a collider could potentially intersect with another collider ONLY by their collision layers.
     * This method can be considered part of the broad phase of collision detection.
     *
     * @param collider the other collider
     * @return true if and only if both objects could potentially collide
     */
    boolean canCollideWith(Collider collider);

    /**
     * A short containing all enabled layers encoded as bit mask.
     * Should be usable to compare different masks with each other to check for intersection.
     *
     * @return a short with all layer encoded
     */
    short layers();

    /**
     * A short containing all enabled masks encoded as bit mask.
     * Should be usable to compare different masks with each other to check for intersection.
     *
     * @return a short with all masks encoded
     */
    short mask();

    /**
     * Determines whether a collision mask for a specified layer is enabled.
     *
     * @param layer the layer from 0 to TOTAL_LAYER-1, the behaviour for layers outside the range is undefined
     * @return true if and only if the mask for the specified layer is enabled
     */
    boolean hasMask(int layer);

    /**
     * Determines whether the collider exists in a specified layer.
     *
     * @param layer the layer from 0 to TOTAL_LAYER-1, the behaviour for layers outside the range is undefined
     * @return true if and only if the collider exists in the specified layer
     */
    boolean isOnLayer(int layer);

    /**
     * Change the colliders presence in a specified layer.
     * Making a collider present in a specified layer will it enable to collide
     * with all collider including the specified layer in their collision mask.
     *
     * @param layer  the layer from 0 to TOTAL_LAYER-1, the behaviour for layers outside the range is undefined
     * @param active whether the collider should be present (true) or not (false)
     */
    void setLayer(int layer, boolean active);

    /**
     * Changes the collision mask entry for a specified layer.
     * Enabling the mask for layer n will allow this collider to collide with any other collider set in the target layer n vise versa.
     *
     * @param layer  the layer from 0 to TOTAL_LAYER-1, the behaviour for layers outside the range is undefined
     * @param active whether the mask should enabled (true) or disabled (false)
     */
    void setMask(int layer, boolean active);

    /**
     * Defines the behaviour of this collider.
     * A passive collider will never get checked against others colliders,
     * however any collider might get checked against it.
     *
     * @return whether this collider is passive
     */
    boolean passive();

    /**
     * Adds a tag to this collider, making it a tagged collider if it isnt already one.
     *
     * @param tag the new case-sensitive tag to add
     */
    void addTag(String tag);

    /**
     * Removes a tag to this collider. If the last tag got removed, this collider becomes an untagged collider.
     *
     * @param tag the case-sensitive tag to remove
     */
    void removeTag(String tag);

    /**
     * All tags assigned to this collider.
     *
     * @return an unmodifiable set of all tags assigned to this collider
     */
    Set<String> tags();

    /**
     * Defines whether a collider is a tagged or an untagged collider.
     *
     * @return true, if and only if that collider contains at least a one tag
     */
    boolean hasTags();

    /**
     * Checks for matching tags with another collider.
     * There are 3 different rules for tag matching: <br>
     * - a collider without tags can only collide with a collider without tags (ignoring tags and only using layers) <br>
     * - a collider without tags can never collide with a tagged collider <br>
     * - a tagged collider can only collide with another tagged object, if there is any intersection comparing both sets of tags <br>
     *
     * @param collider another collider
     * @return true, if the colliders are allowed to collide
     */
    boolean matchTags(Collider collider);

}