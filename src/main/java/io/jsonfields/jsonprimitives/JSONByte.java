package io.jsonfields.jsonprimitives;

import io.jsonfields.JSONField;

public class JSONByte implements JSONField {
    private final String name;
    private final byte value;

    public JSONByte(String name, byte value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Byte getValue() {
        return value;
    }
}
