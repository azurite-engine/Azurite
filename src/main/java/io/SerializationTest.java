package io;

import java.util.Collections;
import java.util.List;

public class SerializationTest {
    private int i;
    private boolean b;
    @NoSerialize
    private SerializationTest st;
    private char c;
    private String s;
    @CustomSerialize("testCustom")
    private List<String> a;

    public SerializationTest(int i, boolean b, char c, String s, List<String> a) {
        this.i = i;
        this.b = b;
        this.c = c;
        this.s = s;
        this.a = a;
        this.st = new SerializationTest(69, false, 'a', "lol",  Collections.singletonList("ttt"), true);
    }

    public SerializationTest(int i, boolean b, char c, String s, List<String> a, boolean oneCopy) {
        this.i = i;
        this.b = b;
        this.c = c;
        this.s = s;
        this.a = a;
        if(!oneCopy)
            this.st = new SerializationTest(69, false, 'a', "lol", Collections.singletonList("ttt"));
    }
}
