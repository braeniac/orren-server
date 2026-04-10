package com.braeniac.orren_engine.engine.resolver;

import com.braeniac.orren_engine.engine.model.UserCommand;
import com.braeniac.orren_engine.engine.world.state.TurnContext;

public class Resolver implements ReferencedResolver {

    @Override
    public ResolvedCommand resolve(UserCommand command, TurnContext turnContext) {
        return null;
    }
}
