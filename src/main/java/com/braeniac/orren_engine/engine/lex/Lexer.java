package com.braeniac.orren_engine.engine.lex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Lexer {

    private static final Set<String> VERBS = Set.of(
            //actions
            "take", "grab", "get", "drop", "put", "throw", "give", "look",
            "examine", "inspect", "search", "go", "walk", "run", "enter", "exit",
            "climb", "open", "close", "lock", "unlock", "use", "push",
            "pull", "turn", "attack", "hit", "kill", "fight", "read",
            "listen", "smell", "touch", "taste", "wear", "remove", "wait",
            "pick", "move",

            //orren specific
            "resonate", "attune", "channel", "hum", "echo",
            "forge", "temper", "bind", "amplify", "dampen", "vibrate",
            "strike", "cool", "heat", "extract", "embed", "shatter", "bend", "break",
            "recall", "forget", "awaken", "meditate", "focus", "align", "rotate",
            "connect", "separate", "dream", "sense", "sleep",

            //speech verbs
            "say", "shout", "whisper", "yell",

            //setting
            "inventory", "i", "save", "restore", "quit", "q"
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

    private static final Map<String, String> NORMALIZED_MULTI_WORD_VERBS = Map.ofEntries(
            Map.entry("pick up", "take"),
            Map.entry("look at", "look"),
            Map.entry("talk to", "talk"),
            Map.entry("turn on", "turn_on"),
            Map.entry("turn off", "turn_off")
    );

    private static final Map<String, String> NORMALIZED_VERBS = Map.ofEntries(
            Map.entry("grab", "take"),
            Map.entry("pick", "take"),
            Map.entry("inspect", "look"),
            Map.entry("examine", "look"),
            Map.entry("hit", "strike"),
            Map.entry("fight", "attack"),
            Map.entry("kill", "attack"),
            Map.entry("move", "go"),
            Map.entry("i", "inventory"),
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
                int start  = i;
                i++; // skip opening quote

                StringBuilder sb = new StringBuilder();
                while (i < input.length() && input.charAt(i) != '"') {
                    sb.append(input.charAt(i));
                    i++;
                }

                if (i >= input.length()) {
                    throw new IllegalArgumentException("Unterminated quoted text starting at index " + start);
                }

                i++; //skip closing quote

                String rawText = input.substring(start, i);
                String normalizedText = sb.toString();

                tokens.add(new Token(
                        TokenType.QUOTED_TEXT,
                        rawText,
                        normalizedText,
                        start,
                        i)
                );
                continue;
            }

            //single or multi-word scanning
            if (Character.isLetter(current)) {

                WordSpan firstWord = readWord(input, i);

                MultiVerbMatch multiWordVerbMatch = tryMatchMultiWordVerb(input, firstWord);
                if (multiWordVerbMatch != null) {
                    tokens.add(new Token(
                        TokenType.VERB,
                            multiWordVerbMatch.rawText(),
                            multiWordVerbMatch.normalizedVerb(),
                            multiWordVerbMatch.startIndex(),
                            multiWordVerbMatch.endIndex()
                    ));
                    i = multiWordVerbMatch.endIndex();
                    continue;
                }

                String rawWord = firstWord.rawWord();
                String lowered = rawWord.toLowerCase();

                Token token = classifySingleWord(rawWord, lowered, firstWord.startIndex(), firstWord.endIndex());
                tokens.add(token);
                i = firstWord.endIndex();
                continue;
            }

            throw new IllegalArgumentException(
                    "Unexpected Character '" + current + "' at index " + i
            );
        }

        tokens.add(new Token(TokenType.EOF, "", "", input.length(), input.length()));
        return tokens;
    }


    private Token classifySingleWord(String rawWord, String lowered, int start, int i) {
        //multi-command separators written as words
        if (lowered.equals("and") || lowered.equals("then")) {
            return new Token(TokenType.SEPARATOR, rawWord, lowered, start, i);
        }

        //normalized verb - verb synonym normalization
        if (NORMALIZED_VERBS.containsKey(lowered)) {
            return new Token(TokenType.VERB, rawWord, NORMALIZED_VERBS.get(lowered), start, i);
        }

        //direct verb match
        if (VERBS.contains(lowered)) {
            return new Token(TokenType.VERB, rawWord, lowered, start, i);
        }

        if (PREPOSITIONS.contains(lowered)) {
            return new Token(TokenType.PREP, rawWord, lowered, start, i);
        }

        if (DETERMINERS.contains(lowered)) {
            return new Token(TokenType.DET, rawWord, lowered, start, i);
        }

        if (ADVERBS.contains(lowered)) {
            return new Token(TokenType.ADV, rawWord, lowered, start, i);
        }

        if (PRONOUNS.contains(lowered)) {
            return new Token(TokenType.PRONOUN, rawWord, lowered, start, i);
        }

        if (DIRECTIONS.containsKey(lowered)) {
            return new Token(TokenType.DIRECTION, rawWord, DIRECTIONS.get(lowered), start, i);
        }

        return new Token(TokenType.WORD, rawWord, lowered, start, i);
    }

    private WordSpan readWord(String input, int currentIndex) {
        int i = currentIndex;
        while (i < input.length()) {
            char ch = input.charAt(i);
            if (Character.isLetter(ch) || ch == '-' || ch == '\'') {
                i++;
            } else {
                break;
            }
        }

        String rawWord = input.substring(currentIndex, i);
        return new WordSpan(rawWord, currentIndex, i);
    }

    private MultiVerbMatch tryMatchMultiWordVerb(String input, WordSpan firstWord) {

        int cursor = firstWord.endIndex();

        //if at current is empty space
        while (cursor < input.length() && Character.isWhitespace(input.charAt(cursor))) {
            cursor++;
        }

        if (cursor >= input.length() || !Character.isLetter(input.charAt(cursor))) {
            return null;
        }

        //there must be a second word
        WordSpan secondWord = readWord(input, cursor);

        String combinedLowered = firstWord.rawWord().toLowerCase() + " " + secondWord.rawWord().toLowerCase();

        String normalizedVerb = NORMALIZED_MULTI_WORD_VERBS.get(combinedLowered);

        if (normalizedVerb == null) return null;

        String rawText = input.substring(firstWord.startIndex, secondWord.endIndex());

        return new MultiVerbMatch(
                rawText,
                normalizedVerb,
                firstWord.startIndex(),
                secondWord.endIndex()
        );
    }

    //WordSpan pojo
    private record WordSpan(
            String rawWord,
            int startIndex,
            int endIndex
    ) {}

    //MultiVerbMatch pojo
    private record MultiVerbMatch(
            String rawText,
            String normalizedVerb,
            int startIndex,
            int endIndex
    ) {}
}

