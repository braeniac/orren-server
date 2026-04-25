package com.braeniac.orren_engine.engine.world.model;

import java.util.List;
import java.util.Objects;

public class WorldObject {

    private final String name;
    private final String ID;
    private final String description;
    private final List<String> aliases;

    public WorldObject(final String name,
                       final String ID,
                       final String description,
                       final List<String> aliases) {
        this.name = Objects.requireNonNull(name, "Name must not be null");
        this.ID = Objects.requireNonNull(ID, "id must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");

        //aliases let the player refer to the same object in different ways
        //EXAMPLE
        //name = "small bronze key"
        //aliases = ["key", "bronze key"]
        this.aliases = List.copyOf(Objects.requireNonNull(aliases, "aliases must not be null"));
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public String getDescription() {
        return description;
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


