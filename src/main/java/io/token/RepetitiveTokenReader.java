package io.token;

import java.util.Scanner;

/**
 * @author Juyas
 * @version 08.12.2021
 * @since 08.12.2021
 */
public class RepetitiveTokenReader extends TokenReader {

    public static final int NO_LIMIT = -1;

    protected final int limit;

    public RepetitiveTokenReader(String targetType, String pattern, int optionalPrio, int limit) {
        super(targetType, pattern, optionalPrio);
        this.limit = limit;
    }

    public RepetitiveTokenReader(String targetType, String target, int limit) {
        this(targetType, target, 0, limit);
    }

    @Override
    public Token read(Scanner scanner) {
        StringBuilder builder = new StringBuilder();
        int amount = 0;
        while (canRead(scanner) && (amount <= limit || limit < 0)) {
            builder.append(scanner.next(target));
            amount++;
        }
        return new Token(targetType, builder.toString());
    }

}