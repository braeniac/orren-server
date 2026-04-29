package com.braeniac.orren_engine.engine.handler;

import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.model.Openable;
import com.braeniac.orren_engine.engine.world.model.Portable;
import com.braeniac.orren_engine.engine.world.model.WorldObject;
import com.braeniac.orren_engine.engine.world.state.TurnContext;
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
    public String handle(ResolvedCommand command, TurnContext turnContext) {
        if (!"open".equals(command.getVerb())) {
            throw new IllegalArgumentException("OpenHandler can only handle verb 'open'.");
        }

        WorldObject directObject = command.getDirectObject();

        if (directObject == null) {
            return "Open what?";
        }

        //is the object openable (like chest).
        if (!(directObject instanceof Openable openable)) {
            return "You can't open that " + directObject.getName() + " .";
        }

        //chest is openable but does it require a key?
        //if the directObject is open
        if (openable.isOpen()) {
            return "The " + directObject.getName() + " is already open.";
        }

        //the directObject implemented Openable so its up to the object if it's in an open/closed state.
        openable.open();

        return "You open the " + directObject.getName() + " .";
    }


    @Override
    public String getVerb() {
        return "open";
    }

    @Override
    public boolean supports(ResolvedCommand command) {
        return Handler.super.supports(command);
    }
}
