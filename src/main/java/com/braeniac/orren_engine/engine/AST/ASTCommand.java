package com.braeniac.orren_engine.engine.AST;

import java.util.List;
import java.util.Objects;

public class ASTCommand {

    private final String verb;
    //a way to represent an object in a command
    //along with words to describe it.
    private final ASTNounPhrase directObjects;  //nullable
    //whisper, shout, yell
    private final String quotedText; //nullable
    //take the key --> from the wooden chest <--
    private final List<ASTPrepPhrase> prepPhrases;
    private String adverb; //nullable

    public ASTCommand(String verb,
                      ASTNounPhrase directObjects,
                      String quotedText,
                      List<ASTPrepPhrase> prepPhrases,
                      String adverb) {
        this.verb = Objects.requireNonNull(verb);
        this.directObjects = directObjects;
        this.quotedText = quotedText;
        this.prepPhrases = List.copyOf(Objects.requireNonNull(prepPhrases));
    }

    public String getVerb() {
        return verb;
    }

    public ASTNounPhrase getDirectObjects() {
        return directObjects;
    }

    public String getQuotedText() {
        return quotedText;
    }

    public List<ASTPrepPhrase> getPrepPhrases() {
        return prepPhrases;
    }

    public String getAdverb() {
        return adverb;
    }

    @Override
    public String toString() {
        return "ASTCommand{" +
                "verb='" + verb + '\'' +
                ", directObjects=" + directObjects +
                ", quotedText='" + quotedText + '\'' +
                ", prepPhrases=" + prepPhrases +
                ", adverb='" + adverb + '\'' +
                '}';
    }

}
