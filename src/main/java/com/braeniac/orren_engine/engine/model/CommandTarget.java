package com.braeniac.orren_engine.engine.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents the main thing the user command acts on.
 *
 * Examples:
 * ----------
 * take lantern  -> target = lantern
 * open chest    -> target = chest
 * go north      -> target = north
 * use it        -> target = pronoun "it"
 *
 * This class supports two valid shapes:
 *
 * 1. Normal noun phrase target
 *    - head is present
 *    - pronoun is null
 *
 * 2. Pronoun target
 *    - pronoun is present
 *    - head is null
 *
 * We intentionally reject invalid mixed/empty states.
 */
public final class CommandTarget {

    private final String determiner;   // nullable
    private final List<String> descriptors;
    private final String head;         // nullable if pronoun
    private final String pronoun;      // nullable if normal noun phrase

    public CommandTarget(
            String determiner,
            List<String> descriptors,
            String head,
            String pronoun
    ) {
        this.descriptors = List.copyOf(Objects.requireNonNull(descriptors, "descriptors must not be null"));

        // We want exactly one of these to be set:
        // - head for a noun phrase
        // - pronoun for a pronoun target
        //
        // Invalid:
        //   head = null, pronoun = null
        //   head = "key", pronoun = "it"
        boolean hasHead = head != null && !head.isBlank();
        boolean hasPronoun = pronoun != null && !pronoun.isBlank();

        if (hasHead == hasPronoun) {
            throw new IllegalArgumentException(
                    "CommandTarget must have either a head or a pronoun, but not both."
            );
        }

        this.determiner = determiner;
        this.head = hasHead ? head : null;
        this.pronoun = hasPronoun ? pronoun : null;
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