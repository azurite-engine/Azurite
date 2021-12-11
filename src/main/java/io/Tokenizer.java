package io;

import io.token.Token;
import io.token.TokenReader;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Juyas
 * @version 11.12.2021
 * @since 11.12.2021
 */
public abstract class Tokenizer {

    public static Predicate<LinkedList<Token>> eh(TokenReader... reader) {
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

    public static Predicate<Token> e(TokenReader reader) {
        return token -> token != null && token.getType().equals(reader.type());
    }

    public static Predicate<Token> ne(TokenReader reader) {
        return token -> token == null || !token.getType().equals(reader.type());
    }

    /**
     * Reads an input string and extracts all tokens depending on the implementation from it
     *
     * @param input the input string data
     * @return a list of all tokens while retaining its chronological order.
     */
    public abstract List<Token> tokenize(String input);

    /**
     * Read raw input data to determine the charset based on the input - or return a default one.
     *
     * @param input the raw input byte
     * @return the charset somewhere defined in the input data or a default charset
     */
    public abstract Charset detectCharset(byte[] input);

    /**
     * Tokenize data using the defined encoding in the header, or the default according to the {@link FileFormat} if there is none defined
     * and return all tokens.
     *
     * @param input the raw utf-8 input bytes
     * @return a list of all tokens while retaining its chronological order.
     */
    public List<Token> tokenize(byte[] input) {
        return tokenize(input, detectCharset(input));
    }

    /**
     * Tokenize data using the defined encoding and return all tokens.
     *
     * @param input the raw utf-8 input bytes
     * @return a list of all tokens while retaining its chronological order.
     */
    public List<Token> tokenize(byte[] input, Charset charset) {
        return tokenize(new String(input, charset));
    }

}