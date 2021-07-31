package io;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JSONField {
    private final Object value;
    private final List<JSONField> fields;
    private final Class<?> type;
    private final String name;

    public JSONField(Object value, String name, Class<?> type, boolean ignoreAnnotations) {
        this.value = value;
        this.name = name;
        this.type = type;
        if(type.isAssignableFrom(String.class)) {
            this.fields = null;
        }
        else {
            this.fields = type.isPrimitive() ? null : populateFields(ignoreAnnotations);
        }
    }

    //Array constructor
    public JSONField(Object[] value, String name, Class<?> type, boolean ignoreAnnotations) {
        this.value = value;
        this.name = name;
        this.type = type;
        this.fields = new ArrayList<>();

        for(Object o : value) {
            fields.add(new JSONField(o, null, o.getClass(), ignoreAnnotations));
        }
    }

    public List<JSONField> populateFields(boolean ignoreAnnotations) {
        List<JSONField> generatedFields = new ArrayList<>();

        try {
            for(int i = 0; i < value.getClass().getDeclaredFields().length; i++) {
                Field field = value.getClass().getDeclaredFields()[i];
                String name = field.getName();

                if(!ignoreAnnotations) {
                    NoSerialize noSerialize = field.getAnnotation(NoSerialize.class);
                    if(noSerialize != null) {
                        continue;
                    }

                    CustomSerialize customSerialize = field.getAnnotation(CustomSerialize.class);

                    if(customSerialize != null) {
                        name = customSerialize.value();
                    }
                }

                boolean isFieldAccessible = field.isAccessible();

                if(!isFieldAccessible) {
                    field.setAccessible(true);
                }

                if(field.get(value) == null) {
                    continue;
                }

                if(field.getType().isArray()) {
                    generatedFields.add(new JSONField((Object[]) field.get(value), name, field.getType(), ignoreAnnotations));
                }
                else if(field.getType().isAssignableFrom(List.class)) {
                    generatedFields.add(new JSONField(((List<?>) field.get(value)).toArray(), name, field.getType(), ignoreAnnotations));
                }
                else {
                    generatedFields.add(new JSONField(field.get(value), name, field.getType(), ignoreAnnotations));
                }

                if(!isFieldAccessible) {
                    field.setAccessible(false);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return generatedFields;
    }

    public List<JSONField> getFields() {
        return fields;
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimitive() {
        return type.isPrimitive();
    }

    public Class<?> getType() {
        return type;
    }
}
