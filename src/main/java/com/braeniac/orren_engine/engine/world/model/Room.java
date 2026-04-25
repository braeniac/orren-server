package com.braeniac.orren_engine.engine.world.model;

//reprsents as location the player can stand in.

//EXAMPLE
// - The hollow of orren
// - Iron Door Chamber

//rooms hold visible objects and exists.
//the resolver will eventually use the current room to answer:
//  "what objects can the player currently refer to?"

import java.util.*;

public class Room {

    private final String id;
    private final String name;
    private final String description;
    //objects currently visible in this room.
    private final List<WorldObject> objects = new ArrayList<>();
    //key = direction text like "north"
    //value = destination room id
    private final Map<String, String> exits = new HashMap<>();

    public Room(String id, String name, String description) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<WorldObject> getObjects() {
        return List.copyOf(objects);
    }

    public void addObject(WorldObject object) {
        objects.add(Objects.requireNonNull(object, "object must not be null"));
    }

    public void removeObject(WorldObject object) {
        objects.remove(object);
    }

    public String getExit(String direction) {
        return exits.get(direction);
    }

    public Map<String, String> getExits() {
        return Map.copyOf(exits);
    }
}
