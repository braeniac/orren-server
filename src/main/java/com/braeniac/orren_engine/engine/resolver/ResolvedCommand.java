package com.braeniac.orren_engine.engine.resolver;
import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.world.model.WorldObject;

import java.util.List;
import java.util.Objects;

/**
 * A command whose textual targets have been resolved to actual world objects.
 *
 * This is the structure that command handlers should execute.
 *
 */
public class ResolvedCommand {
    private final CommandDomain domain;
    private final String verb;
    private final WorldObject directObject; //nullable
    private final String quotedText; //nullable
    private final List<ResolvedModifier> modifiersList; //nullable
    private String adverbs; //nullable

    public ResolvedCommand(CommandDomain domain,
                           String verb,
                           WorldObject directObject,
                           String quotedText,
                           List<ResolvedModifier> modifiersList,
                           String adverbs) {
        this.domain = Objects.requireNonNull(domain);
        this.verb = Objects.requireNonNull(verb);
        this.directObject = directObject;
        this.quotedText = quotedText;
        this.modifiersList = List.copyOf(Objects.requireNonNull(modifiersList));
        this.adverbs = adverbs;
    }

    public CommandDomain getDomain() {
        return domain;
    }

    public String getVerb() {
        return verb;
    }

    public WorldObject getDirectObject() {
        return directObject;
    }

    public String getQuotedText() {
        return quotedText;
    }

    public List<ResolvedModifier> getModifiersList() {
        return modifiersList;
    }

    public String getAdverbs() {
        return adverbs;
    }

    public boolean hasDirectObject() {
        return directObject != null;
    }

    public boolean hasQuotedText() {
        return quotedText != null;
    }

    @Override
    public String toString() {
        return "ResolvedCommand{" +
                "domain=" + domain +
                ", verb='" + verb + '\'' +
                ", directObject=" + directObject +
                ", quotedText='" + quotedText + '\'' +
                ", modifiersList=" + modifiersList +
                ", adverbs='" + adverbs + '\'' +
                '}';
    }
}
