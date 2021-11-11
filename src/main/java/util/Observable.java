package util;

import java.util.LinkedList;
import java.util.List;

/**
 * Standard observable model.
 *
 * @author Juyas
 * @version 11.11.2021
 * @since 11.11.2021
 */
public class Observable<Type> {

    private Type value;
    private final List<Observer<Type>> observers;

    public Observable(Type defValue) {
        this.value = defValue;
        this.observers = new LinkedList<>();
    }

    public Observable() {
        this(null);
    }

    /**
     * Add an observer to this observable.
     *
     * @param observer the observer to add
     */
    public void observe(Observer<Type> observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this observable.
     *
     * @param observer the observer to remove
     */
    public void ignore(Observer<Type> observer) {
        this.observers.remove(observer);
    }

    /**
     * Removes all observers from this observable
     */
    public void ignoreAll() {
        this.observers.clear();
    }

    public synchronized void setValue(Type value) {
        this.value = value;
        observers.forEach(o -> o.notify(value));
    }

    public Type getValue() {
        return value;
    }

}