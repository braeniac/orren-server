package com.braeniac.orren_engine.engine.resolver;

import com.braeniac.orren_engine.engine.world.model.WorldObject;

import java.util.Objects;

public class ResolvedModifier {

    private final String preposition;
    private final WorldObject target;

    public ResolvedModifier(String preposition, WorldObject target) {
        this.preposition = Objects.requireNonNull(preposition);
        this.target = Objects.requireNonNull(target);
    }

    public String getPreposition() {
        return preposition;
    }

    public WorldObject getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "ResolvedModifier{" +
                "preposition='" + preposition + '\'' +
                ", target=" + target +
                '}';
    }
}
