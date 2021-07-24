package io.jsonfields.jsonprimitives;

import io.jsonfields.JSONField;

public class JSONChar implements JSONField {
    private final String name;
    private final char value;

    public JSONChar(String name, char value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Character getValue() {
        return value;
    }
}
