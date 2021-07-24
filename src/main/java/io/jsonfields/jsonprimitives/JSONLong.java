package io.jsonfields.jsonprimitives;

import io.jsonfields.JSONField;

public class JSONLong implements JSONField {
    private final String name;
    private final long value;

    public JSONLong(String name, long value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getValue() {
        return value;
    }
}
