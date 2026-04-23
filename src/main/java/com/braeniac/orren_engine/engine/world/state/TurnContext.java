package com.braeniac.orren_engine.engine.world.state;

import com.braeniac.orren_engine.engine.world.model.WorldObject;

import java.util.List;
import java.util.Objects;

public final class TurnContext {
    private final List<WorldObject> visibleObjects;
    private final List<WorldObject> inventoryObjects;
    private final WorldObject lastReferencedObject; // nullable

    public TurnContext(
            List<WorldObject> visibleObjects,
            List<WorldObject> inventoryObjects,
            WorldObject lastReferencedObject
    ) {
        // These lists should always exist, even if empty.
        // They represent the current search scope for object resolution.
        this.visibleObjects = List.copyOf(
                Objects.requireNonNull(visibleObjects, "visibleObjects must not be null")
        );

        this.inventoryObjects = List.copyOf(
                Objects.requireNonNull(inventoryObjects, "inventoryObjects must not be null")
        );

        // This is intentionally allowed to be null.
        //
        // Why:
        // Some turns do not reference any prior object yet.
        // Example:
        //   say "hello"
        //   look
        //   wait
        //
        // Pronoun resolution ("it") may use this later, but not every command needs it.
        this.lastReferencedObject = lastReferencedObject;
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

    public List<WorldObject> getAccessibleObjects() {
        return java.util.stream.Stream
                .concat(visibleObjects.stream(), inventoryObjects.stream())
                .distinct()
                .toList();
    }
}