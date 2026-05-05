package com.braeniac.orren_engine.engine.handler;

import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.model.Room;
import com.braeniac.orren_engine.engine.world.model.WorldObject;
import com.braeniac.orren_engine.engine.world.state.TurnContext;
import com.braeniac.orren_engine.engine.world.state.WorldState;
import org.aspectj.weaver.World;


//handles movement commands like:
// - go north
// - north

public class GoHandler implements Handler{
    @Override
    public CommandDomain getSupportedDomain() {
        return CommandDomain.MOVEMENT;
    }

    @Override
    public String handle(ResolvedCommand command, TurnContext turnContext) {

        if (!"go".equals(command.getVerb())) {
            throw new IllegalArgumentException("GoHandler can only handle verb 'go'.");
        }

        WorldObject directObject = command.getDirectObject();

        //if no resolved movement target exists, the command is incomplete.
        if (directObject == null) return "Go where?";

        //get the direction
        String direction = directObject.getName();

        //get the current world state so we can move the player
        WorldState worldState = turnContext.getWorldState();

        //find the room the player is currently in
        Room currentRoom = worldState.getCurrentRoom();

        if (currentRoom == null) return "You are nowhere.";

        //based on direction get the exit based on direction
        String destinationRoomId = currentRoom.getExit(direction);

        if (destinationRoomId == null) return "You can't go " + direction + ".";

        //actually update player state
        worldState.movePlayerTo(destinationRoomId);

        Room newRoom = worldState.getCurrentRoom();

        if (newRoom == null) {
            return "You move " + direction + ", but the destination is missing.";
        }

        return newRoom.getDescription();
    }


    @Override
    public String getVerb() {
        return "go";
    }

    @Override
    public boolean supports(ResolvedCommand command) {
        return Handler.super.supports(command);
    }
}
