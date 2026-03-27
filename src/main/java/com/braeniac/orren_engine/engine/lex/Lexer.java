package com.braeniac.orren_engine.engine.lex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Lexer {

    private static final Set<String> VERBS = Set.of(
            "take", "grab", "get", "drop", "put", "throw", "give", "look",
            "examine", "inspect", "search", "go", "walk", "run", "enter", "exit",
            "climb", "open", "close", "lock", "unlock", "use", "push",
            "pull", "turn", "attack", "hit", "kill", "fight", "read",
            "listen", "smell", "touch", "taste", "wear", "remove",
            "inventory", "i", "save", "s", "restore", "quit", "q", "wait", "sleep",
            "sense", "dream", "resonate", "attune", "channel", "hum",
            "echo", "forge", "temper", "bind", "amplify", "dampen", "vibrate",
            "strike", "cool", "heat", "extract", "embed", "shatter", "bend", "break",
            "recall", "forget", "awaken", "meditate", "focus", "align", "rotate",
            "connect", "separate", "pickup", "pick", "move"
    );

    private static final Set<String> PREPOSITIONS = Set.of(
        "with", "to", "at", "on", "in", "into", "from", "using"
    );

    private static final Set<String> DETERMINERS = Set.of(
            "the", "a", "an", "my"
    );

    private static final Set<String> ADVERBS = Set.of(
        "softly", "quickly", "slowly", "carefully", "quietly"
    );

    private static final Set<String> PRONOUNS = Set.of(
        "he", "she", "it", "them"
    );

    private static final Map<String, String> DIRECTIONS = Map.ofEntries(
            Map.entry("north", "north"),
            Map.entry("n", "north"),
            Map.entry("south", "south"),
            Map.entry("s", "south"),
            Map.entry("east", "east"),
            Map.entry("e", "east"),
            Map.entry("west", "west"),
            Map.entry("w", "west"),
            Map.entry("up", "up"),
            Map.entry("u", "up"),
            Map.entry("down", "down"),
            Map.entry("d", "down")
    );

    private static final Map<String, String> NORMALIZED_VERBS = Map.ofEntries(
            Map.entry("grab", "take"),
            Map.entry("pickup", "take"),
            Map.entry("pick", "take"),
            Map.entry("inspect", "look"),
            Map.entry("examine", "look"),
            Map.entry("hit", "strike"),
            Map.entry("fight", "attack"),
            Map.entry("move", "go"),
            Map.entry("i", "inventory"),
            Map.entry("s", "save"),
            Map.entry("q", "quit")
    );

    public List<Token> lex(String input) {

        List<Token> tokens = new ArrayList<>();

        //user input is empty or blank return EOF token
        if (input == null || input.isBlank()) {
            tokens.add(new Token(TokenType.EOF, "", "", 0, 0));
        }

        int i=0;
        while (i < input.length()) {
            char current = input.charAt(i);

            //skip whitespace
            if (Character.isWhitespace(current)) {
                i++;
                continue;
            }

            //is current character a separator: comma or semicolon
            if (current == ',' || current == ';') {
                String symbol = String.valueOf(current);
                tokens.add(new Token(TokenType.SEPARATOR, symbol, symbol, i, i+1));
                i++;
                continue;
            }

            //did we come across quoted text
            if (current == '"') {
                
            }

        }

        return tokens;
    }

}


