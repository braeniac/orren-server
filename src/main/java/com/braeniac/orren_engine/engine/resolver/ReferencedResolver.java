package com.braeniac.orren_engine.engine.resolver;

import com.braeniac.orren_engine.engine.model.UserCommand;
import com.braeniac.orren_engine.engine.world.state.TurnContext;

/**
 * Resolves text-based command targets into actual world objects
 *
 * using the current game/turn context.
 */

public interface ReferencedResolver {
    ResolvedCommand resolve(UserCommand command, TurnContext turnContext);
}
