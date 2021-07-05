package util;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 05.07.2021
 * @since 05.07.2021
 */
public class Tuple<Type> {

    private final Type[] content;

    public Tuple(Pair<Type, Type> data) {
        this(data.getLeft(), data.getRight());
    }

    public Tuple(Triple<Type, Type, Type> data) {
        this(data.getLeft(), data.getMiddle(), data.getRight());
    }

    public Tuple(Type... content) {
        this.content = content;
    }

    public Type getN(int n) {
        return content[n];
    }

    public void setN(int n, Type data) {
        this.content[n] = data;
    }

    public Type[] getContent() {
        return content;
    }

    public Pair<Type, Type> getPair(int n, int m) {
        return new Pair<>(getN(n), getN(m));
    }

    public Triple<Type, Type, Type> getTriple(int o, int n, int m) {
        return new Triple<>(getN(o), getN(n), getN(m));
    }

}