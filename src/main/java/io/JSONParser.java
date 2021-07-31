package io;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.List;

@Target(ElementType.FIELD)
@interface NoSerialize { }

@Target(ElementType.FIELD)
@interface CustomSerialize {
    String value();
}

public final class JSONParser {
    private static boolean prettyPrint = true;

    public static String serialize(JSONInstance instance) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(startJSON());

        int numTabs = 0;

        try {
            for(int i = 0; i < instance.getFields().size(); i++) {
                JSONField field = instance.getFields().get(i);
                boolean isLastField = i >= instance.getFields().size() - 1;

                if(field.getValue() == null) {
                    continue;
                }

                if(field.getType().isArray()) {
                    stringBuilder.append(tabs(numTabs + 1)).append(array(field.getName(), field.getFields(), numTabs))
                            .append(endLine(!isLastField));
                }
                else if(field.getType() == char.class) {
                    stringBuilder.append(tabs(numTabs + 1)).append(string(field.getName(), Character.toString((char) field.getValue())))
                            .append(endLine(!isLastField));
                }
                else if(field.getType().isAssignableFrom(String.class)) {
                    stringBuilder.append(tabs(numTabs + 1)).append(string(field.getName(), (String) field.getValue()))
                            .append(endLine(!isLastField));
                }
                else if(field.isPrimitive()) {
                    stringBuilder.append(tabs(numTabs + 1)).append(numericPrimitive(field.getName(), field.getValue()))
                            .append(endLine(!isLastField));
                }
                else if(!field.isPrimitive()) {
                    stringBuilder.append(tabs(numTabs + 1)).append(object(field.getName(), field.getFields(), numTabs))
                            .append(endLine(!isLastField));
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        stringBuilder.append(endJSON());

        return stringBuilder.toString();
    }

    public static void setPrettyPrint(boolean prettyPrint) {
        JSONParser.prettyPrint = prettyPrint;
    }

    private static String numericPrimitive(String name, Object value) {
        if(name != null) {
            return "\"" + name + "\": " + value;
        }
        else {
            return value.toString();
        }
    }

    private static String string(String name, String value) {
        if(name != null) {
            return "\"" + name + "\": \"" + value + "\"";
        }
        else {
            return "\"" + value + "\"";
        }
    }

    private static String object(String name, List<JSONField> jsonFields, int tabs) {
        StringBuilder stringBuilder = new StringBuilder();

        if(name != null) {
            stringBuilder.append(prettyPrint ? "\"" + name + "\": {\n" : "\"" + name + "\": { ");
        }
        else {
            stringBuilder.append(prettyPrint ? "{\n" : "{ ");
        }

        int numTabs = tabs + 1;

        try {
            for(int i = 0; i < jsonFields.size(); i++) {
                JSONField field = jsonFields.get(i);
                boolean isLastField = i >= jsonFields.size() - 1;

                if(field.getValue() == null) {
                    continue;
                }

                if(field.getType().isArray()) {
                    stringBuilder.append(tabs(numTabs + 1)).append(array(field.getName(), field.getFields(), numTabs))
                            .append(endLine(!isLastField));
                }
                else if(field.getType() == char.class) {
                    stringBuilder.append(tabs(numTabs + 1)).append(string(field.getName(), Character.toString((char) field.getValue())))
                            .append(endLine(!isLastField));
                }
                else if(field.getType().isAssignableFrom(String.class)) {
                    stringBuilder.append(tabs(numTabs + 1)).append(string(field.getName(), (String) field.getValue()))
                            .append(endLine(!isLastField));
                }
                else if(field.isPrimitive()) {
                    stringBuilder.append(tabs(numTabs + 1)).append(numericPrimitive(field.getName(), field.getValue()))
                            .append(endLine(!isLastField));
                }
                else if(!field.isPrimitive()) {
                    stringBuilder.append(tabs(numTabs + 1)).append(object(field.getName(), field.getFields(), numTabs))
                            .append(endLine(!isLastField));
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        stringBuilder.append(tabs(numTabs)).append("}");

        return stringBuilder.toString();
    }

    private static String array(String name, List<JSONField> jsonFields, int tabs) {
        StringBuilder stringBuilder = new StringBuilder();

        if(name != null) {
            stringBuilder.append(prettyPrint ? "\"" + name + "\": [\n" : "\"" + name + "\": [ ");
        }
        else {
            stringBuilder.append(prettyPrint ? "[\n" : "[ ");
        }

        int numTabs = tabs + 1;

        try {
            for(int i = 0; i < jsonFields.size(); i++) {
                JSONField field = jsonFields.get(i);
                boolean isLastField = i >= jsonFields.size() - 1;

                if(field.getValue() == null) {
                    continue;
                }

                if(field.getType() == char.class) {
                    stringBuilder.append(tabs(numTabs + 1)).append(string(field.getName(), Character.toString((char) field.getValue())))
                            .append(endLine(!isLastField));
                }
                else if(field.getType().isAssignableFrom(String.class)) {
                    stringBuilder.append(tabs(numTabs + 1)).append(string(field.getName(), (String) field.getValue()))
                            .append(endLine(!isLastField));
                }
                else if(field.getType().isPrimitive()) {
                    stringBuilder.append(tabs(numTabs + 1)).append(numericPrimitive(field.getName(), field.getValue()))
                            .append(endLine(!isLastField));
                }
                else if(!field.getType().isPrimitive()) {
                    stringBuilder.append(tabs(numTabs + 1)).append(object(field.getName(), field.getFields(), numTabs))
                            .append(endLine(!isLastField));
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        stringBuilder.append(tabs(numTabs)).append("]");

        return stringBuilder.toString();
    }

    private static String tabs(int numTabs) {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < numTabs; i++) {
            stringBuilder.append(prettyPrint ? "\t" : " ");
        }

        return stringBuilder.toString();
    }

    private static String endLine(boolean comma) {
        String string = "";

        if(comma)
            string += ",";

        string += prettyPrint ? "\n" : " ";

        return string;
    }

    private static String startJSON() {
        return prettyPrint ? "{\n" : "{ ";
    }

    private static String endJSON() {
        return "}";
    }
}
