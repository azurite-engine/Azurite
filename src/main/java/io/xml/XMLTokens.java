package io.xml;

import io.token.RepetitiveTokenReader;
import io.token.Token;
import io.token.TokenReader;

import java.util.Scanner;

/**
 * @author Juyas
 * @version 08.12.2021
 * @since 08.12.2021
 */
public class XMLTokens {

    public static TokenReader OPEN_TAG = new TokenReader("OPEN_TAG", "<");
    public static TokenReader CLOSE_TAG = new TokenReader("CLOSE_TAG", ">");
    public static TokenReader SELF_CLOSE = new TokenReader("SELF_CLOSE", "/");
    public static TokenReader ATTR_EQUALS = new TokenReader("ATTR_EQUALS", "=");
    public static TokenReader SPACING = new RepetitiveTokenReader("SPACING", "[\\s\\t\\n\\r]", RepetitiveTokenReader.NO_LIMIT);
    public static TokenReader VALUE = new RepetitiveTokenReader("VALUE", "[^\"<>&']", RepetitiveTokenReader.NO_LIMIT);
    public static TokenReader QUOTATION = new TokenReader("QUOTATION", "\"");
    public static TokenReader COMMENT_MARK = new TokenReader("COMMENT_MARK", "!");
    public static TokenReader COMMENT_CONTENT = new RepetitiveTokenReader("COMMENT_CONTENT", "--", RepetitiveTokenReader.NO_LIMIT) {
        @Override
        public boolean canRead(Scanner scanner) {
            scanner.useDelimiter(" ");
            boolean has = scanner.hasNext(target + ".*");
            scanner.useDelimiter("");
            return !has;
        }

        @Override
        public Token read(Scanner scanner) {
            StringBuilder builder = new StringBuilder();
            int amount = 0;
            boolean c = false;
            while (!(c && scanner.hasNext("-")) && scanner.hasNext() && (amount <= limit || limit < 0)) {
                String next = scanner.next();
                builder.append(next);
                c = next.equals("-");
                amount++;
            }
            return new Token(targetType, builder.toString());
        }

    };
    public static TokenReader COMMENT_DASHES = new TokenReader("COMMENT_DASHES", "--") {
        @Override
        public boolean canRead(Scanner scanner) {
            scanner.useDelimiter(" ");
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
    public static TokenReader IDENTIFIER = new RepetitiveTokenReader("IDENTIFIER", "[a-zA-Z0-9_]", -1, RepetitiveTokenReader.NO_LIMIT) {
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

}