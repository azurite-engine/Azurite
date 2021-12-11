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

    protected String get(List<Token> tokens, int pos) {
        return pos >= tokens.size() ? null : tokens.get(pos).toString();
    }

    protected boolean check(List<Token> tokens, int pos, TokenReader... readers) {
        for (int i = pos; i < readers.length + pos; i++) {
            if (!is(tokens, i, readers[i - pos])) return false;
        }
        return true;
    }

    protected boolean is(List<Token> tokens, int pos, TokenReader reader) {
        if (pos >= tokens.size()) return false;
        return tokens.get(pos).getType().equals(reader.type());
    }

    public abstract T parse(List<Token> tokens);

}