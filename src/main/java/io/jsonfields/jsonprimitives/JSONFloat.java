package io.jsonfields.jsonprimitives;

import io.jsonfields.JSONField;

public class JSONFloat implements JSONField {
    private final String name;
    private final float value;

    public JSONFloat(String name, float value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Float getValue() {
        return value;
    }
}
