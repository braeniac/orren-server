package com.braeniac.orren_engine.engine.world.model;

//represents a physical item in the world
//every item is a world object, but not every world object is an item.

//EXAMPLE
// - a lantern is an item
// - a room is not an item.
// - a door may be a WorldObject, but not necessarily an item.

import java.util.List;

public class Item extends WorldObject {
    public Item(String id, String name, String description, List<String> aliases) {
        super(id, name, description, aliases);
    }
}
