package com.braeniac.orren_engine.engine.handler;

//chooses which Handler should execute a resolved command.

import com.braeniac.orren_engine.engine.model.CommandDomain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Dispatcher {

    private final Map<String, Handler> handlerByVerb = new HashMap<>();

    public Dispatcher(List<Handler> handlers) {
        Objects.requireNonNull(handlers, "handlers must not be null");
        for (Handler handler : handlers) {
            register(handler);
        }
    }

    private void register(Handler handler) {
        Objects.requireNonNull(handler, "handler must not be null");
        handlerByVerb.put(handler.getVerb(), handler);
    }

}
