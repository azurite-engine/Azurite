package io.token;

/**
 * @author Juyas
 * @version 08.12.2021
 * @since 08.12.2021
 */
public class Token {

    private final String type;
    private final String value;

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "t='" + type + '\'' +
                ",v='" + value.replace("\n", "{\\n}") + '\'' +
                '}';
    }
}