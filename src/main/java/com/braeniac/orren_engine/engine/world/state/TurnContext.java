package com.braeniac.orren_engine.engine.world.state;

import com.braeniac.orren_engine.engine.world.model.Room;
import com.braeniac.orren_engine.engine.world.model.WorldObject;

import java.util.List;
import java.util.Objects;

public final class TurnContext {
    private final WorldState worldState;
    private final WorldObject lastReferencedObject; // nullable

    public TurnContext(
            WorldState worldState,
            WorldObject lastReferencedObject
    ) {

        this.worldState = Objects.requireNonNull(worldState, "worldState must not be null");
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

        Room currentRoom = worldState.getCurrentRoom();

        if (currentRoom == null) {
            return List.of();
        }

        return currentRoom.getObjects();
    }

    public List<WorldObject> getInventoryObjects() {
        return worldState.getPlayer().getInventory();
    }

    public WorldObject getLastReferencedObject() {
        return lastReferencedObject;
    }

    public List<WorldObject> getAccessibleObjects() {
        return java.util.stream.Stream
                .concat(getVisibleObjects().stream(),
                        getInventoryObjects().stream())
                .distinct()
                .toList();
    }
}