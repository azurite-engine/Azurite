package io;

import io.jsonfields.JSONField;
import io.jsonfields.JSONObject;
import io.jsonfields.jsonprimitives.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class JSONInstance {
    private final List<JSONField> fields;

    public JSONInstance(Object object) {
        this.fields = populateFields(object);
    }

    public JSONInstance() {
        this.fields = new ArrayList<>();
    }

    private List<JSONField> populateFields(Object object) {
        List<JSONField> jsonFields = new ArrayList<>();

        try {
            for(Field field : object.getClass().getDeclaredFields()) {
                boolean isFieldAccessible = field.isAccessible();
                if(!isFieldAccessible) {
                    field.setAccessible(true);
                }

                if(Modifier.isFinal(field.getModifiers()) || field.get(object) == null || Modifier.isTransient(field.getModifiers())) continue;

                if(field.getType() == int.class) {
                    jsonFields.add(new JSONInteger(field.getName(), field.getInt(object)));
                }
                else if(field.getType() == float.class) {
                    jsonFields.add(new JSONFloat(field.getName(), field.getFloat(object)));
                }
                else if(field.getType() == double.class) {
                    jsonFields.add(new JSONDouble(field.getName(), field.getDouble(object)));
                }
                else if(field.getType() == long.class) {
                    jsonFields.add(new JSONLong(field.getName(), field.getLong(object)));
                }
                else if(field.getType() == boolean.class) {
                    jsonFields.add(new JSONBoolean(field.getName(), field.getBoolean(object)));
                }
                else if(field.getType() == byte.class) {
                    jsonFields.add(new JSONByte(field.getName(), field.getByte(object)));
                }
                else if(field.getType() == short.class) {
                    jsonFields.add(new JSONShort(field.getName(), field.getShort(object)));
                }
                else if(field.getType() == char.class) {
                    jsonFields.add(new JSONChar(field.getName(), field.getChar(object)));
                }
                else if(field.getType().isAssignableFrom(String.class)) {
                    jsonFields.add(new JSONString(field.getName(), (String) field.get(object)));
                }
                else if(field.getType().isAssignableFrom(ArrayList.class)) {
                    //TODO: Lists
                }
                else if(!field.getType().isPrimitive()) {
                    jsonFields.add(new JSONObject(field.getName(), field.get(object)));
                }
                else if(field.getType().isArray()) {
                    //TODO: Arrays
                }

                field.setAccessible(isFieldAccessible);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return jsonFields;
    }

    public void addField(JSONField field) {
        fields.add(field);
    }

    public void removeField(JSONField field) {
        fields.remove(field);
    }

    public List<JSONField> getFields() {
        return fields;
    }
}
