package com.braeniac.orren_engine.engine.AST;

import java.util.List;
import java.util.Objects;

public final class ASTCommandSequence {

    //each ASTCommand is ONE step in the entire plan
    private final List<ASTCommand> commands;

    public ASTCommandSequence(List<ASTCommand> commands) {
        this.commands = List.copyOf(Objects.requireNonNull(commands));
    }

    public List<ASTCommand> getCommands() {
        return commands;
    }

    @Override
    public String toString() {
        return "ASTCommandSequence{" +
                "commands=" + commands +
                '}';
    }
}
