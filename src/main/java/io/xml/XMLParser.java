package io.xml;

import io.token.Token;
import io.token.TokenReader;
import io.token.TokenStream;
import util.Pair;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static io.xml.XMLTokens.*;

/**
 * @author Juyas
 * @version 08.12.2021
 * @since 08.12.2021
 */
public class XMLParser {

    private final String input;
    private List<Token> tokens;

    private XMLParser(String input) {
        int index = input.lastIndexOf("?>");
        this.input = index > 0 ? input.substring(index + 2).trim() : input;
        this.tokens = null;
    }

    public static Charset readHeader(byte[] input) {
        String data = new String(input, StandardCharsets.UTF_8);
        if (!data.contains("<?") || !data.contains("?>")) return StandardCharsets.UTF_8;
        data = data.replace("<?", "<");
        data = data.replace("?>", "/>");
        data = data.substring(0, data.indexOf(">") + 1);
        XMLElement parse = parse(data);
        String encoding = parse.getAttributes().getOrDefault("encoding", "UTF-8");
        return Charset.isSupported(encoding) ? Charset.forName(encoding) : StandardCharsets.UTF_8;
    }

    public static XMLElement parse(byte[] input) {
        return new XMLParser(new String(input, readHeader(input))).tokenize().parse(0).getLeft();
    }

    public static XMLElement parse(byte[] input, Charset charset) {
        return new XMLParser(new String(input, charset)).tokenize().parse(0).getLeft();
    }

    public static XMLElement parse(String input) {
        return new XMLParser(input).tokenize().parse(0).getLeft();
    }

    private Pair<XMLElement, Integer> parse(int pos) {
        XMLElement element = null;
        int ending = 0;
        if (is(pos, SPACING)) pos++;
        //read comments
        if (check(pos, OPEN_TAG, COMMENT_MARK, COMMENT_DASHES, COMMENT_CONTENT, COMMENT_DASHES, CLOSE_TAG)) {
            Token commentToken = tokens.get(pos + 3);
            element = new XMLElement(commentToken.getValue());
            ending = pos + 6;
        } else if (check(pos, OPEN_TAG, IDENTIFIER)) {
            //read start of the element
            element = new XMLElement(tokens.get(pos + 1).getValue(), (String) null);
            pos += 2;
            if (is(pos, SPACING)) {
                while (check(pos, SPACING, IDENTIFIER, ATTR_EQUALS, QUOTATION, VALUE, QUOTATION)) {
                    element.addAttribute(tokens.get(pos + 1).getValue(), tokens.get(pos + 4).getValue());
                    pos += 6;
                }
            }
            //header is done
            if (is(pos, CLOSE_TAG)) {
                pos++;
                String spacing = "";
                if (is(pos, SPACING)) {
                    spacing = tokens.get(pos).getValue();
                    pos++;
                }
                //value and close tag
                if (check(pos, VALUE, OPEN_TAG, SELF_CLOSE, IDENTIFIER, CLOSE_TAG)) {
                    element.setValue(transformValue(spacing + tokens.get(pos).getValue(), true));
                    ending = pos + 5;
                } else {
                    int p = pos;
                    while (check(p, OPEN_TAG, IDENTIFIER) || check(p, OPEN_TAG, COMMENT_MARK)) {
                        Pair<XMLElement, Integer> parse = parse(p);
                        element.addSubElement(parse.getLeft());
                        p = parse.getRight();
                        if (is(p, SPACING)) p++;
                    }
                    if (check(p - 2, SELF_CLOSE, CLOSE_TAG) || check(p - 3, SELF_CLOSE, CLOSE_TAG, SPACING)) {
                        ending = p;
                    } else if (check(p, OPEN_TAG, SELF_CLOSE, IDENTIFIER, CLOSE_TAG)) {
                        if (!tokens.get(p + 2).getValue().equals(element.getTag()))
                            fail("Closing tag doesnt match opening tag: \"" + element.getTag() + "\" <> " + get(p + 3));
                        ending = p + 4;
                    } else
                        fail("Missing closing tag for \"" + element.getTag() + "\", found " + get(p) + " at " + p + "/" + tokens.size());
                }
            } else if (check(pos, SELF_CLOSE, CLOSE_TAG)) {
                ending = pos + 2;
            }
        } else if (tokens.size() > pos) fail("Expected start of a new tag, but got: " + tokens.get(pos));
        return new Pair<>(element, ending);
    }

