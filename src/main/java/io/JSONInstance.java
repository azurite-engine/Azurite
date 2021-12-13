package io;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JSONInstance {
    private final List<JSONField> fields;

    public JSONInstance() {
        this.fields = new ArrayList<>();
    }

    public JSONInstance(Object object, boolean ignoreAnnotations) {
        this.fields = new ArrayList<>();

        populateFields(object, ignoreAnnotations);
    }

    public void populateFields(Object object, boolean ignoreAnnotations) {
        try {
            for(int i = 0; i < object.getClass().getDeclaredFields().length; i++) {
                Field field = object.getClass().getDeclaredFields()[i];
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

                if(field.get(object) == null) {
                    continue;
                }

                if(field.getType().isArray()) {
                    fields.add(new JSONField((Object[]) field.get(object), name, field.getType(), ignoreAnnotations));
                }
                else if(field.getType().isAssignableFrom(List.class)) {
                    fields.add(new JSONField(((List<?>) field.get(object)).toArray(), name, field.getType(), ignoreAnnotations));
                }
                else {
                    fields.add(new JSONField(field.get(object), name, field.getType(), ignoreAnnotations));
                }

                if(!isFieldAccessible) {
                    field.setAccessible(false);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<JSONField> getFields() {
        return fields;
    }

    public void addField(JSONField field) {
        fields.add(field);
    }

    public void removeField(JSONField field) {
        fields.remove(field);
    }
}
