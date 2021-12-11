package io.token;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Juyas
 * @version 08.12.2021
 * @since 08.12.2021
 */
public class TokenReader implements Comparable<TokenReader> {

    protected final int optionalPrio;
    protected final Pattern target;
    protected final String targetType;

    public TokenReader(String targetType, String pattern, int optionalPrio) {
        this.target = Pattern.compile(pattern);
        this.optionalPrio = optionalPrio;
        this.targetType = targetType;
    }

    public TokenReader(String targetType, String target) {
        this(targetType, target, 0);
    }

    public boolean canRead(Scanner scanner) {
        return scanner.hasNext(target);
    }

    @Override
    public int compareTo(TokenReader o) {
        return Integer.compare(optionalPrio, o.optionalPrio);
    }

    public Token read(Scanner scanner) {
        return new Token(targetType, scanner.next(target));
    }

    public String type() {
        return targetType;
    }

    @Override
    public String toString() {
        return "read " + targetType;
    }
}