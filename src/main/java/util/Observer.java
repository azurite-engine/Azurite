package util;

/**
 * Standard observer model.
 *
 * @author Juyas
 * @version 11.11.2021
 * @since 11.11.2021
 */
public interface Observer<Type> {

    /**
     * This method gets called by an {@link Observable} if the value has been changed
     *
     * @param newVal the new value of the observed variable
     */
    void notify(Type newVal);

}