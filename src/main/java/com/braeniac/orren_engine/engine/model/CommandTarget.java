package com.braeniac.orren_engine.engine.model;


import java.util.List;
import java.util.Objects;

/**
 * The main thing the user command acts on
 *
 * EXAMPLE:
 *
 * take lantern ->  target = lantern
 * open chest   ->  target = chest
 * go north     ->  target = north
 *
 */

public class CommandTarget {

    private final String determiner;  //nullable
    private final List<String> descriptors;
    private final String head;        //nullable if pronoun
    private final String pronoun;     //nullable if normal noun phrase

    public CommandTarget(String determiner,
                         List<String> descriptors,
                         String head,
                         String pronoun) {
        this.determiner = determiner;
        this.descriptors = List.copyOf(Objects.requireNonNull(descriptors));
        this.head = head;
        this.pronoun = pronoun;
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

    public boolean isPronoun() {
        return pronoun != null;
    }

    @Override
    public String toString() {
        return "CommandTarget{" +
                "determiner='" + determiner + '\'' +
                ", descriptors=" + descriptors +
                ", head='" + head + '\'' +
                ", pronoun='" + pronoun + '\'' +
                '}';
    }
}
