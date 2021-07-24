package io.jsonfields.jsonprimitives;

import io.jsonfields.JSONField;

public class JSONInteger implements JSONField {
    private final String name;
    private final int value;

    public JSONInteger(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
