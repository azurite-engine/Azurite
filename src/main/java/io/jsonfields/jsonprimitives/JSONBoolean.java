package io.jsonfields.jsonprimitives;

import io.jsonfields.JSONField;

public class JSONBoolean implements JSONField {
    private final String name;
    private final boolean value;

    public JSONBoolean(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Boolean getValue() {
        return value;
    }
}
