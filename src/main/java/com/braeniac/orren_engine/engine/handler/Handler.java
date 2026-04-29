package com.braeniac.orren_engine.engine.handler;

import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.state.TurnContext;
import com.braeniac.orren_engine.engine.world.state.WorldState;

//base contract for all the command handlers.
//movement, manipulation, speech etc.

public interface Handler {

    //return the domain this handler is responsible for.
    //EXAMPLE : GoHandler -> deal with movement
    CommandDomain getSupportedDomain();

    //The Resolver returns a ResolvedCommand
    //what this means:
    // - the verb is normalized
    // - directObject is already a WorldObject
    // - modifiers are already resolved

    //this is the returned narrative text the player sees

    // EXAMPLE
    // "You take the bronze key."
    // "The iron door is already open."
    String handle(ResolvedCommand command, TurnContext turnContext);

    String getVerb();

    //a quick yes or no check before dispatching a command.
    default boolean supports(ResolvedCommand command) {
        return command != null && command.getDomain() == getSupportedDomain();
    }

}
