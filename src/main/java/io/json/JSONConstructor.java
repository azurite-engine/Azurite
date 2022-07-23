package io.json;

import util.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class JSONConstructor {
    private JSONObject serializedObject;

    public JSONConstructor() {
    }

    public JSONConstructor(JSONObject jsonObject) {
        this.serializedObject = jsonObject;
    }

    public void toJSON(Object obj) {
        List<Field> fields = Arrays.asList(obj.getClass().getDeclaredFields());
        Field field;
        serializedObject = new JSONObject();
        serializedObject.startJSON();

        try {
            int numTabs = 1;
            boolean isNotLastField = true;
            for (int i = 0; i < fields.size(); i++) {
                field = fields.get(i);

                boolean isFieldAccessible = field.isAccessible();
                if (!isFieldAccessible) {
                    field.setAccessible(true);
                }

                if (!field.getName().equals("$assertionsDisabled") && (Modifier.isTransient(field.getModifiers()) || Modifier.isFinal(field.getModifiers()) || field.get(obj) == null)) {
                    continue;
                }

                if (i == fields.size() - 1) {
                    isNotLastField = false;
                }

                if (field.getType() == int.class) {
                    serializedObject.addVar(field.getName(), field.getInt(obj), numTabs, true, isNotLastField);
                } else if (field.getType() == float.class) {
                    serializedObject.addVar(field.getName(), field.getFloat(obj), numTabs, true, isNotLastField);
                } else if (field.getType() == double.class) {
                    serializedObject.addVar(field.getName(), field.getDouble(obj), numTabs, true, isNotLastField);
                } else if (field.getType() == char.class) {
                    serializedObject.addVar(field.getName(), field.getChar(obj), numTabs, true, isNotLastField);
                } else if (field.getType() == String.class) {
                    serializedObject.addVar(field.getName(), (String) field.get(obj), numTabs, true, isNotLastField);
                } else if (field.getType() == boolean.class) {
                    serializedObject.addVar(field.getName(), field.getBoolean(obj), numTabs, true, isNotLastField);
                } else if (field.getType().isArray()) {
                    if (field.getType().getComponentType() == int.class) {
                        serializedObject.addArray(field.getName(), (int[]) field.get(obj), numTabs, true, isNotLastField);
                    } else if (field.getType().getComponentType() == float.class) {
                        serializedObject.addArray(field.getName(), (float[]) field.get(obj), numTabs, true, isNotLastField);
                    } else if (field.getType().getComponentType() == double.class) {
                        serializedObject.addArray(field.getName(), (double[]) field.get(obj), numTabs, true, isNotLastField);
                    } else if (field.getType().getComponentType() == char.class) {
                        serializedObject.addArray(field.getName(), (char[]) field.get(obj), numTabs, true, isNotLastField);
                    } else if (field.getType().getComponentType() == String.class) {
                        serializedObject.addArray(field.getName(), (String[]) field.get(obj), numTabs, true, isNotLastField);
                    } else if (field.getType().getComponentType() == boolean.class) {
                        serializedObject.addArray(field.getName(), (boolean[]) field.get(obj), numTabs, true, isNotLastField);
                    } else if (!field.getType().isPrimitive()) {
                        serializedObject.addArray(field.getName(), (Object[]) field.get(obj), numTabs, true, isNotLastField);
                    }
                } else if (field.getType().isAssignableFrom(List.class)) {
                    serializedObject.addList(field.getName(), (List<?>) field.get(obj), numTabs, true, isNotLastField);
                } else if (!field.getType().isPrimitive()) {
                    serializedObject.addObject(field.getName(), field.get(obj), numTabs, true, isNotLastField);
                }

                if (!isFieldAccessible) {
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            Log.fatal("Error while handling JSON conversion");
            e.printStackTrace();
        }

        serializedObject.endJSON();
    }

    public void saveToFile(String filePath) {
        try {
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write(serializedObject.getJSON());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        Log.p(serializedObject.getJSON());
    }
}
