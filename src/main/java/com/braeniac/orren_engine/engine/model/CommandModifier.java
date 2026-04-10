package com.braeniac.orren_engine.engine.model;

import java.util.Objects;

/**
 * represents a command modifier, usually represented by prepositional phrases
 *
 * EXAMPLE:
 *      with key
 *      from chest
 *      into forge
 */

public class CommandModifier {

    private final CommandTarget target;
    private final String preposition;

    public CommandModifier(CommandTarget target, String preposition) {
        this.target = Objects.requireNonNull(target);
        this.preposition = Objects.requireNonNull(preposition);
    }

    public CommandTarget getTarget() {
        return target;
    }

    public String getPreposition() {
        return preposition;
    }

    @Override
    public String toString() {
        return "CommandModifier{" +
                "target=" + target +
                ", preposition='" + preposition + '\'' +
                '}';
    }
}
