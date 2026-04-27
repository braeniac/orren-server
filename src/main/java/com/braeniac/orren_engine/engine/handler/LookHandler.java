package com.braeniac.orren_engine.engine.handler;

import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.model.WorldObject;
import com.braeniac.orren_engine.engine.world.state.WorldState;

//handles 'look' commands.

//perception commands work in two modes:
// 1. no target -> "look" means examine the room/current scene
// 2. with target -> "look door" means inspect one object

public class LookHandler implements Handler{
    @Override
    public CommandDomain getSupportedDomain() {
        return CommandDomain.PERCEPTION;
    }

    @Override
    public String handle(ResolvedCommand command, WorldState worldState) {
        if (!"look".equals(command.getVerb())) {
            throw new IllegalArgumentException("LookHandler can only handle verb 'look'.");
        }

        WorldObject directObject = command.getDirectObject();

        //if no object is specified, we treat this as a generic room look.
        if (directObject == null) {
            return "You look around.";
        }

        //if an object is present, we treat this as focused inspection
        //later this can call object-specific description logic.
        return "You look at the " + directObject.getName() + ".";

    }

    @Override
    public boolean supports(ResolvedCommand command) {
        return Handler.super.supports(command);
    }
}
