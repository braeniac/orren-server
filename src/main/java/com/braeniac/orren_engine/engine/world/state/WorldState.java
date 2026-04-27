package com.braeniac.orren_engine.engine.world.state;

import com.braeniac.orren_engine.engine.world.model.Player;
import com.braeniac.orren_engine.engine.world.model.Room;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//represents the current state of the game world

//Let's say the player types in "go north"
//the GoHandler will:
//-ask WorldState for the current room
//-move whether the room has a north exit
//-move the player to the destination room
//-return the new room description

public class WorldState {

    //keeps track of current room
    //keeps track of inventory
    //player health status
    private final Player player;

    //all rooms in the world
    private final Map<String, Room> roomsById = new HashMap<>();

    public WorldState(Player player, Collection<Room> rooms) {
        this.player = Objects.requireNonNull(player, "player must not be null");
        Objects.requireNonNull(rooms, "rooms must not be null");

        for (Room room :rooms) {
            addRoom(room);
        }
    }

    public Player getPlayer() {
        return player;
    }

    //when creating the world each room will be registered so players can move into rooms by id.
    public void addRoom(Room room) {
        Objects.requireNonNull(room, "room must not be null");
        roomsById.put(room.getId(), room);
    }

    //find room by id.
    public Room getRoom(String roomId) {
        return roomsById.get(roomId);
    }

    //the current room the player is in right now.
    public Room getCurrentRoom() {
        return getRoom(player.getCurrentRoomId());
    }


    //moves the player into a new room
    //this method does NOT validate whether the movement is allowed (locked room).
    public void movePlayerTo(String roomId) {
        if (!roomsById.containsKey(roomId)) {
            throw new IllegalArgumentException("Unknown room id " + roomId);
        }
        player.moveTo(roomId);
    }

    //debugging only ---------------------------------------------
    public Collection<Room> getRooms() {
        return Map.copyOf(roomsById).values();
    }
}
