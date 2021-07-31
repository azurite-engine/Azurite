package io;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JSONInstance {
    private List<JSONField> fields;

    public JSONInstance() {
        this.fields = new ArrayList<>();
    }

    public JSONInstance(Object object) {
        this.fields = new ArrayList<>();

        populateFields(object);
    }

    public void populateFields(Object object) {
        try {
            for(int i = 0; i < object.getClass().getDeclaredFields().length; i++) {
                Field field = object.getClass().getDeclaredFields()[i];

                boolean isFieldAccessible = field.isAccessible();

                if(!isFieldAccessible) {
                    field.setAccessible(true);
                }

                if(field.get(object) == null) {
                    continue;
                }

                if(field.getType().isArray()) {
                    fields.add(new JSONField((Object[]) field.get(object), field.getName(), field.getType()));
                }
                else if(field.getType().isAssignableFrom(List.class)) {
                    fields.add(new JSONField(((List<?>) field.get(object)).toArray(), field.getName(), field.getType()));
                }
                else {
                    fields.add(new JSONField(field.get(object), field.getName(), field.getType()));
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
