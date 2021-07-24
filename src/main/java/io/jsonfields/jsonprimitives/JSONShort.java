package io.jsonfields.jsonprimitives;

import io.jsonfields.JSONField;

public class JSONShort implements JSONField {
    private final String name;
    private final short value;

    public JSONShort(String name, short value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Short getValue() {
        return value;
    }
}
