package com.braeniac.orren_engine.engine.handler;

import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.model.WorldObject;

public class TakeHandler implements Handler{
    @Override
    public CommandDomain getSupportedDomain() {
        return CommandDomain.MANIPULATION;
    }

    @Override
    public String handle(ResolvedCommand command) {
        //defensive check
        //this handler is specifically for the verb "take"
        //if the router sends the wrong verb here, we fail clearly.
        if (!"take".equals(command.getVerb())) {
            throw new IllegalArgumentException("TakeHandler can only handle verb 'take'.");
        }

        WorldObject directObject = command.getDirectObject();
        //if the resolver can't find the object, we give the player an error response
        if (directObject == null) {
            return "Take what?";
        }

        //TODO ----------------------------------------------
        // right now we just return a narrative text
        // later we want to:
        // - remove object from room
        // - add object to inventory
        // - update TurnContext (the last referenced object)
        return "You take the " + directObject.getName() + ".";
    }

    @Override
    public boolean supports(ResolvedCommand command) {
        return Handler.super.supports(command);
    }
}
