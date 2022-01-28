package util.safety;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Juyas
 * @version 19.07.2021
 * @since 19.07.2021
 */
public class PreconditionsTest {

    @Test
    public void nonNull() {
        String object = "Any String, test";
        Assert.assertEquals(object, Preconditions.nonNull(object));
    }

    @Test(expected = NullPointerException.class)
    public void isNull() {
        Preconditions.nonNull(null);
    }

    @Test
    public void isNull2() {
        String name = "obj";
        try {
            Preconditions.nonNull(name, null);
        } catch (NullPointerException e) {
            Assert.assertEquals(name + " is null", e.getMessage());
        }
    }

}