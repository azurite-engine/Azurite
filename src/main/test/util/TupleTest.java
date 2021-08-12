package util;

import org.joml.Vector2f;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class TupleTest {

    Tuple<Integer> ints;
    Tuple<String> strs;
    Tuple<Vector2f> vecs;

    Integer[] intdata = new Integer[]{1, -4, 6, -3, 5};
    String[] strdata = new String[]{"hi", "test", "lol", "wtf"};
    Vector2f[] vecdata = new Vector2f[]{new Vector2f(0, 0), new Vector2f(0, 1), new Vector2f(1, 1)};

    @Before
    public void setUp() throws Exception {
        ints = new Tuple<>(intdata);
        strs = new Tuple<>(strdata);
        vecs = new Tuple<>(vecdata);
    }

    @Test
    public void constructors() {
        Tuple<String> stringTuple1 = new Tuple<>(new Pair<>("1", "2"));
        Tuple<String> stringTuple2 = new Tuple<>(new Triple<>("1", "2", "3"));
        Assert.assertArrayEquals(new String[]{"1", "2"}, stringTuple1.getContent());
        Assert.assertArrayEquals(new String[]{"1", "2", "3"}, stringTuple2.getContent());
    }

    @Test
    public void getN() {
        Assert.assertEquals(1, (int) ints.getN(0));
        Assert.assertEquals("hi", strs.getN(0));
        Assert.assertEquals(new Vector2f(0, 0), vecs.getN(0));
    }

    @Test
    public void setN() {
        Assert.assertEquals(1, (int) ints.getN(0));
        Assert.assertEquals("hi", strs.getN(0));
        Assert.assertEquals(new Vector2f(0, 0), vecs.getN(0));
        ints.setN(0, 10);
        strs.setN(0, "hey");
        vecs.setN(0, new Vector2f(2, 2));

        Assert.assertEquals(10, (int) ints.getN(0));
        Assert.assertEquals("hey", strs.getN(0));
        Assert.assertEquals(new Vector2f(2, 2), vecs.getN(0));
        ints.setN(0, 1);
        strs.setN(0, "hi");
        vecs.setN(0, new Vector2f(1, 1));
    }

    @Test
    public void getContent() {
        Assert.assertArrayEquals(intdata, ints.getContent());
        Assert.assertArrayEquals(strdata, strs.getContent());
        Assert.assertArrayEquals(vecdata, vecs.getContent());
    }

    @Test
    public void getPair() {
        for (int i = 0; i < intdata.length; i++) {
            for (int j = 0; j < intdata.length; j++) {
                Pair<Integer, Integer> pair = ints.getPair(i, j);
                Assert.assertEquals(intdata[i], pair.getLeft());
                Assert.assertEquals(intdata[j], pair.getRight());
            }
        }
        for (int i = 0; i < strdata.length; i++) {
            for (int j = 0; j < strdata.length; j++) {
                Pair<String, String> pair = strs.getPair(i, j);
                Assert.assertEquals(strdata[i], pair.getLeft());
                Assert.assertEquals(strdata[j], pair.getRight());
            }
        }
    }

    @Test
    public void getTriple() {
        for (int i = 0; i < intdata.length; i++) {
            for (int j = 0; j < intdata.length; j++) {
                for (int k = 0; k < intdata.length; k++) {
                    Triple<Integer, Integer, Integer> triple = ints.getTriple(i, j, k);
                    Assert.assertEquals(intdata[i], triple.getLeft());
                    Assert.assertEquals(intdata[j], triple.getMiddle());
                    Assert.assertEquals(intdata[k], triple.getRight());
                }
            }
        }
        for (int i = 0; i < strdata.length; i++) {
            for (int j = 0; j < strdata.length; j++) {
                for (int k = 0; k < strdata.length; k++) {
                    Triple<String, String, String> triple = strs.getTriple(i, j, k);
                    Assert.assertEquals(strdata[i], triple.getLeft());
                    Assert.assertEquals(strdata[j], triple.getMiddle());
                    Assert.assertEquals(strdata[k], triple.getRight());
                }
            }
        }
    }

}