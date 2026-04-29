package com.braeniac.orren_engine.engine.handler;

import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.model.Room;
import com.braeniac.orren_engine.engine.world.model.WorldObject;
import com.braeniac.orren_engine.engine.world.state.TurnContext;
import com.braeniac.orren_engine.engine.world.state.WorldState;

//handles 'look' commands.

//perception commands work in two modes:
// 1. no target -> "look" means examine the room/current scene
// 2. with target -> "look door" means inspect one object

public class LookHandler implements Handler{
    @Override
    public CommandDomain getSupportedDomain() {
        return CommandDomain.PERCEPTION;
    }

    @Override
    public String handle(ResolvedCommand command, TurnContext turnContext) {
        if (!"look".equals(command.getVerb())) {
            throw new IllegalArgumentException("LookHandler can only handle verb 'look'.");
        }

        WorldObject directObject = command.getDirectObject();

        //if no object is specified, we treat this as a generic room look.
        //EXAMPLE:
        // user types: "look at lantern"
        if (directObject != null) {
            return describeObject(directObject);
        }

        //if the user just wants the description of the room
        //EXAMPLE:
        //user types: "look"
        Room currentRoom = turnContext.getWorldState().getCurrentRoom();

        if (currentRoom == null)
            return "You're nowhere.";

        //if an object is present, we treat this as focused inspection
        //later this can call object-specific description logic.
        return describeRoom(currentRoom);

    }

    @Override
    public String getVerb() {
        return "look";
    }

    //describe the object in room or in inventory.
    private String describeObject(WorldObject directObject) {
        String description = directObject.getDescription();

        //if an object doesn't have a description
        if (description == null || description.isBlank()) {
            return "You see nothing unusual about the " + directObject.getName() + ".";
        }

        return description;
    }

    //describe the room (including objects + exits)
    private String describeRoom(Room currentRoom) {

        StringBuilder sb = new StringBuilder();

        //helps player understand what things they can interact with.
        sb.append(currentRoom.getDescription());
        if (!currentRoom.getObjects().isEmpty()) {
            sb.append("\n\nYou see:");

            for (WorldObject object : currentRoom.getObjects()) {
                sb.append("\n- ").append(object.getName());
            }
        }

        //helps the player understand possible movement options
        //TODO ------------
        // hide exits until its discovered
        if (!currentRoom.getExits().isEmpty()) {
            sb.append("\n\nExits:");

            for (String direction : currentRoom.getExits().keySet()) {
                sb.append("\n- ").append(direction);
            }
        }

        return sb.toString();
    }


    @Override
    public boolean supports(ResolvedCommand command) {
        return Handler.super.supports(command);
    }
}
