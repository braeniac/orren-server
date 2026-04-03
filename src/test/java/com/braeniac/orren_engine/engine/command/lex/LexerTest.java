package com.braeniac.orren_engine.engine.command.lex;

import com.braeniac.orren_engine.engine.lex.TokenType;
import com.braeniac.orren_engine.engine.lex.Lexer;
import com.braeniac.orren_engine.engine.lex.Token;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;



public class LexerTest {

    private final Lexer lexer = new Lexer();

    @Test
    void shouldLexSimpleVerbAndObject() {
        List<Token> tokens = lexer.lex("take lantern");
        assertEquals(3, tokens.size());

        assertToken(tokens.get(0), TokenType.VERB, "take", "take");
        assertToken(tokens.get(1), TokenType.WORD, "lantern", "lantern");
        assertToken(tokens.get(2), TokenType.EOF, "", "");
    }

    @Test
    void shouldLexSimpleVerbWithSpace() {

        List<Token> tokens = lexer.lex("pick up lantern");
        assertEquals(3, tokens.size());

        assertToken(tokens.get(0), TokenType.VERB, "pick up", "take");
        assertToken(tokens.get(1), TokenType.WORD, "lantern", "lantern");
        assertToken(tokens.get(2), TokenType.EOF, "", "");
    }

    @Test
    void shouldLexSimpleDirections() {
        List<Token> tokens = lexer.lex("go s");
        assertEquals(3, tokens.size());

        assertToken(tokens.get(0), TokenType.VERB, "go", "go");
        assertToken(tokens.get(1), TokenType.DIRECTION, "s", "south");
        assertToken(tokens.get(2), TokenType.EOF, "", "");

    }


    @Test
    void shouldLexSimpleVerbWithQuote() {
        List<Token> tokens = lexer.lex("say \"open the gate\"");
        assertEquals(3, tokens.size());

        assertToken(tokens.get(0), TokenType.VERB, "say", "say");
        assertToken(tokens.get(1), TokenType.QUOTED_TEXT, "\"open the gate\"", "open the gate");
        assertToken(tokens.get(2), TokenType.EOF, "", "");
    }

    @Test
    void shouldLexSimpleCommandWithPrep() {
        List<Token> tokens = lexer.lex("put lantern on table");
        assertEquals(5, tokens.size());

        assertToken(tokens.get(0), TokenType.VERB, "put", "put");
        assertToken(tokens.get(1), TokenType.WORD, "lantern", "lantern");
        assertToken(tokens.get(2), TokenType.PREP, "on", "on");
        assertToken(tokens.get(3), TokenType.WORD, "table", "table");
        assertToken(tokens.get(4), TokenType.EOF, "", "");
    }

    @Test
    void shouldLexSimpleCommandWithSeparator() {
        List<Token> tokens = lexer.lex("take key, open the door");
        assertEquals(7, tokens.size());

        assertToken(tokens.get(0), TokenType.VERB, "take", "take");
        assertToken(tokens.get(1), TokenType.WORD, "key", "key");
        assertToken(tokens.get(2), TokenType.SEPARATOR, ",", ",");
        assertToken(tokens.get(3), TokenType.VERB, "open", "open");
        assertToken(tokens.get(4), TokenType.DET, "the", "the");
        assertToken(tokens.get(5), TokenType.WORD, "door", "door");
        assertToken(tokens.get(6), TokenType.EOF, "", "");
    }


    @Test
    void shouldLexComplexUserCommand() {
        List<Token> tokens = lexer.lex("grab my lantern; open the wooden door, then whisper \"hello\" quietly");

        assertEquals(14, tokens.size());
        assertToken(tokens.get(0), TokenType.VERB, "grab", "take");
        assertToken(tokens.get(1), TokenType.DET, "my", "my");
        assertToken(tokens.get(2), TokenType.WORD, "lantern", "lantern");
        assertToken(tokens.get(3), TokenType.SEPARATOR, ";", ";");
        assertToken(tokens.get(4), TokenType.VERB, "open", "open");
        assertToken(tokens.get(5), TokenType.DET, "the", "the");
        assertToken(tokens.get(6), TokenType.WORD, "wooden", "wooden");
        assertToken(tokens.get(7), TokenType.WORD, "door", "door");
        assertToken(tokens.get(8), TokenType.SEPARATOR, ",", ",");
        assertToken(tokens.get(9), TokenType.SEPARATOR, "then", "then");
        assertToken(tokens.get(10), TokenType.VERB, "whisper", "whisper");
        assertToken(tokens.get(11), TokenType.QUOTED_TEXT, "\"hello\"", "hello");
        assertToken(tokens.get(12), TokenType.ADV, "quietly", "quietly");
        assertToken(tokens.get(13), TokenType.EOF, "", "");

    }

    @Test
    void shouldLexBlankCommand() {
        List<Token> tokens = lexer.lex("");

        assertEquals(2, tokens.size());
        assertToken(tokens.get(0), TokenType.EOF, "", "");
        assertToken(tokens.get(1), TokenType.EOF, "", "");
    }



    private void assertToken(Token token, TokenType expectedType, String expectedRaw, String expectedNormalized) {
        assertEquals(expectedType, token.getType());
        assertEquals(expectedRaw, token.getRawText());
        assertEquals(expectedNormalized, token.getNormalizedText());
    }
}
