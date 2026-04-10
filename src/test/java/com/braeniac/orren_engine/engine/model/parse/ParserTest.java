package com.braeniac.orren_engine.engine.model.parse;

import com.braeniac.orren_engine.engine.AST.ASTCommandSequence;
import com.braeniac.orren_engine.engine.AST.ASTCommand;
import com.braeniac.orren_engine.engine.AST.ASTNounPhrase;
import com.braeniac.orren_engine.engine.lex.Lexer;
import com.braeniac.orren_engine.engine.lex.Token;
import com.braeniac.orren_engine.engine.parse.ParseException;
import com.braeniac.orren_engine.engine.parse.Parser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private final Lexer lexer = new Lexer();

    @Test
    void shouldParseSimpleVerbOnlyCommand() {
        String input = "sense";
        List<Token> tokens = lexer.lex(input);
        Parser parser = new Parser(tokens, 0);
        ASTCommandSequence ast = parser.parseCommandSequence();

        assertEquals(1, ast.getCommands().size());

        ASTCommand command = ast.getCommands().get(0);
        assertEquals("sense", command.getVerb());
        assertNull(command.getDirectObjects());
        assertNull(command.getQuotedText());
        assertTrue(command.getPrepPhrases().isEmpty());
        assertNull(command.getAdverb());
    }

    @Test
    void shouldParseVerbAndSimpleObject() {
        String input = "pick up lantern";
        List<Token> tokens = lexer.lex(input);
        Parser parser = new Parser(tokens, 0);

        ASTCommandSequence ast = parser.parseCommandSequence();

        assertEquals(1, ast.getCommands().size());

        ASTCommand command = ast.getCommands().get(0);
        assertEquals("take", command.getVerb());
        assertEquals("lantern", command.getDirectObjects().getHead());
        assertNull(command.getQuotedText());
        assertTrue(command.getPrepPhrases().isEmpty());
        assertNull(command.getAdverb());
    }

    @Test
    void shouldParseNounPhrases() {
        String input = "take the small bronze key";
        List<Token> tokens = lexer.lex(input);
        //tokens.forEach(System.out::println);
        Parser parser = new Parser(tokens, 0);

        ASTCommandSequence ast = parser.parseCommandSequence();
        //System.out.println(ast);
        assertEquals(1, ast.getCommands().size());

        ASTCommand command = ast.getCommands().get(0);
        assertEquals("take", command.getVerb());
        assertEquals("the", command.getDirectObjects().getDeterminer());
        assertEquals("small", command.getDirectObjects().getDescriptors().get(0));
        assertEquals("bronze", command.getDirectObjects().getDescriptors().get(1));
        assertEquals("key", command.getDirectObjects().getHead());
    }


    @Test
    void shouldParseWithQuote() {
        String input = "say \"open the gate\"";
        List<Token> tokens = lexer.lex(input);
        Parser parser = new Parser(tokens, 0);

        ASTCommandSequence ast = parser.parseCommandSequence();

        ASTCommand command = ast.getCommands().get(0);
        assertEquals("say", command.getVerb());
        assertEquals("open the gate", command.getQuotedText());
        assertNull(command.getDirectObjects());
        assertTrue(command.getPrepPhrases().isEmpty());
        assertNull(command.getAdverb());
    }

    @Test
    void shouldParseWithDirections() {
        String input = "move north";
        List<Token> tokens = lexer.lex(input);
        Parser parser = new Parser(tokens, 0);
        ASTCommandSequence ast = parser.parseCommandSequence();

        ASTCommand command = ast.getCommands().get(0);
        assertEquals("go", command.getVerb());
        ASTNounPhrase direction = command.getDirectObjects();
        assertEquals("north", direction.getHead());
    }

    @Test
    void shouldParseWithDirectionsTwo() {
        String input = "north";
        List<Token> tokens = lexer.lex(input);
        Parser parser = new Parser(tokens, 0);
        ASTCommandSequence ast = parser.parseCommandSequence();

        ASTCommand command = ast.getCommands().get(0);
        assertEquals("go", command.getVerb());
        ASTNounPhrase direction = command.getDirectObjects();
        assertEquals("north", direction.getHead());
    }

    @Test
    void shouldWorkWithMultipleCommands() {
        String input = "take key, open door, go north";
        List<Token> tokens = lexer.lex(input);
//        tokens.forEach(System.out::println);

        Parser parser = new Parser(tokens, 0);
        ASTCommandSequence ast = parser.parseCommandSequence();

//        System.out.println(ast);

        assertEquals(3, ast.getCommands().size());

        ASTCommand commandA = ast.getCommands().get(0);
        assertEquals("take", commandA.getVerb());
        assertEquals("key", commandA.getDirectObjects().getHead());

        ASTCommand commandB = ast.getCommands().get(1);
        assertEquals("open", commandB.getVerb());
        assertEquals("door", commandB.getDirectObjects().getHead());

        ASTCommand commandC = ast.getCommands().get(2);
        assertEquals("go", commandC.getVerb());
        assertEquals("north", commandC.getDirectObjects().getHead());

    }

    @Test
    void shouldThrowWhenSpeechCommandHasNoQuote() {

        String input = "say lantern";
        List<Token> tokens = lexer.lex(input);
        Parser parser = new Parser(tokens, 0);

        ParseException ex = assertThrows(
                ParseException.class,
                () -> parser.parseCommandSequence()
        );

        assertTrue(ex.getMessage().contains("Expected quoted text"));
    }

    @Test
    void shouldThrowWhenInputStartsWithSeparator() {

        String input = "and take lantern";
        List<Token> tokens = lexer.lex(input);
        Parser parser = new Parser(tokens, 0);

        ParseException ex = assertThrows(
                ParseException.class,
                () -> parser.parseCommandSequence()
        );

        assertTrue(ex.getMessage().contains("Expected a verb"));
    }




}


