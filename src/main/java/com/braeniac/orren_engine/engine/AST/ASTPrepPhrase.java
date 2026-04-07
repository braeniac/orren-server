package com.braeniac.orren_engine.engine.AST;

import java.util.Objects;

//represents a relationship between things.

public class ASTPrepPhrase {
    private final String preposition;
    private final ASTNounPhrase object;

    public ASTPrepPhrase(String preposition,
                         ASTNounPhrase object) {
        this.preposition = Objects.requireNonNull(preposition);
        this.object = Objects.requireNonNull(object);
    }

    public ASTNounPhrase getObject() {
        return object;
    }

    public String getPreposition() {
        return preposition;
    }

    @Override
    public String toString() {
        return "ASTPrepPhrase{" +
                "preposition='" + preposition + '\'' +
                ", object=" + object +
                '}';
    }
}
