package com.braeniac.orren_engine.engine.world.model;

import java.util.Objects;

public class WorldObject {

    private final String name;
    private final String ID;

    public WorldObject(final String name,
                       final String ID) {
        this.name = Objects.requireNonNull(name);
        this.ID = Objects.requireNonNull(ID);
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }
}


