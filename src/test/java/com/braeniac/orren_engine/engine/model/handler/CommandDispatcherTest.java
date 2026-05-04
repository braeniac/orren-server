package com.braeniac.orren_engine.engine.model.handler;


import com.braeniac.orren_engine.engine.handler.Dispatcher;
import com.braeniac.orren_engine.engine.handler.TakeHandler;
import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.model.*;
import com.braeniac.orren_engine.engine.world.state.TurnContext;
import com.braeniac.orren_engine.engine.world.state.WorldState;
import org.h2.command.Command;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the command dispatcher.
 *
 * Why this test matters:
 * ----------------------
 * Up to this point, we have tested individual pieces:
 * - parser
 * - resolver
 * - command model
 * - handlers
 *
 * But the dispatcher is the piece that connects a resolved command
 * to the correct handler.
 *
 * This test proves that:
 *
 *   ResolvedCommand("take", lantern)
 *       -> CommandDispatcher
 *       -> TakeHandler
 *       -> world state changes
 *
 * In other words, this test proves that command execution is actually wired together.
 */
class CommandDispatcherTest {

    @Test
    void shouldDispatchTakeCommandToTakeHandlerAndMoveObjectToInventory() {


        //create a portable (able to pick up) lantern
        WorldObject lantern = new TestPortableItem(
                "item_lantern",
                "rusted lantern",
                "A rusted lantern lies cold against the stone.",
                List.of("lantern", "rusted lantern")
        );

        //create the starting room
        Room hollow = new Room(
                "room_hollow",
                "The Hollow of Orren",
                "You awaken to the hollow echo of dripping water."
        );

        //add object to the hollow of orren.
        hollow.addObject(lantern);


        //create the player in the starting room
        //at this point the player has an empty inventory.
        Player player = new Player("room_hollow");

        //the WorldState ties the player and the room together.
        //tha handler mutates this.
        WorldState worldState = new WorldState(
                player,
                List.of(hollow)
        );

        //the TurnContext gives the handler access to the current world state
        //NOTE: lastReferencedObject is null because this is the very first command.
        TurnContext turnContext = new TurnContext(worldState, null);

        //create a resolved command manually.
        //test focused on the dispatcher (missing steps : lexer/parser/resolver).
        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.MANIPULATION,
                "take",
                lantern,
                null,
                List.of(),
                null
        );

        //register the TakeHandler with the dispatcher
        // "verb:take" so "TakeHandler".
        Dispatcher dispatcher = new Dispatcher(
                List.of(new TakeHandler())
        );

        //Expected behaviour:
        // - dispatcher finds TakeHandler
        // - TakeHandler removes the lantern from the room
        // - TakeHandler adds lantern to players inventory.
        String response = dispatcher.dispatch(command, turnContext);

        //test the phrase the players receive
        assertEquals("You take the rusted lantern.", response);

        //the lantern should no longer be in the room!!
        assertFalse(hollow.getObjects().contains(lantern));

        //verify that the players inventory now has the lantern.
        assertTrue(player.getInventory().contains(lantern));
    }

    @Test
    void shouldReturnUnknownCommandMessageWhenNoHandlerExists() {
        /*
         * This test proves the dispatcher fails gracefully.
         *
         * If the engine receives a resolved command but has no registered handler
         * for that verb, it should return a player-friendly message instead of crashing.
         */
        Room hollow = new Room(
                "room_hollow",
                "The Hollow of Orren",
                "You awaken to the hollow echo of dripping water."
        );

        Player player = new Player("room_hollow");

        WorldState worldState = new WorldState(
                player,
                List.of(hollow)
        );

        TurnContext turnContext = new TurnContext(worldState, null);

        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.UNKNOWN,
                "dance",
                null,
                null,
                List.of(),
                null
        );

        /*
         * Dispatcher has no handlers registered.
         */
        Dispatcher dispatcher = new Dispatcher(List.of());

        String response = dispatcher.dispatch(command, turnContext);

        assertEquals("I don't know how to 'dance'.", response);
    }

    /**
     * Tiny test-only portable item.
     *
     * Why this exists:
     * ----------------
     * Item by itself is just a world object.
     * To test TakeHandler properly, we need an item that implements Portable.
     *
     * In the real game, you may eventually create classes like:
     * - Lantern extends Item implements Portable
     * - BronzeKey extends Item implements Portable
     *
     * But for this test, a small local class is enough.
     */
    private static final class TestPortableItem extends Item implements Portable {

        private TestPortableItem(
                String id,
                String name,
                String description,
                List<String> aliases
        ) {
            super(id, name, description, aliases);
        }
    }
}
