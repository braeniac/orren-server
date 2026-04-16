package com.braeniac.orren_engine.engine.world.state;

import com.braeniac.orren_engine.engine.world.model.WorldObject;

import java.util.List;
import java.util.Objects;

public class TurnContext {
    // player
    // current room
    // visible objects
    // inventory
    // last referenced object ("it")

    private List<WorldObject> visibleObjects;
    private List<WorldObject> inventoryObjects;
    private  WorldObject lastReferencedObject;

    public TurnContext(List<WorldObject> visibleObjects,
                       List<WorldObject> inventoryObjects,
                       WorldObject lastReferencedObject) {
        this.visibleObjects = List.copyOf(Objects.requireNonNull(visibleObjects ,"Visible objects must not be null"));
        this.inventoryObjects = List.copyOf(Objects.requireNonNull(inventoryObjects, "Inventory objects must not be null"));
        this.lastReferencedObject = Objects.requireNonNull(lastReferencedObject);
    }

    public List<WorldObject> getVisibleObjects() {
        return visibleObjects;
    }

    public List<WorldObject> getInventoryObjects() {
        return inventoryObjects;
    }

    public WorldObject getLastReferencedObject() {
        return lastReferencedObject;
    }

    //concats objects currently available in current room plus what's already in your inventory.
    public List<WorldObject> getAccessibleObjects() {
        return java.util.stream.Stream.concat(visibleObjects.stream(), inventoryObjects.stream())
                .distinct()
                .toList();
    }

    @Override
    public String toString() {
        return "TurnContext{" +
                "visibleObjects=" + visibleObjects +
                ", inventoryObjects=" + inventoryObjects +
                ", lastReferencedObject=" + lastReferencedObject +
                '}';
    }
}
