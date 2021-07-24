package io.jsonfields.jsonprimitives;

import io.jsonfields.JSONField;

public class JSONString implements JSONField {
    private final String name;
    private final String value;

    public JSONString(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }
}
