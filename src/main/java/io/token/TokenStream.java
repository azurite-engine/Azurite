package io.token;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * @author Juyas
 * @version 08.12.2021
 * @since 08.12.2021
 */
public class TokenStream {

    private final Scanner scanner;
    private final LinkedList<Token> history;

    public TokenStream(String input) {
        this.scanner = new Scanner(input);
        this.scanner.useDelimiter("");
        this.history = new LinkedList<>();
    }

    public TokenStream eat(TokenReader reader) {
        if (reader.canRead(scanner)) history.add(reader.read(scanner));
        return this;
    }

    public TokenStream eatConditionally(TokenReader reader, Predicate<Token> previousToken) {
        return eatHistorically(reader, tokens -> previousToken.test(tokens.isEmpty() ? null : tokens.getLast()));
    }

    public TokenStream eatHistorically(TokenReader reader, Predicate<LinkedList<Token>> previousToken) {
        if (!previousToken.test(history)) return this;
        return eat(reader);
    }

    public LinkedList<Token> getHistory() {
        return history;
    }

    public boolean isOffering() {
        return scanner.hasNext();
    }

}