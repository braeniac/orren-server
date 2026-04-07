package com.braeniac.orren_engine.engine.AST;

import java.util.List;
import java.util.Objects;

//represents a thing.

public class ASTNounPhrase {
    private final String determiner; //nullable
    private final List<String> descriptors;
    private final String head;  //nullable
    private final String pronoun;   //nullable
    private final List<ASTPrepPhrase> postModifiers;

    public ASTNounPhrase(String determiner,
                         List<String> descriptors,
                         String head,
                         String pronoun,
                         List<ASTPrepPhrase> postModifiers) {
        this.determiner = determiner;
        this.descriptors = List.copyOf(Objects.requireNonNull(descriptors));
        this.head = head;
        this.pronoun = pronoun;
        this.postModifiers = List.copyOf(Objects.requireNonNull(postModifiers));
    }

    public String getDeterminer() {
        return determiner;
    }

    public List<String> getDescriptors() {
        return descriptors;
    }

    public String getHead() {
        return head;
    }

    public String getPronoun() {
        return pronoun;
    }

    public List<ASTPrepPhrase> getPostModifiers() {
        return postModifiers;
    }

    public boolean isPronoun() {
        return pronoun != null;
    }

    @Override
    public String toString() {
        return "ASTNounPhrase{" +
                "determiner='" + determiner + '\'' +
                ", descriptors=" + descriptors +
                ", head='" + head + '\'' +
                ", pronoun='" + pronoun + '\'' +
                ", postModifiers=" + postModifiers +
                '}';
    }
}
