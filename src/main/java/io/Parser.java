package io;

import io.token.Token;
import io.token.TokenReader;

import java.util.List;

/**
 * @author Juyas
 * @version 11.12.2021
 * @since 11.12.2021
 */
public abstract class Parser<T> {

    /**
     * Helper method to get a value in of the token list without going out of bounds.
     *
     * @param tokens the list of tokens
     * @param pos    the position
     * @return the value at the given position in the list or <code>null</code> if the position is out of bounds
     */
    protected String get(List<Token> tokens, int pos) {
        return pos >= tokens.size() ? null : tokens.get(pos).toString();
    }

    /**
     * Match a list of tokens at the given position.
     * Starting at pos, executes {@link this#is(List, int, TokenReader)} to test if all tokens are in the right order.
     *
     * @param tokens  the list of tokens
     * @param pos     the position to start matching
     * @param readers the expected list of tokens by using their {@link TokenReader#type()}
     * @return if the token list matches the expected ones.
     * @see this#is(List, int, TokenReader)
     */
    protected boolean check(List<Token> tokens, int pos, TokenReader... readers) {
        for (int i = pos; i < readers.length + pos; i++) {
            if (!is(tokens, i, readers[i - pos])) return false;
        }
        return true;
    }

    /**
     * Match a token.
     *
     * @param tokens the list of tokens
     * @param pos    the position to match
     * @param reader the expected token to match using its {@link TokenReader#type()}
     * @return true, if and only if the token list has a token at the given position that matches the given type
     */
    protected boolean is(List<Token> tokens, int pos, TokenReader reader) {
        if (pos >= tokens.size()) return false;
        return tokens.get(pos).getType().equals(reader.type());
    }

    /**
     * Parse an object by taking a chronological list of tokens.
     *
     * @param tokens the chronological list of tokens
     * @return the parsed object
     */
    public abstract T parse(List<Token> tokens);

}