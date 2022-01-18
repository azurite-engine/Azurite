package io.xml;

import io.Parser;
import io.token.Token;
import util.Pair;

import java.util.List;

import static io.xml.XMLTokenizer.*;

/**
 * A simple XML string parsing class.
 * Accepts:
 * - spacing at any allowed position
 * - node, sub nodes and attributes inside nodes
 * - self-closing nodes
 * - comments
 * - different encoding in ?xml header
 * Does not accept e.g.:
 * - namespaces
 * - complex ?xml headers
 * - stylesheet
 * - DTD declaration and data
 * - entities
 *
 * @author Juyas
 * @version 11.12.2021
 * @see XMLTokenizer
 * @since 08.12.2021
 */
public final class XMLParser extends Parser<XMLElement> {

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

    @Override
    public XMLElement parse(List<Token> tokens) {
        return parse(tokens, 0).getLeft();
    }

    private Pair<XMLElement, Integer> parse(List<Token> tokens, int pos) {
        XMLElement element = null;
        int ending = 0;
        if (is(tokens, pos, SPACING)) pos++;
        //read comments
        if (check(tokens, pos, OPEN_TAG, COMMENT_MARK, COMMENT_DASHES, COMMENT_CONTENT, COMMENT_DASHES, CLOSE_TAG)) {
            Token commentToken = tokens.get(pos + 3);
            element = new XMLElement(commentToken.getValue());
            ending = pos + 6;
        } else if (check(tokens, pos, OPEN_TAG, IDENTIFIER)) {
            //read start of the element
            element = new XMLElement(tokens.get(pos + 1).getValue(), (String) null);
            pos += 2;
            if (is(tokens, pos, SPACING)) {
                while (check(tokens, pos, SPACING, IDENTIFIER, ATTR_EQUALS, QUOTATION, VALUE, QUOTATION)) {
                    element.addAttribute(tokens.get(pos + 1).getValue(), tokens.get(pos + 4).getValue());
                    pos += 6;
                }
            }
            //header is done
            if (is(tokens, pos, CLOSE_TAG)) {
                pos++;
                String spacing = "";
                if (is(tokens, pos, SPACING)) {
                    spacing = tokens.get(pos).getValue();
                    pos++;
                }
                //value and close tag
                if (check(tokens, pos, VALUE, OPEN_TAG, SELF_CLOSE, IDENTIFIER, CLOSE_TAG)) {
                    element.setValue(transformValue(spacing + tokens.get(pos).getValue(), true));
                    ending = pos + 5;
                } else {
                    int p = pos;
                    while (check(tokens, p, OPEN_TAG, IDENTIFIER) || check(tokens, p, OPEN_TAG, COMMENT_MARK)) {
                        Pair<XMLElement, Integer> parse = parse(tokens, p);
                        element.addSubElement(parse.getLeft());
                        p = parse.getRight();
                        if (is(tokens, p, SPACING)) p++;
                    }
                    if (check(tokens, p - 2, SELF_CLOSE, CLOSE_TAG) || check(tokens, p - 3, SELF_CLOSE, CLOSE_TAG, SPACING)) {
                        ending = p;
                    } else if (check(tokens, p, OPEN_TAG, SELF_CLOSE, IDENTIFIER, CLOSE_TAG)) {
                        if (!tokens.get(p + 2).getValue().equals(element.getTag()))
                            fail("Closing tag doesnt match opening tag: \"" + element.getTag() + "\" <> " + get(tokens, p + 3));
                        ending = p + 4;
                    } else
                        fail("Missing closing tag for \"" + element.getTag() + "\", found " + get(tokens, p) + " at " + p + "/" + tokens.size());
                }
            } else if (check(tokens, pos, SELF_CLOSE, CLOSE_TAG)) {
                ending = pos + 2;
            }
        } else if (tokens.size() > pos) fail("Expected start of a new tag, but got: " + tokens.get(pos));
        return new Pair<>(element, ending);
    }

    private void fail(String msg) {
        throw new XMLSyntaxException(msg);
    }

}