package com.braeniac.orren_engine.engine.handler;

//chooses which Handler should execute a resolved command.

import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.state.TurnContext;
import org.hibernate.dialect.lock.internal.HANALockingSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//choose what handler it uses when we resolve ResolveCommand.
//centralize handler with a dispatcher.

public class Dispatcher {

    private final Map<String, Handler> handlerByVerb = new HashMap<>();

    public Dispatcher(List<Handler> handlers) {
        Objects.requireNonNull(handlers, "handlers must not be null");
        for (Handler handler : handlers) {
            register(handler);
        }
    }

    //register the handler by the verb it supports
    private void register(Handler handler) {
        Objects.requireNonNull(handler, "handler must not be null");
        handlerByVerb.put(handler.getVerb(), handler);
    }

    //executes the resolved command using handler.
    public String dispatch(ResolvedCommand command, TurnContext turnContext) {
        Objects.requireNonNull(command, "command must not be null");
        Objects.requireNonNull(turnContext, "turnContext must not be nul");

        Handler handler = handlerByVerb.get(command.getVerb());

        if (handler == null) {
            return "I don't know how to '" + command.getVerb() + "'.";
        }

        return handler.handle(command, turnContext);
    }

}
