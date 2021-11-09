package graphics;

import components.Component;
import graphics.renderer.RenderBatch;

/**
 * <h1>Azurite</h1>
 * A Class that represents a component that can be rendered by a specific renderer.
 * This is used to store the exact location of the component in the data buffer for safe removal.
 *
 * @param <T> The RenderBatch that renders the component
 * @see RenderBatch
 */
public abstract class RenderableComponent<T extends RenderBatch> extends Component {
    /**
     * The batch in which this component has been added
     */
    private T batch;
    /**
     * The index at which this component is placed in the batch
     */
    private int index;

    /**
     * Sets this component's batch and index to where it has currently been added
     *
     * @param batch the batch that this component has been added to
     * @param index the index at which this component is placed in the batch
     */
    public void setLocation(T batch, int index) {
        this.batch = batch;
        this.index = index;
    }

    /**
     * Get the batch in which this component has been added
     *
     * @return The batch in which this component has been added
     */
    public T getBatch() {
        return batch;
    }

    /**
     * Get the index at which this component is placed in the batch
     *
     * @return The index at which this component is placed in the batch
     */
    public int getIndex() {
        return index;
    }
}
