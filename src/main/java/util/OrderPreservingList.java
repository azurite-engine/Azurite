package util;

import java.util.Collection;
import java.util.LinkedList;

/**
 * <h1>Azurite</h1>
 * <p>
 * A LinkedList override to create an order-preserving list to improve speed in a special use case: <br>
 * You want to remove and add object from/to a list and
 * keep the elements in the list sorted without having to re-sort each time.
 *
 * @author Juyas
 * @version 18.06.2021
 * @since 18.06.2021
 */
public class OrderPreservingList<T extends Comparable<T>> extends LinkedList<T> {


    //find the next matching index for a given obj by its natural order (small to big)
    //if the obj is bigger than any other obj in the list, just add the new obj at the end i=size()
    private int findIndexToAdd(T obj) {
        int index = size();
        for (int i = 0; i < size(); i++) {
            if (get(i).compareTo(obj) >= 0)
                index = i;
        }
        return index;
    }

    @Override
    public boolean add(T t) {
        int indexToAdd = findIndexToAdd(t);
        super.add(indexToAdd, t);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return addAll(c);
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        add(element);
    }


}