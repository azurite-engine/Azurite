package io.xml;

import util.Pair;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import static io.xml.XMLTokenType.*;

/**
 * @version 24.07.2022
 * @since 24.07.2022
 */
public class XML {

    protected static Charset readHeader(byte[] input) {
        String data = new String(input, StandardCharsets.UTF_8);
        if (!data.contains("<?") || !data.contains("?>")) return StandardCharsets.UTF_8;
        String key = "encoding=\"";
        data = data.substring(data.indexOf(key) + key.length());
        data = data.substring(0, data.indexOf("\""));
        return Charset.isSupported(data) ? Charset.forName(data) : StandardCharsets.UTF_8;
    }

    protected static List<XMLToken> tokenize(String input) {
        //remove header data before tokenizing
        int index = input.lastIndexOf("?>");
        input = index > 0 ? input.substring(index + 2).trim() : input;

        char[] chars = input.toCharArray();
        XMLTokenType[] types = new XMLTokenType[input.length()];
        for (int i = 0; i < input.length(); i++) {
            switch (chars[i]) {
                case '<':
                    types[i] = OPEN_TAG;
                    break;
                case '>':
                    types[i] = CLOSE_TAG;
                    break;
                case '!':
                    types[i] = OPEN_COMMENT;
                    break;
                case '-':
                    types[i] = COMMENT_TAG;
                    break;
                case '=':
                    types[i] = ATTR_EQUALS;
                    break;
                case '/':
                    types[i] = SLASH;
                    break;
                case '"':
                    types[i] = QUOTATION;
                    break;
                default:
                    if (Character.isSpaceChar(chars[i]) || Character.isWhitespace(chars[i])) types[i] = SPACING;
                    else types[i] = IDENTIFIER;
                    break;
            }
        }

        List<XMLToken> tokens = new LinkedList<>();
        StringBuilder builder = null;
        boolean identifier = false;
        for (int i = 0; i < types.length; i++) {
            switch (types[i]) {

                case OPEN_TAG:
                case SLASH:
                case CLOSE_TAG:
                case ATTR_EQUALS:
                case QUOTATION:
                case OPEN_COMMENT:
                case COMMENT_TAG:
                    if (builder != null) {
                        if (identifier)
                            tokens.add(new XMLToken(builder.toString(), IDENTIFIER));
                        else tokens.add(new XMLToken(builder.toString(), SPACING));
                        builder = null;
                        identifier = false;
                    }
                    tokens.add(new XMLToken(String.valueOf(chars[i]), types[i]));
                    break;
                case IDENTIFIER:
                    if (builder != null && !identifier) {
                        tokens.add(new XMLToken(builder.toString(), SPACING));
                        builder = null;
                    }
                    if (builder == null) {
                        builder = new StringBuilder().append(chars[i]);
                        identifier = true;
                    } else builder.append(chars[i]);
                    break;
                case SPACING:
                    if (builder != null && identifier) {
                        tokens.add(new XMLToken(builder.toString(), IDENTIFIER));
                        builder = null;
                        identifier = false;
                    }
                    if (builder == null)
                        builder = new StringBuilder().append(chars[i]);
                    else builder.append(chars[i]);
                    break;
            }
        }
        return tokens;
    }

    public static String transformValue(String value, boolean isRaw) {
        if (isRaw) {
            //make value ready to read
            return value.replace("&amp;", "&")
                    .replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&apos;", "'")
                    .replace("&quot;", "\"");
        } else {
            //make value ready to write
            return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("'", "&apos;")
                    .replace("\"", "&quot;");
        }
    }

    public static XMLElement parse(String input) {
        List<XMLToken> tokens = tokenize(input);
        Pair<XMLElement, Integer> parse = parse(tokens, 0);
        return parse.getLeft();
    }

    public static XMLElement parse(byte[] input, Charset charset) {
        return parse(new String(input, charset));
    }

    public static XMLElement parse(byte[] input) {
        return parse(new String(input, readHeader(input)));
    }

