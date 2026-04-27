package com.braeniac.orren_engine.engine.handler;

import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.model.WorldObject;
import com.braeniac.orren_engine.engine.world.state.TurnContext;
import com.braeniac.orren_engine.engine.world.state.WorldState;


//handles movement commands like:
// - go north
// - north

public class GoHandler implements Handler{
    @Override
    public CommandDomain getSupportedDomain() {
        return CommandDomain.MOVEMENT;
    }

    @Override
    public String handle(ResolvedCommand command, TurnContext turnContext) {

        if (!"go".equals(command.getVerb())) {
            throw new IllegalArgumentException("GoHandler can only handle verb 'go'.");
        }

        WorldObject directObject = command.getDirectObject();

        //if no resolved movement target exists, the command is incomplete.
        if (directObject == null) {
            return "Go where?";
        }


        //TODO ----------------------------------------------
        // right now we just return a narrative text
        // later we want to:
        // - interpret direction from the object
        // - look up the matching exit
        // - move the player to the next room
        // - render the new room description

        return "You go " + directObject + " .";
    }

    @Override
    public boolean supports(ResolvedCommand command) {
        return Handler.super.supports(command);
    }
}
