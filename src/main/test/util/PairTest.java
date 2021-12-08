package util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Juyas
 * @version 19.07.2021
 * @since 19.07.2021
 */
public class PairTest {

    @Test
    public void getterSetter() {
        Pair<String, String> pair = new Pair<>("l", "r");
        Assert.assertEquals("l", pair.getLeft());
        Assert.assertEquals("r", pair.getRight());
        pair.setLeft("1");
        pair.setRight("2");
        Assert.assertEquals("1", pair.getLeft());
        Assert.assertEquals("2", pair.getRight());
    }

    @Test
    public void extend() {
        Pair<String, String> pair = new Pair<>("l", "r");
        Triple<String, String, String> rr = pair.extend("rr");
        Assert.assertEquals("l", rr.getLeft());
        Assert.assertEquals("r", rr.getMiddle());
        Assert.assertEquals("rr", rr.getRight());
    }

}