package com.braeniac.orren_engine.engine.model;

import org.apache.catalina.User;

import java.util.List;
import java.util.Objects;

/**
 * Engine-facing representation of one parsed user command.
 *
 * This is the structure that command handlers should consume.
 *
 * Examples:
 *
 * "take the small bronze key from the old chest"
 * domain = MANIPULATION
 * verb = take
 * directTarget = key
 * modifier = [ from chest ]
 *
 * "say \"hello\""
 *  -> domain = SPEECH
 *  -> verb = "say"
 *  -> quotedText = "hello"
 */

public class UserCommand {
    private final Model domain;
    private final String verb;
    private final CommandTarget directTarget;
    private final String quotedText;
    private final List<CommandModifier> modifierList;
    private final String adverb;

    public UserCommand(Model domain,
                       String verb,
                       CommandTarget directTarget,
                       String quotedText,
                       List<CommandModifier> modifierList,
                       String adverb) {
        this.domain = Objects.requireNonNull(domain);
        this.verb = Objects.requireNonNull(verb);
        this.directTarget = directTarget;
        this.quotedText = quotedText;
        this.modifierList = List.copyOf(Objects.requireNonNull(modifierList));
        this.adverb = adverb;
    }

    public Model getDomain() {
        return domain;
    }

    public String getVerb() {
        return verb;
    }

    public CommandTarget getDirectTarget() {
        return directTarget;
    }

    public String getQuotedText() {
        return quotedText;
    }

    public List<CommandModifier> getModifierList() {
        return modifierList;
    }

    public String getAdverb() {
        return adverb;
    }

    public boolean hasDirectTarget() {
        return directTarget != null;
    }

    public boolean hasQuotedText() {
        return quotedText != null;
    }

    @Override
    public String toString() {
        return "UserCommand{" +
                "domain=" + domain +
                ", verb='" + verb + '\'' +
                ", directTarget=" + directTarget +
                ", quotedText='" + quotedText + '\'' +
                ", modifierList=" + modifierList +
                ", adverb='" + adverb + '\'' +
                '}';
    }
}
