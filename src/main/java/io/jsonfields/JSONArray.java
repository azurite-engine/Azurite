package io.jsonfields;

import java.util.ArrayList;
import java.util.List;

public class JSONArray implements JSONField {
    private final String name;
    //private final JSONField[] elements;

    public JSONArray(String name, int[] array) {
        this.name = name;
        //this.elements = populateElements(array);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JSONField[] getValue() {
        return null;
    }
}
