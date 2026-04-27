package com.braeniac.orren_engine.engine.handler;

import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.model.WorldObject;
import com.braeniac.orren_engine.engine.world.state.WorldState;


// handles "open" commands

//"open" is a manipulation verb but lead to different future world rules.

//EXAMPLE
// - object must be openable
// - object may already be open
// - object may be locked

public class OpenHandler implements Handler{
    @Override
    public CommandDomain getSupportedDomain() {
        return CommandDomain.MANIPULATION;
    }

    @Override
    public String handle(ResolvedCommand command, WorldState worldState) {
        if (!"open".equals(command.getVerb())) {
            throw new IllegalArgumentException("OpenHandler can only handle verb 'open'.");
        }

        WorldObject directObject = command.getDirectObject();

        if (directObject == null) {
            return "Open what?";
        }


        //TODO ----------------------------------------------
        // right now we just return a narrative text
        // later we want to:
        // - instanceOf openable check
        // - locked checks
        // - state transitions
        return "You open the " + directObject.getName() + " .";
    }

    @Override
    public boolean supports(ResolvedCommand command) {
        return Handler.super.supports(command);
    }
}
