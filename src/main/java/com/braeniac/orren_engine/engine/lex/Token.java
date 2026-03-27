package com.braeniac.orren_engine.engine.lex;
import java.util.Objects;

/**
 * This class is designed to represent one lexical token produced by the lexer.

 * Command: "take the small bronze key"

 * token ->
 *      verb   : "take"
 *      det    : "the"
 *      adj    : "small"
 *      adj    : "bronze"
 *      noun   : "key"
 */

public class Token {
    private final TokenType type;
    //preserve what the user typed.
    private final String rawText;
    //generalize commands
    private final String normalizedText;
    private final int startIndex;
    private final int endIndex;

    public Token(
            TokenType type,
            String rawText,
            String normalizedText,
            int startIndex,
            int endIndex) {
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.rawText = Objects.requireNonNull(rawText, "rawText must not be null");
        this.normalizedText = Objects.requireNonNull(normalizedText, "normalizedText must not be null");
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    } //constructor

    public TokenType getType() { return type; }

    public String getRawText() { return rawText; }

    public String getNormalizedText() { return normalizedText; }

    public int getStartIndex() { return startIndex; }

    public int getEndIndex() { return endIndex; }

    //Helper for parser/debugging.
    public boolean isType(TokenType expectedType) {
        return this.type == expectedType;
    }

    //Helper: for matching normaized text.
    public boolean hasNormalizedText(String expectedText) {
        return this.normalizedText.equals(expectedText);
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", rawText='" + rawText + '\'' +
                ", normalizedText='" + normalizedText + '\'' +
                ", startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return startIndex == token.startIndex && endIndex == token.endIndex && type == token.type && Objects.equals(rawText, token.rawText) && Objects.equals(normalizedText, token.normalizedText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, rawText, normalizedText, startIndex, endIndex);
    }
}
