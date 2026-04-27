package com.braeniac.orren_engine.engine.handler;

import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.state.WorldState;

//handles speech command
// - say "hello"
// - should "who goes there?"
// - whisper "be quiet"

//speech commands do not usually
//resolve normal world objects
//instead, they work with quoted text.
//keeping speech seperate prevents object-oriented assumptions from leaking in.

public class SayHandler implements Handler{

    @Override
    public CommandDomain getSupportedDomain() {
        return CommandDomain.SPEECH;
    }

    @Override
    public String handle(ResolvedCommand command, WorldState worldState) {
        if (!"say".equals(command.getVerb())) {
            throw new IllegalArgumentException("SayHandler can only handle verb 'say'.");
        }

        String quotedText = command.getQuotedText();

        if (quotedText == null || quotedText.isBlank()) {
            return "Say what?";
        }

        //TODO ----------------------------------------------
        // right now we just return a narrative text
        // later we want to:
        // - just echo the speech back to the player
        // - connect dialogue systems,
        //      - NPC reactions, magical speech triggers, etc.

        return "You say, \"" + quotedText + "\"";

    }

    @Override
    public boolean supports(ResolvedCommand command) {
        return Handler.super.supports(command);
    }
}
