package com.braeniac.orren_engine.engine.handler.perception;

import com.braeniac.orren_engine.engine.handler.LookHandler;
import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.model.Player;
import com.braeniac.orren_engine.engine.world.model.Room;
import com.braeniac.orren_engine.engine.world.model.WorldObject;
import com.braeniac.orren_engine.engine.world.state.TurnContext;
import com.braeniac.orren_engine.engine.world.state.WorldState;
import org.h2.command.Command;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests LookHandler.
 *
 * Why this matters:
 * -----------------
 * "look" is one of the most important commands in a text adventure.
 *
 * It proves that the engine can describe:
 *   1. the current room
 *   2. objects in the current room
 *   3. specific objects
 *   4. exits from the room
 *
 * In other words, this test confirms that your world model is becoming visible
 * to the player through narrative text.
 */
class LookHandlerTest {

    @Test
    void shouldDescribeCurrentRoomWhenNoDirectObjectIsProvided() {
        /*
         * Create a room with a strong opening description.
         *
         * This represents the player typing:
         *   look
         *
         * Since the command has no direct object, LookHandler should describe
         * the current room instead of a specific item.
         */
        Room hollow = new Room(
                "room_hollow",
                "The Hollow of Orren",
                "You awaken to the hollow echo of dripping water."
        );

        /*
         * Add an object to the room so we can confirm LookHandler lists visible objects.
         */
        WorldObject lantern = new WorldObject(
                "item_lantern",
                "rusted lantern",
                "A rusted lantern lies cold against the stone.",
                List.of("lantern", "rusted lantern")
        );

        hollow.addObject(lantern);

        /*
         * Add an exit so we can confirm LookHandler lists possible movement options.
         */
        hollow.addExit("down", "room_stairway");

        /*
         * Place the player in this room.
         */
        Player player = new Player("room_hollow");

        WorldState worldState = new WorldState(player, List.of(hollow));
        TurnContext turnContext = new TurnContext(worldState, null);

        /*
         * Create a resolved "look" command.
         *
         * directObject is null because the player typed only:
         *   look
         */
        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.PERCEPTION,
                "look",
                null,
                null,
                List.of(),
                null
        );

        LookHandler handler = new LookHandler();

        String response = handler.handle(command, turnContext);

        /*
         * We use contains(...) instead of exact string equality because room
         * descriptions often grow over time.
         *
         * This keeps the test focused on behavior:
         *   - room description appears
         *   - object name appears
         *   - exit appears
         */
        assertTrue(response.contains("You awaken to the hollow echo of dripping water."));
        assertTrue(response.contains("You see:"));
        assertTrue(response.contains("rusted lantern"));
        assertTrue(response.contains("Exits:"));
        assertTrue(response.contains("down"));
    }

    @Test
    void shouldDescribeSpecificObjectWhenDirectObjectIsProvided() {
        /*
         * This represents:
         *   look lantern
         *
         * The resolver has already found the actual WorldObject, so LookHandler
         * should describe the object directly.
         */
        WorldObject lantern = new WorldObject(
                "item_lantern",
                "rusted lantern",
                "A rusted lantern lies cold against the stone.",
                List.of("lantern", "rusted lantern")
        );

        Room hollow = new Room(
                "room_hollow",
                "The Hollow of Orren",
                "You awaken in darkness."
        );

        Player player = new Player("room_hollow");

        WorldState worldState = new WorldState(player, List.of(hollow));
        TurnContext turnContext = new TurnContext(worldState, null);

        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.PERCEPTION,
                "look",
                lantern,
                null,
                List.of(),
                null
        );

        LookHandler handler = new LookHandler();

        String response = handler.handle(command, turnContext);

        assertEquals("A rusted lantern lies cold against the stone.", response);
    }

    @Test
    void shouldReturnFallbackWhenObjectHasBlankDescription() {
        /*
         * This test protects against empty object descriptions.
         *
         * If a world object has no useful description, the handler should still
         * return something readable instead of an empty string.
         */
        WorldObject stone = new WorldObject(
                "obj_stone",
                "stone wall",
                "",
                List.of("wall", "stone")
        );

        Room hollow = new Room(
                "room_hollow",
                "The Hollow of Orren",
                "A damp cave."
        );

        Player player = new Player("room_hollow");

        WorldState worldState = new WorldState(player, List.of(hollow));
        TurnContext turnContext = new TurnContext(worldState, null);

        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.PERCEPTION,
                "look",
                stone,
                null,
                List.of(),
                null
        );

        LookHandler handler = new LookHandler();

        String response = handler.handle(command, turnContext);

        assertEquals("You see nothing unusual about the stone wall.", response);
    }

    @Test
    void shouldReturnNowhereWhenCurrentRoomDoesNotExist() {
        /*
         * This is a defensive test.
         *
         * If the player points to a room id that does not exist in WorldState,
         * LookHandler should not crash.
         */
        Player player = new Player("missing_room");

        WorldState worldState = new WorldState(player, List.of());
        TurnContext turnContext = new TurnContext(worldState, null);

        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.PERCEPTION,
                "look",
                null,
                null,
                List.of(),
                null
        );

        LookHandler handler = new LookHandler();

        String response = handler.handle(command, turnContext);

        assertEquals("You are nowhere.", response);
    }

    @Test
    void shouldThrowWhenWrongVerbIsSentToLookHandler() {
        /*
         * This test catches router/dispatcher mistakes.
         *
         * LookHandler should only receive "look" commands.
         * If it receives "take", something upstream routed incorrectly.
         */
        Room hollow = new Room(
                "room_hollow",
                "The Hollow of Orren",
                "A damp cave."
        );

        Player player = new Player("room_hollow");

        WorldState worldState = new WorldState(player, List.of(hollow));
        TurnContext turnContext = new TurnContext(worldState, null);

        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.MANIPULATION,
                "take",
                null,
                null,
                List.of(),
                null
        );

        LookHandler handler = new LookHandler();

        assertThrows(IllegalArgumentException.class, () ->
                handler.handle(command, turnContext)
        );
    }
}