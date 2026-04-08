package com.braeniac.orren_engine.engine.parse;

import com.braeniac.orren_engine.engine.AST.ASTCommand;
import com.braeniac.orren_engine.engine.AST.ASTCommandSequence;
import com.braeniac.orren_engine.engine.AST.ASTNounPhrase;
import com.braeniac.orren_engine.engine.AST.ASTPrepPhrase;
import com.braeniac.orren_engine.engine.lex.Token;
import com.braeniac.orren_engine.engine.lex.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * EXAMPLE:
 *
 * User command: Take the small bronze key from the old chest
 *
 * verb = TAKE
 * directObject =
 *            determiner = THE
 *            descriptor = [SMALL, BRONZE]
 *            head =  KEY
 * prepPhrase =
 *            FROM
 *            nounPhrase = THE OLD CHEST
 *
 */


public class Parser {

    private List<Token> tokens;
    private int current;

    public Parser(List<Token> tokens, int current) {
        this.tokens = List.copyOf(Objects.requireNonNull(tokens));
        this.current = 0;
    }

    public ASTCommandSequence parseCommandSequence() {
        List<ASTCommand> commands = new ArrayList<>();
        commands.add(parseCommand());

        while(match(TokenType.SEPARATOR)) {
            if (check(TokenType.EOF)) break;
            commands.add(parseCommand());
        }

        consume(TokenType.EOF, "Expected end of input.");
        return new ASTCommandSequence(commands);
    }

    private ASTCommand parseCommand() {

        // handles direction like: go south, go s.
        if (match(TokenType.DIRECTION)) {
            String direction = previous().getNormalizedText();
            return new ASTCommand(
                    "go", // implicit verb
                    new ASTNounPhrase(
                            null,
                            List.of(),
                            direction,
                            null,
                            List.of()
                    ),
                    null,
                    List.of(),
                    null
            );
        }

        Token verbToken = consume(TokenType.VERB, "Expected a verb");
        String verb = verbToken.getNormalizedText();

        // is this speech: say, whisper, yell, shout etc...
        if (isSpeechVerb(verb)) {
            Token quoted = consume(TokenType.QUOTED_TEXT, "Expected quoted text after speech verb.");
            return new ASTCommand(
                    verb,
                    null,
                    quoted.getNormalizedText(),
                    List.of(),
                    null
            );
        }

        ASTNounPhrase directObjects = null;
        List<ASTPrepPhrase> postModifiers = new ArrayList<>();
        String adverb = null;

        if (startNounPhrase()) {
            directObjects = parseNounPhrase();
        }

        while (check(TokenType.PREP)) {
            postModifiers.add(parsePrepPhrase());
        }

        if (match(TokenType.ADV)) {
            adverb = previous().getNormalizedText();
        }

        return new ASTCommand(
                verb,
                directObjects,
                null,
                postModifiers,
                adverb
        );

    }

    private ASTNounPhrase parseNounPhrase() {
        //pronoun
        if (match(TokenType.PRONOUN)) {
            return new ASTNounPhrase(
                    null,
                    List.of(),
                    null,
                    previous().getNormalizedText(),
                    List.of()
            );
        }

        //direction
        if (match(TokenType.DIRECTION)) {
            return new ASTNounPhrase(
                    null,
                    List.of(),
                    previous().getNormalizedText(),
                    null,
                    List.of()
            );
        }

        String determiner = null;
        if (match(TokenType.DET)) {
            determiner = previous().getNormalizedText();
        }

        List<String> words = new ArrayList<>();
        while (match(TokenType.WORD)) {
            words.add(previous().getNormalizedText());
        }

        if (words.isEmpty()) {
            throw error(peek(), "Expected noun phrase.");
        }

        String head = words.get(words.size() - 1);
        List<String> descriptors = words.subList(0, words.size() - 1);

        List<ASTPrepPhrase> postModifiers = new ArrayList<>();
        while (check(TokenType.PREP)) {
            postModifiers.add(parsePrepPhrase());
        }

        return new ASTNounPhrase(
                determiner,
                descriptors,
                head,
                null,
                postModifiers
        );
    }

    private ASTPrepPhrase parsePrepPhrase() {
        Token prep = consume(TokenType.PREP, "Expected preposition.");
        ASTNounPhrase np = parseNounPhrase();
        return new ASTPrepPhrase(prep.getNormalizedText(), np);
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return type == TokenType.EOF;
        }
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean startNounPhrase() {
        return check(TokenType.DET)
                || check(TokenType.WORD)
                || check(TokenType.PRONOUN)
                || check(TokenType.DIRECTION);
    }

    private boolean match(TokenType type) {
        if (!check(type)) {
            return false;
        }
        advance();
        return true;
    }

    private boolean isSpeechVerb(String verb) {
        return verb.equals("say")
                || verb.equals("yell")
                || verb.equals("whisper");
    }

    private ParseException error(Token token, String message) {
        return new ParseException(
                message + " Found token: " + token
        );
    }




}
