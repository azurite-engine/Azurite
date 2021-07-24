package io;

import io.jsonfields.*;
import io.jsonfields.jsonprimitives.*;

public final class JSONParser {
    private static boolean prettyPrint = false;

    //TODO: Finish
    public static String parse(JSONInstance instance) {
        StringBuilder parsedJSOSN = new StringBuilder();

        parsedJSOSN.append(startJSON());

        int numTabs = 0;
        for(int i = 0; i < instance.getFields().size(); i++) {
            JSONField field = instance.getFields().get(i);
            boolean isLastField = instance.getFields().size() - 1 == i;

            if(field instanceof JSONString) {
                parsedJSOSN.append(tabs(numTabs + 1)).append(aString((JSONString) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONInteger) {
                parsedJSOSN.append(tabs(numTabs + 1)).append(anInt((JSONInteger) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONFloat) {
                parsedJSOSN.append(tabs(numTabs + 1)).append(aFloat((JSONFloat) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONChar) {
                parsedJSOSN.append(tabs(numTabs + 1)).append(aChar((JSONChar) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONLong) {
                parsedJSOSN.append(tabs(numTabs + 1)).append(aLong((JSONLong) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONByte) {
                parsedJSOSN.append(tabs(numTabs + 1)).append(aByte((JSONByte) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONBoolean) {
                parsedJSOSN.append(tabs(numTabs + 1)).append(aBoolean((JSONBoolean) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONDouble) {
                parsedJSOSN.append(tabs(numTabs + 1)).append(aDouble((JSONDouble) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONShort) {
                parsedJSOSN.append(tabs(numTabs + 1)).append(aShort((JSONShort) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONObject) {
                parsedJSOSN.append(tabs(numTabs + 1)).append(anObject((JSONObject) field, numTabs)).append(lineEnding(!isLastField));
            }
        }

        parsedJSOSN.append(endJSON());

        return parsedJSOSN.toString();
    }

    //TODO: Finish
    public static JSONInstance parse(String jsonString) {
        return null;
    }

    public static boolean getPrettyPrint() {
        return prettyPrint;
    }

    public static void setPrettyPrint(boolean prettyPrint) {
        JSONParser.prettyPrint = prettyPrint;
    }

    private static StringBuilder tabs(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append("\t");
        }

        return sb;
    }

    private static String lineEnding(boolean comma) {
        StringBuilder s = new StringBuilder();

        if(comma) s.append(",");
        if(prettyPrint)
            s.append("\n");
        else
            s.append(" ");

        return s.toString();
    }

    private static String startJSON() {
        if(prettyPrint)
            return "{\n";
        else
            return "{ ";
    }

    private static String endJSON() {
        return "}";
    }

    private static String aString(JSONString string) {
        return "\"" + string.getName() + "\": \"" + string.getValue() + "\"";
    }

    private static String anInt(JSONInteger anInt) {
        return "\"" + anInt.getName() + "\": " + anInt.getValue();
    }

    private static String aFloat(JSONFloat aFloat) {
        return "\"" + aFloat.getName() + "\": " + aFloat.getValue();
    }

    private static String aChar(JSONChar aChar) {
        return "\"" + aChar.getName() + "\": '" + aChar.getValue() + "'";
    }

    private static String aLong(JSONLong aLong) {
        return "\"" + aLong.getName() + "\": " + aLong.getValue();
    }

    private static String aByte(JSONByte aByte) {
        return "\"" + aByte.getName() + "\": " + aByte.getValue();
    }

    private static String aBoolean(JSONBoolean aBoolean) {
        return "\"" + aBoolean.getName() + "\": " + aBoolean.getValue();
    }

    private static String aDouble(JSONDouble aDouble) {
        return "\"" + aDouble.getName() + "\": " + aDouble.getValue();
    }

    private static String aShort(JSONShort aShort) {
        return "\"" + aShort.getName() + "\": " + aShort.getValue();
    }

    private static String anObject(JSONObject anObject, int tabs) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\"").append(anObject.getName()).append("\": {").append(lineEnding(false));

        int numTabs = tabs + 1;
        for(int i = 0; i < anObject.getFields().length; i++) {
            JSONField field = anObject.getFields()[i];
            boolean isLastField = anObject.getFields().length - 1 == i;

            if(field instanceof JSONString) {
                stringBuilder.append(tabs(numTabs + 1)).append(aString((JSONString) field)).append(lineEnding(!isLastField));
            }
            if(field instanceof JSONInteger) {
                stringBuilder.append(tabs(numTabs + 1)).append(anInt((JSONInteger) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONFloat) {
                stringBuilder.append(tabs(numTabs + 1).append(aFloat((JSONFloat) field)).append(lineEnding(!isLastField)));
            }
            else if(field instanceof JSONDouble) {
                stringBuilder.append(tabs(numTabs + 1)).append(aDouble((JSONDouble) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONLong) {
                stringBuilder.append(tabs(numTabs + 1)).append(aLong((JSONLong) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONBoolean) {
                stringBuilder.append(tabs(numTabs + 1)).append(aBoolean((JSONBoolean) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONByte) {
                stringBuilder.append(tabs(numTabs + 1)).append(aByte((JSONByte) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONShort) {
                stringBuilder.append(tabs(numTabs + 1)).append(aShort((JSONShort) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONChar) {
                stringBuilder.append(tabs(numTabs + 1)).append(aChar((JSONChar) field)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONObject) {
                stringBuilder.append(tabs(numTabs + 1)).append(anObject((JSONObject) field,
                        numTabs)).append(lineEnding(!isLastField));
            }
            else if(field instanceof JSONArray) {
                //TODO: Array support
            }
        }

        stringBuilder.append(tabs(numTabs)).append("}");

        return stringBuilder.toString();
    }
}
