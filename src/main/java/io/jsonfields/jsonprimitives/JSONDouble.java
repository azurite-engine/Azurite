package io.jsonfields.jsonprimitives;

import io.jsonfields.JSONField;

public class JSONDouble implements JSONField {
    private final String name;
    private final double value;

    public JSONDouble(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Double getValue() {
        return value;
    }
}
