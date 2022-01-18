package io.json;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class JSONObject {
    private StringBuilder stringBuilder = new StringBuilder();

    public String getJSON() {
        return stringBuilder.toString();
    }

    private StringBuilder tabs(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append("\t");
        }

        return sb;
    }

    private String lineEnding(boolean newLine, boolean comma) {
        String s = "";

        if (comma) s += ",";
        if (newLine) s += "\n";

        return s;
    }

    public void startJSON() {
        stringBuilder.append("{").append(lineEnding(true, false));
    }

    public void endJSON() {
        stringBuilder.append("}");
    }

    public void addObject(String propertyName, Object obj, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\"").append(":").append(" {").append(lineEnding(true, false));
        List<Field> fields = Arrays.asList(obj.getClass().getDeclaredFields());
        Field field;

        try {
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
                    addVar(field.getName(), field.getInt(obj), numTabs + 1, true, isNotLastField);
                } else if (field.getType() == float.class) {
                    addVar(field.getName(), field.getFloat(obj), numTabs + 1, true, isNotLastField);
                } else if (field.getType() == double.class) {
                    addVar(field.getName(), field.getDouble(obj), numTabs + 1, true, isNotLastField);
                } else if (field.getType() == char.class) {
                    addVar(field.getName(), field.getChar(obj), numTabs + 1, true, isNotLastField);
                } else if (field.getType() == String.class) {
                    addVar(field.getName(), (String) field.get(obj), numTabs + 1, true, isNotLastField);
                } else if (field.getType() == boolean.class) {
                    addVar(field.getName(), field.getBoolean(obj), numTabs + 1, true, isNotLastField);
                } else if (field.getType().isArray()) {
                    if (field.getType().getComponentType() == int.class) {
                        addArray(field.getName(), (int[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    } else if (field.getType().getComponentType() == float.class) {
                        addArray(field.getName(), (float[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    } else if (field.getType().getComponentType() == double.class) {
                        addArray(field.getName(), (double[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    } else if (field.getType().getComponentType() == char.class) {
                        addArray(field.getName(), (char[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    } else if (field.getType().getComponentType() == String.class) {
                        addArray(field.getName(), (String[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    } else if (field.getType().getComponentType() == boolean.class) {
                        addArray(field.getName(), (boolean[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    } else if (!field.getType().isPrimitive()) {
                        addArray(field.getName(), (Object[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    }
                } else if (field.getType().isAssignableFrom(List.class)) {
                    addList(field.getName(), (List<?>) field.get(obj), numTabs + 1, true, isNotLastField);
                } else if (!field.getType().isPrimitive()) {
                    addObject(field.getName(), field.get(obj), numTabs + 1, true, isNotLastField);
                }

                if (!isFieldAccessible) {
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stringBuilder.append(tabs(numTabs)).append("}").append(lineEnding(newLine, comma));
    }

    private void addObject(Object obj, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("{").append(lineEnding(true, false));
        List<Field> fields = Arrays.asList(obj.getClass().getDeclaredFields());
        Field field;

        addVar("type", obj.getClass().getTypeName(), numTabs + 1, true, true);

        try {
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
                    addVar(field.getName(), field.getInt(obj), numTabs + 1, true, isNotLastField);
                } else if (field.getType() == float.class) {
                    addVar(field.getName(), field.getFloat(obj), numTabs + 1, true, isNotLastField);
                } else if (field.getType() == double.class) {
                    addVar(field.getName(), field.getDouble(obj), numTabs + 1, true, isNotLastField);
                } else if (field.getType() == char.class) {
                    addVar(field.getName(), field.getChar(obj), numTabs + 1, true, isNotLastField);
                } else if (field.getType() == String.class) {
                    addVar(field.getName(), (String) field.get(obj), numTabs + 1, true, isNotLastField);
                } else if (field.getType() == boolean.class) {
                    addVar(field.getName(), field.getBoolean(obj), numTabs + 1, true, isNotLastField);
                } else if (field.getType().isArray()) {
                    if (field.getType().getComponentType() == int.class) {
                        addArray(field.getName(), (int[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    } else if (field.getType().getComponentType() == float.class) {
                        addArray(field.getName(), (float[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    } else if (field.getType().getComponentType() == double.class) {
                        addArray(field.getName(), (double[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    } else if (field.getType().getComponentType() == char.class) {
                        addArray(field.getName(), (char[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    } else if (field.getType().getComponentType() == String.class) {
                        addArray(field.getName(), (String[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    } else if (field.getType().getComponentType() == boolean.class) {
                        addArray(field.getName(), (boolean[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    } else if (!field.getType().isPrimitive()) {
                        addArray(field.getName(), (Object[]) field.get(obj), numTabs + 1, true, isNotLastField);
                    }
                } else if (field.getType().isAssignableFrom(List.class)) {
                    addList(field.getName(), (List<?>) field.get(obj), numTabs + 1, true, isNotLastField);
                } else if (!field.getType().isPrimitive()) {
                    addObject(field.getName(), field.get(obj), numTabs + 1, true, isNotLastField);
                }

                if (!isFieldAccessible) {
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stringBuilder.append(tabs(numTabs)).append("}").append(lineEnding(newLine, comma));
    }

    public void addVar(String propertyName, int value, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\":").append(value).append(lineEnding(newLine, comma));
    }

    public void addVar(String propertyName, float value, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\":").append(value).append(lineEnding(newLine, comma));
    }

    public void addVar(String propertyName, String value, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\":").append("\"").append(value).append("\"").append(lineEnding(newLine, comma));
    }

    public void addVar(String propertyName, double value, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\":").append(value).append(lineEnding(newLine, comma));
    }

    public void addVar(String propertyName, char value, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\":").append("\"").append(value).append("\"").append(lineEnding(newLine, comma));
    }

    public void addVar(String propertyName, boolean value, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\":").append(value).append(lineEnding(newLine, comma));
    }

    public void addArray(String propertyName, String[] array, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\"").append(": [").append(lineEnding(true, false));
        for (int i = 0; i < array.length; i++) {
            if (i < array.length - 1) {
                stringBuilder.append(tabs(numTabs + 1)).append("\"").append(array[i]).append("\"").append(lineEnding(true, true));
                continue;
            }

            stringBuilder.append(tabs(numTabs + 1)).append("\"").append(array[i]).append("\"").append(lineEnding(true, false));
        }
        stringBuilder.append(tabs(numTabs)).append("]").append(lineEnding(newLine, comma));
    }

    public void addArray(String propertyName, float[] array, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\"").append(": [").append(lineEnding(true, false));
        for (int i = 0; i < array.length; i++) {
            if (i < array.length - 1) {
                stringBuilder.append(tabs(numTabs + 1)).append(array[i]).append("f").append(lineEnding(true, true));
                continue;
            }

            stringBuilder.append(tabs(numTabs + 1)).append(array[i]).append("f").append(lineEnding(true, false));
        }
        stringBuilder.append(tabs(numTabs)).append("]").append(lineEnding(newLine, comma));
    }

    public void addArray(String propertyName, int[] array, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\"").append(": [").append(lineEnding(true, false));
        for (int i = 0; i < array.length; i++) {
            if (i < array.length - 1) {
                stringBuilder.append(tabs(numTabs + 1)).append(array[i]).append(lineEnding(true, true));
                continue;
            }

            stringBuilder.append(tabs(numTabs + 1)).append(array[i]).append(lineEnding(true, false));
        }
        stringBuilder.append(tabs(numTabs)).append("]").append(lineEnding(newLine, comma));
    }

    public void addArray(String propertyName, double[] array, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\"").append(": [").append(lineEnding(true, false));
        for (int i = 0; i < array.length; i++) {
            if (i < array.length - 1) {
                stringBuilder.append(tabs(numTabs + 1)).append(array[i]).append(lineEnding(true, true));
                continue;
            }

            stringBuilder.append(tabs(numTabs + 1)).append(array[i]).append(lineEnding(true, false));
        }
        stringBuilder.append(tabs(numTabs)).append("]").append(lineEnding(newLine, comma));
    }

    public void addArray(String propertyName, char[] array, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\"").append(": [").append(lineEnding(true, false));
        for (int i = 0; i < array.length; i++) {
            if (i < array.length - 1) {
                stringBuilder.append(tabs(numTabs + 1)).append(array[i]).append(lineEnding(true, true));
                continue;
            }

            stringBuilder.append(tabs(numTabs + 1)).append(array[i]).append(lineEnding(true, false));
        }
        stringBuilder.append(tabs(numTabs)).append("]").append(lineEnding(newLine, comma));
    }

    public void addArray(String propertyName, boolean[] array, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\"").append(": [").append(lineEnding(true, false));
        for (int i = 0; i < array.length; i++) {
            if (i < array.length - 1) {
                stringBuilder.append(tabs(numTabs + 1)).append(array[i]).append(lineEnding(true, true));
                continue;
            }

            stringBuilder.append(tabs(numTabs + 1)).append(array[i]).append(lineEnding(true, false));
        }
        stringBuilder.append(tabs(numTabs)).append("]").append(lineEnding(newLine, comma));
    }

    public void addArray(String propertyName, Object[] array, int numTabs, boolean newLine, boolean comma) {
        stringBuilder.append(tabs(numTabs)).append("\"").append(propertyName).append("\"").append(": [").append(lineEnding(true, false));
        for (int i = 0; i < array.length; i++) {
            if (i < array.length - 1) {
                addObject(array[i], numTabs + 1, true, true);
                continue;
            }

            addObject(array[i], numTabs + 1, true, false);
        }
        stringBuilder.append(tabs(numTabs)).append("]").append(lineEnding(newLine, comma));
    }

    public <T> void addList(String propertyName, List<T> list, int numTabs, boolean newLine, boolean comma) {
        addArray(propertyName, list.toArray(), numTabs, newLine, comma);
    }
}
