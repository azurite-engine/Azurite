package io.xml;

/**
 * @version 24.07.2022
 * @since 24.07.2022
 */
public class XMLToken {

    private final String value;

    private final XMLTokenType tokenType;

    public XMLToken(String value, XMLTokenType tokenType) {
        this.value = value;
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    public XMLTokenType getTokenType() {
        return tokenType;
    }

    @Override
    public String toString() {
        return "Token{" +
                "v='" + value + '\'' +
                ",t=" + tokenType +
                '}';
    }
}