    private String get(int pos) {
        return pos >= tokens.size() ? null : tokens.get(pos).toString();
    }

    private void fail(String why) {
        throw new XMLSyntaxException(why);
    }

    private boolean check(int pos, TokenReader... readers) {
        for (int i = pos; i < readers.length + pos; i++) {
            if (!is(i, readers[i - pos])) return false;
        }
        return true;
    }

    private boolean is(int pos, TokenReader reader) {
        if (pos >= tokens.size()) return false;
        return tokens.get(pos).getType().equals(reader.type());
    }

    private XMLParser tokenize() {
        TokenStream stream = new TokenStream(input);
        int count = 0;
        while (stream.isOffering() && count < input.length()) {
            stream.eatConditionally(OPEN_TAG, ne(OPEN_TAG)) //open tags not after open tags
                    .eatConditionally(SELF_CLOSE, e(OPEN_TAG)) //closing slash only after open tag or identifier
                    .eatConditionally(IDENTIFIER, e(OPEN_TAG).or(e(SELF_CLOSE))) //identifier only after open tag or closing slash
                    .eatHistorically(SPACING, eh(OPEN_TAG, IDENTIFIER)) //spacing allowed after identifier in tag
                    .eatHistorically(IDENTIFIER, eh(IDENTIFIER, SPACING).or(eh(QUOTATION, SPACING))) //identifier for attributes
                    .eatConditionally(SELF_CLOSE, e(IDENTIFIER)) //closing slash only after open tag or identifier
                    //attributes
                    .eatHistorically(ATTR_EQUALS, eh(SPACING, IDENTIFIER)) //equals only after identifier inside a tag
                    .eatConditionally(QUOTATION, e(ATTR_EQUALS)) //quotation after equals
                    .eatConditionally(VALUE, e(QUOTATION)) // value after quotation
                    .eatHistorically(QUOTATION, eh(QUOTATION, VALUE)) //quotation at the end of a value
                    .eatHistorically(SPACING, eh(QUOTATION, VALUE, QUOTATION)).eatConditionally(SELF_CLOSE, e(QUOTATION))
                    //end tag
                    .eatConditionally(CLOSE_TAG, e(IDENTIFIER).or(e(QUOTATION)).or(e(SELF_CLOSE))) //close tag after identifier in tag or quotation of attribute
                    .eatHistorically(SPACING, eh(CLOSE_TAG)) //eat spacing after close tag
                    .eatHistorically(VALUE, eh(QUOTATION, CLOSE_TAG).or(eh(OPEN_TAG, IDENTIFIER, CLOSE_TAG))) //value between tags
                    .eatHistorically(VALUE, eh(QUOTATION, CLOSE_TAG, SPACING).or(eh(OPEN_TAG, IDENTIFIER, CLOSE_TAG, SPACING))) //value between tags with spacing
                    //spacing between tags - ignorable spacing
                    .eatHistorically(SPACING, eh(SELF_CLOSE, CLOSE_TAG).or(eh(SELF_CLOSE, IDENTIFIER, CLOSE_TAG))) //spacing after a finished tag
                    //comment block
                    .eatConditionally(COMMENT_MARK, e(OPEN_TAG)) //comment can start after open tag
                    .eatConditionally(COMMENT_DASHES, e(COMMENT_MARK)) //comment dashes can/should follow after a mark
                    .eatHistorically(COMMENT_CONTENT, eh(COMMENT_MARK, COMMENT_DASHES)) //comments can be put after a mark followed by dashes
                    .eatConditionally(COMMENT_DASHES, e(COMMENT_CONTENT)) // comments end with dashes
                    .eatConditionally(CLOSE_TAG, e(COMMENT_DASHES)); //comments end with dashes

            count++;
        }
        this.tokens = stream.getHistory();
        return this;
    }

    private static Predicate<LinkedList<Token>> eh(TokenReader... reader) {
        return tokens -> {
            Iterator<Token> itr = tokens.descendingIterator();
            int r = reader.length - 1;
            while (r >= 0) {
                if (!itr.hasNext()) return false;
                String next = itr.next().getType();
                if (!next.equals(reader[r].type())) return false;
                r--;
            }
            return true;
        };
    }

    private static Predicate<Token> e(TokenReader reader) {
        return token -> token != null && token.getType().equals(reader.type());
    }

    private static Predicate<Token> ne(TokenReader reader) {
        return token -> token == null || !token.getType().equals(reader.type());
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

}