package io.jsonfields;

import io.jsonfields.jsonprimitives.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class JSONObject implements JSONField {
    private final String name;
    private final List<JSONField> fields;

    public JSONObject(String name, Object value) {
        this.name = name;
        this.fields = populateFields(value);
    }

    public JSONObject(String name) {
        this.name = name;
        this.fields = new ArrayList<>();
    }

    public void addField(JSONField field) {
        fields.add(field);
    }

    public void removeField(JSONField field) {
        fields.remove(field);
    }

    public JSONField[] getFields() {
        return fields.toArray(new JSONField[0]);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return null;
    }

    private List<JSONField> populateFields(Object value) {
        List<JSONField> jsonFields = new ArrayList<>();

        try {
            for(Field field : value.getClass().getDeclaredFields()) {
                if(Modifier.isFinal(field.getModifiers()) || field.get(value) == null || Modifier.isTransient(field.getModifiers())) continue;

                boolean isFieldAccessible = field.isAccessible();
                if(!isFieldAccessible) {
                    field.setAccessible(true);
                }

                if(field.getType() == int.class) {
                    jsonFields.add(new JSONInteger(field.getName(), field.getInt(value)));
                }
                else if(field.getType() == float.class) {
                    jsonFields.add(new JSONFloat(field.getName(), field.getFloat(value)));
                }
                else if(field.getType() == double.class) {
                    jsonFields.add(new JSONDouble(field.getName(), field.getDouble(value)));
                }
                else if(field.getType() == long.class) {
                    jsonFields.add(new JSONLong(field.getName(), field.getLong(value)));
                }
                else if(field.getType() == boolean.class) {
                    jsonFields.add(new JSONBoolean(field.getName(), field.getBoolean(value)));
                }
                else if(field.getType() == byte.class) {
                    jsonFields.add(new JSONByte(field.getName(), field.getByte(value)));
                }
                else if(field.getType() == short.class) {
                    jsonFields.add(new JSONShort(field.getName(), field.getShort(value)));
                }
                else if(field.getType() == char.class) {
                    jsonFields.add(new JSONChar(field.getName(), field.getChar(value)));
                }
                else if(field.getType().isAssignableFrom(String.class)) {
                    jsonFields.add(new JSONString(field.getName(), (String) field.get(value)));
                }
                else if(field.getType().isAssignableFrom(ArrayList.class)) {
                    //TODO: Lists
                }
                else if(!field.getType().isPrimitive()) {
                    jsonFields.add(new JSONObject(field.getName(), field.get(value)));
                }
                else if(field.getType().isArray()) {
                    //TODO: Arrays
                }

                field.setAccessible(isFieldAccessible);
            }
        }
        catch(Exception ignored) { }

        return jsonFields;
    }
}
