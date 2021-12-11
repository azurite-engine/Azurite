package io.xml;

import io.Tokenizer;
import io.token.RepetitiveTokenReader;
import io.token.Token;
import io.token.TokenReader;
import io.token.TokenStream;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import static io.token.TokenStream.*;

/**
 * @see XMLParser
 * @author Juyas
 * @version 11.12.2021
 * @since 11.12.2021
 */
public final class XMLTokenizer extends Tokenizer {

    public static final TokenReader OPEN_TAG = new TokenReader("OPEN_TAG", "<");
    public static final TokenReader CLOSE_TAG = new TokenReader("CLOSE_TAG", ">");
    public static final TokenReader SELF_CLOSE = new TokenReader("SELF_CLOSE", "/");
    public static final TokenReader ATTR_EQUALS = new TokenReader("ATTR_EQUALS", "=");
    public static final TokenReader SPACING = new RepetitiveTokenReader("SPACING", "[\\s\\t\\n\\r]", RepetitiveTokenReader.NO_LIMIT);
    public static final TokenReader VALUE = new RepetitiveTokenReader("VALUE", "[^\"<>&']", RepetitiveTokenReader.NO_LIMIT);
    public static final TokenReader QUOTATION = new TokenReader("QUOTATION", "\"");
    public static final TokenReader COMMENT_MARK = new TokenReader("COMMENT_MARK", "!");
    public static final TokenReader COMMENT_CONTENT = new RepetitiveTokenReader("COMMENT_CONTENT", "--", RepetitiveTokenReader.NO_LIMIT) {
        @Override
        public boolean canRead(Scanner scanner) {
            scanner.useDelimiter(Pattern.compile("\\p{javaWhitespace}+"));
            boolean has = scanner.hasNext(target + ".*");
            scanner.useDelimiter("");
            return !has;
        }

        @Override
        public Token read(Scanner scanner) {
            StringBuilder builder = new StringBuilder();
            while (!scanner.hasNext("-") && scanner.hasNext()) {
                String next = scanner.next();
                builder.append(next);
            }
            return new Token(targetType, builder.toString());
        }

    };
    public static final TokenReader COMMENT_DASHES = new TokenReader("COMMENT_DASHES", "--") {
        @Override
        public boolean canRead(Scanner scanner) {
            scanner.useDelimiter(Pattern.compile("\\p{javaWhitespace}+"));
            boolean has = scanner.hasNext(target + ".*");
            scanner.useDelimiter("");
            return has;
        }

        @Override
        public Token read(Scanner scanner) {
            String dash = scanner.next("-");
            dash += scanner.next("-");
            return new Token(targetType, dash);
        }
    };
    public static final TokenReader IDENTIFIER = new RepetitiveTokenReader("IDENTIFIER", "[a-zA-Z0-9_-]", -1, RepetitiveTokenReader.NO_LIMIT) {
        @Override
        public boolean canRead(Scanner scanner) {
            return scanner.hasNext("[a-zA-Z_]");
        }

        @Override
        public Token read(Scanner scanner) {
            StringBuilder builder = new StringBuilder();
            int amount = 0;
            //first letter must not be a number
            if (canRead(scanner)) {
                builder.append(scanner.next("[a-zA-Z_]"));
                amount++;
            }
            while (scanner.hasNext(target) && (amount <= limit || limit <= 0)) {
                builder.append(scanner.next(target));
                amount++;
            }
            return new Token(targetType, builder.toString());
        }
    };

    @Override
    public List<Token> tokenize(String input) {
        //remove header data before tokenizing
        int index = input.lastIndexOf("?>");
        input = index > 0 ? input.substring(index + 2).trim() : input;

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
        return stream.getHistory();
    }

    @Override
    public Charset detectCharset(byte[] input) {
        return readHeader(input);
    }

    /**
     * Read the encoding as {@link Charset} from a xml header in raw utf-8 bytes.
     *
     * @param input the raw utf-8 input bytes
     * @return the charset as defined in the header; if there is none, it will return {@link StandardCharsets#UTF_8}
     */
    private Charset readHeader(byte[] input) {
        String data = new String(input, StandardCharsets.UTF_8);
        if (!data.contains("<?") || !data.contains("?>")) return StandardCharsets.UTF_8;
        String key = "encoding=\"";
        data = data.substring(data.indexOf(key) + key.length());
        data = data.substring(0, data.indexOf("\""));
        return Charset.isSupported(data) ? Charset.forName(data) : StandardCharsets.UTF_8;
    }

}