    public static Pair<XMLElement, Integer> parse(List<XMLToken> tokens, int startingPoint) {
        XMLElement element = null;
        String state = "none";
        StringBuilder valueBuilder = new StringBuilder();

        String lastAttrName = null;
        int i = startingPoint;
        while (i < tokens.size()) {
            switch (tokens.get(i).getTokenType()) {

                case OPEN_TAG:
                    if (state.equals("none"))
                        state = "opentag";
                    else if (state.equals("block")) {
                        if (tokens.get(i + 1).getTokenType() == XMLTokenType.SLASH) {
                            //close tag
                            state = "endtag";
                        } else {
                            //new sub element
                            Pair<XMLElement, Integer> sub = parse(tokens, i);
                            element.addSubElement(sub.getLeft());
                            i = sub.getRight();
                        }
                    } else if (state.equals("blockvalue")) {
                        state = "endtag"; //end of the block value
                        element.setValue(transformValue(valueBuilder.toString(), true));
                    }
                    break;
                case SLASH:
                    if (state.equals("value") || state.equals("blockvalue") || state.equals("comment"))
                        valueBuilder.append(tokens.get(i).getValue());
                    else if (state.equals("tag") || state.equals("endtag")) state = "slash";
                    break;
                case CLOSE_TAG:
                    if (state.equals("slash")) {
                        //closing the tag means the end of this algorithm
                        return new Pair<>(element, i);
                    } else if (state.equals("tag")) {
                        //if a tag gets close without slash, it's the beginning of a block
                        state = "block";
                    } else if (state.equals("comment")) {
                        // new comment
                        element = new XMLElement(valueBuilder.toString());
                        return new Pair<>(element, i);
                    } else if (state.equals("tagname")) {
                        //opening block immediately
                        state = "block";
                        element = new XMLElement(valueBuilder.toString(), (String) null);
                        valueBuilder = new StringBuilder();
                    }
                    break;
                case ATTR_EQUALS:
                    if (state.equals("attr")) {
                        state = "quot";
                        lastAttrName = valueBuilder.toString();
                        valueBuilder = new StringBuilder();
                    } else valueBuilder.append(tokens.get(i).getValue());
                    break;
                case QUOTATION:
                    if (state.equals("quot")) {
                        state = "value";
                        valueBuilder = new StringBuilder();
                    } else if (state.equals("value")) {
                        element.addAttribute(lastAttrName, transformValue(valueBuilder.toString(), true));
                        lastAttrName = null;
                        valueBuilder = new StringBuilder();
                        state = "tag";
                    } else if (state.equals("comment") || state.equals("blockvalue"))
                        valueBuilder.append(tokens.get(i).getValue());
                    break;
                case OPEN_COMMENT:
                    if (state.equals("opentag"))
                        state = "opencomment";
                    else if (state.equals("value") || state.equals("blockvalue") || state.equals("comment"))
                        valueBuilder.append(tokens.get(i).getValue());
                    break;
                case COMMENT_TAG:
                    //after an open comment tag, a comment is to be expected
                    if (state.equals("opencomment"))
                        state = "commenttag";
                        //if there is the second tag, the comment starts
                    else if (state.equals("commenttag")) {
                        state = "comment";
                    } else if (!state.equals("comment")) valueBuilder.append(tokens.get(i).getValue());
                    break;
                case IDENTIFIER:
                    if (state.equals("opentag")) {
                        //starting the name of the tag
                        state = "tagname";
                        valueBuilder.append(tokens.get(i).getValue());
                    } else if (state.equals("tagname") || state.equals("attr") || state.equals("comment")
                            || state.equals("value") || state.equals("blockvalue")) {
                        //continue the name
                        valueBuilder.append(tokens.get(i).getValue());
                    } else if (state.equals("tag")) {
                        //inside the tag starting a new attribute
                        state = "attr";
                        valueBuilder.append(tokens.get(i).getValue());
                    } else if (state.equals("block")) {
                        state = "blockvalue";
                        valueBuilder.append(tokens.get(i).getValue());
                    }
                    break;
                case SPACING:
                    if (state.equals("block")) {
                        if (tokens.get(i + 1).getTokenType() == XMLTokenType.IDENTIFIER) {
                            state = "blockvalue";
                            valueBuilder.append(tokens.get(i).getValue());
                        }
                    } else if (state.equals("value") || state.equals("blockvalue") || state.equals("comment")) {
                        valueBuilder.append(tokens.get(i).getValue());
                    } else if (state.equals("tagname")) {
                        state = "tag";
                        element = new XMLElement(valueBuilder.toString(), (String) null);
                        valueBuilder = new StringBuilder();
                    }
                    break;
            }
            i++;
        }
        //some error here
        throw new XMLSyntaxException("End of file reached without closing tag");
    }

}