package io.token;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * A handy structure to perform the lexical analysis on a given {@link String}.
 * Uses the {@link Scanner} class and {@link TokenReader}s reading character by character to tokenize an input.
 *
 * @author Juyas
 * @version 11.12.2021
 * @see io.Tokenizer
 * @since 08.12.2021
 */
public class TokenStream {

    private final Scanner scanner;
    private final LinkedList<Token> history;

    /**
     * Create a new {@link TokenStream} for a given input {@link String}.
     * Also creates an empty token history list {@link this#getHistory()}
     * and the {@link Scanner} with the single character delimiter.
     *
     * @param input the input {@link String}
     */
    public TokenStream(String input) {
        this.scanner = new Scanner(input);
        this.scanner.useDelimiter("");
        this.history = new LinkedList<>();
    }

    /**
     * Try to read a token using the given {@link TokenReader} based on the current position of the internal {@link Scanner}.
     * This method will read and add a new token to the history, if the {@link TokenReader#canRead(Scanner)} method returns true.
     *
     * @param reader the reader to read the next token
     * @return this stream
     */
    public TokenStream eat(TokenReader reader) {
        if (reader.canRead(scanner)) history.add(reader.read(scanner));
        return this;
    }

    /**
     * Try to read a token if the previous token in history matches the given condition.
     *
     * @param reader        the reader to read the next token
     * @param previousToken the condition for the previous token
     * @return this stream
     * @see this#e(TokenReader)
     * @see this#ne(TokenReader)
     * @see this#eat(TokenReader)
     */
    public TokenStream eatConditionally(TokenReader reader, Predicate<Token> previousToken) {
        return eatHistorically(reader, tokens -> previousToken.test(tokens.isEmpty() ? null : tokens.getLast()));
    }

    /**
     * Try to read a token if all previous tokens in history match the given condition.
     *
     * @param reader        the reader to read the next token
     * @param previousToken the conditions for the previous tokens
     * @return this stream
     */
    public TokenStream eatHistorically(TokenReader reader, Predicate<LinkedList<Token>> previousToken) {
        if (!previousToken.test(history)) return this;
        return eat(reader);
    }

    /**
     * The current token history of this stream.
     * Might produce the result at the end of calculation.
     *
     * @return the current token history list.
     */
    public LinkedList<Token> getHistory() {
        return history;
    }

    /**
     * If there is any token left to read.
     * Is identical to {@link Scanner#hasNext()} for the internal scanner.
     *
     * @return whether there are characters/tokens left to be read
     */
    public boolean isOffering() {
        return scanner.hasNext();
    }

    // ----- Helper methods -----

    /**
     * Used for {@link this#eatHistorically(TokenReader, Predicate)} to create a predicate.
     * Calling this method with A,B,C would result in a predicate that matches if the recent history ends with A,B,C.
     * Where C is the most recent entry - therefore the given tokens get matched with the end of the history list.
     *
     * @param readers the {@link TokenReader}s containing the types to match in history
     * @return whether the expected history is matched - as predicate
     * @see this#eatHistorically(TokenReader, Predicate)
     */
    public static Predicate<LinkedList<Token>> eh(TokenReader... readers) {
        return tokens -> {
            Iterator<Token> itr = tokens.descendingIterator();
            int r = readers.length - 1;
            while (r >= 0) {
                if (!itr.hasNext()) return false;
                String next = itr.next().getType();
                if (!next.equals(readers[r].type())) return false;
                r--;
            }
            return true;
        };
    }

    /**
     * Used for {@link this#eatConditionally(TokenReader, Predicate)} to create a predicate.
     * The predicate will match the given readers type with the last token in the history list.
     * This means the predicate passes, if the latest/most recent eaten token is equal to the given one.
     *
     * @param reader the reader containing the type to match in history
     * @return whether the most recent token matches the given one - as predicate
     * @see this#eatHistorically(TokenReader, Predicate)
     */
    public static Predicate<Token> e(TokenReader reader) {
        return token -> token != null && token.getType().equals(reader.type());
    }

    /**
     * Used for {@link this#eatConditionally(TokenReader, Predicate)} to create a predicate.
     * The predicate will NOT match the given readers type with the last token in the history list.
     * This means the predicate passes, if the latest/most recent eaten token is NOT equal to the given one.
     * This method is mostly the opposite to {@link this#e(TokenReader)}, although this predicate also passes,
     * if there is no previous token at all.
     *
     * @param reader the reader containing the type to match in history
     * @return whether the most recent token does not match the given one - as predicate
     * @see this#eatHistorically(TokenReader, Predicate)
     */
    public static Predicate<Token> ne(TokenReader reader) {
        return token -> token == null || !token.getType().equals(reader.type());
    }

}