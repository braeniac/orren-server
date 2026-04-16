package com.braeniac.orren_engine.engine.world.model;

import java.util.List;
import java.util.Objects;

public class WorldObject {

    private final String name;
    private final String ID;
    private final List<String> aliases;

    public WorldObject(final String name,
                       final String ID,
                       final List<String> aliases) {
        this.name = Objects.requireNonNull(name);
        this.ID = Objects.requireNonNull(ID);
        this.aliases = List.copyOf(Objects.requireNonNull(aliases));
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public String toString() {
        return "WorldObject{" +
                "name='" + name + '\'' +
                ", ID='" + ID + '\'' +
                ", aliases=" + aliases +
                '}';
    }
}


