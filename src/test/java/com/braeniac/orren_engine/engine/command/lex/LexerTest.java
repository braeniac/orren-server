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
    void 

    private void assertToken(Token token, TokenType expectedType, String expectedRaw, String expectedNormalized) {
        assertEquals(expectedType, token.getType());
        assertEquals(expectedRaw, token.getRawText());
        assertEquals(expectedNormalized, token.getNormalizedText());
    }
}
