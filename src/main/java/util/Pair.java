package util;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 25.06.2021
 * @since 25.06.2021
 */
public class Pair<L, R> {

    private L left;
    private R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public Pair() {
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public void setRight(R right) {
        this.right = right;
    }

}