package com.braeniac.orren_engine.engine.world.model;

import com.braeniac.orren_engine.engine.parse.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


//the player needs to track
// - current room
// - inventory

//TODO-----
// add health, flags, achievements, equipped items.

public class Player {

    private String currentRoomId;
    private final List<WorldObject> inventory = new ArrayList<>();

    public Player(String currentRoomId) {
        this.currentRoomId = Objects.requireNonNull(currentRoomId, "currentRoomId must not be null");
    }

    public String getCurrentRoomId() {
        return currentRoomId;
    }

    public void moveTo(String roomId) {
        this.currentRoomId = Objects.requireNonNull(roomId, "roomId must not be null");
    }

    public List<WorldObject> getInventory() {
        return List.copyOf(inventory);
    }

    public void addToInventory(WorldObject object) {
        inventory.add(Objects.requireNonNull(object, "object must not be null"));
    }

    public void removeFromInventory(WorldObject object) {
        inventory.remove(object);
    }

    public boolean hasInventory(WorldObject object) {
        return inventory.contains(object);
    }

}
