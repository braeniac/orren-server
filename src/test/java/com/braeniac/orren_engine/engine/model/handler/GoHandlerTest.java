
import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.model.Player;
import com.braeniac.orren_engine.engine.world.model.Room;
import com.braeniac.orren_engine.engine.world.model.WorldObject;
import com.braeniac.orren_engine.engine.world.state.TurnContext;
import com.braeniac.orren_engine.engine.world.state.WorldState;
import com.braeniac.orren_engine.engine.handler.GoHandler;
import org.h2.command.Command;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests GoHandler.
 *
 * Why this matters:
 * -----------------
 * Movement is what turns your engine from a single-room parser into an actual game world.
 *
 * This test proves:
 *   1. the handler can read a direction from the resolved command
 *   2. the current room can provide an exit for that direction
 *   3. the player can move to the destination room
 */
class GoHandlerTest {

    @Test
    void shouldMovePlayerWhenExitExists() {
        /*
         * Starting room.
         *
         * The player begins here.
         */
        Room hollow = new Room(
                "room_hollow",
                "The Hollow of Orren",
                "You awaken to the hollow echo of dripping water."
        );

        /*
         * Destination room.
         *
         * The player should end up here after going down.
         */
        Room stairway = new Room(
                "room_stairway",
                "Crumbling Stairway",
                "The stairway curls downward into the dark."
        );

        /*
         * Add an exit from the starting room to the stairway.
         *
         * This represents:
         *   down -> room_stairway
         */
        hollow.addExit("down", "room_stairway");

        /*
         * Player starts in the hollow.
         */
        Player player = new Player("room_hollow");

        /*
         * WorldState knows all rooms and the player's current location.
         */
        WorldState worldState = new WorldState(
                player,
                List.of(hollow, stairway)
        );

        TurnContext turnContext = new TurnContext(worldState, null);

        /*
         * First-pass movement target.
         *
         * Your current command system represents directions as a WorldObject.
         * That means "down" is passed as the directObject.
         *
         * Later, you may replace this with a Direction enum.
         */
        WorldObject downDirection = new WorldObject(
                "direction_down",
                "down",
                "A downward path.",
                List.of("d", "down")
        );

        /*
         * Represents:
         *   go down
         */
        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.MOVEMENT,
                "go",
                downDirection,
                null,
                List.of(),
                null
        );

        GoHandler handler = new GoHandler();

        String response = handler.handle(command, turnContext);

        assertEquals("The stairway curls downward into the dark.", response);
        assertEquals("room_stairway", player.getCurrentRoomId());
    }

    @Test
    void shouldRejectMovementWhenNoDirectionIsProvided() {
        Room hollow = new Room(
                "room_hollow",
                "The Hollow of Orren",
                "A damp cave."
        );

        Player player = new Player("room_hollow");

        WorldState worldState = new WorldState(
                player,
                List.of(hollow)
        );

        TurnContext turnContext = new TurnContext(worldState, null);

        /*
         * Represents:
         *   go
         *
         * No direct object means no direction was resolved.
         */
        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.MOVEMENT,
                "go",
                null,
                null,
                List.of(),
                null
        );

        GoHandler handler = new GoHandler();

        String response = handler.handle(command, turnContext);

        assertEquals("Go where?", response);
        assertEquals("room_hollow", player.getCurrentRoomId());
    }

    @Test
    void shouldRejectMovementWhenExitDoesNotExist() {
        Room hollow = new Room(
                "room_hollow",
                "The Hollow of Orren",
                "A damp cave."
        );

        Player player = new Player("room_hollow");

        WorldState worldState = new WorldState(
                player,
                List.of(hollow)
        );

        TurnContext turnContext = new TurnContext(worldState, null);

        WorldObject northDirection = new WorldObject(
                "direction_north",
                "north",
                "A northern path.",
                List.of("n", "north")
        );

        /*
         * Represents:
         *   go north
         *
         * But the room has no north exit.
         */
        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.MOVEMENT,
                "go",
                northDirection,
                null,
                List.of(),
                null
        );

        GoHandler handler = new GoHandler();

        String response = handler.handle(command, turnContext);

        assertEquals("You can't go north.", response);
        assertEquals("room_hollow", player.getCurrentRoomId());
    }

    @Test
    void shouldThrowWhenWrongVerbIsSentToGoHandler() {
        Room hollow = new Room(
                "room_hollow",
                "The Hollow of Orren",
                "A damp cave."
        );

        Player player = new Player("room_hollow");

        WorldState worldState = new WorldState(
                player,
                List.of(hollow)
        );

        TurnContext turnContext = new TurnContext(worldState, null);

        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.PERCEPTION,
                "look",
                null,
                null,
                List.of(),
                null
        );

        GoHandler handler = new GoHandler();

        assertThrows(IllegalArgumentException.class, () ->
                handler.handle(command, turnContext)
        );
    }
}