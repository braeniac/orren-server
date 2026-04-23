package com.braeniac.orren_engine.engine.model.resolver;

import com.braeniac.orren_engine.engine.model.CommandModifier;
import com.braeniac.orren_engine.engine.model.CommandTarget;
import com.braeniac.orren_engine.engine.model.Model;
import com.braeniac.orren_engine.engine.model.UserCommand;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.resolver.Resolver;
import com.braeniac.orren_engine.engine.world.model.WorldObject;
import com.braeniac.orren_engine.engine.world.state.TurnContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests verify that the resolver converts a text-based UserCommand
 * into a world-object-based ResolvedCommand.
 *
 * Why these tests matter:
 * -----------------------
 * The parser only gives us language structure:
 *   - verb = "unlock"
 *   - direct target = "door"
 *   - modifier target = "key"
 *
 * The resolver is the first layer that interacts with actual game objects.
 * So if these tests fail, your engine may parse correctly but still act on
 * the wrong object or fail to act at all.
 */
class SimpleReferenceResolverTest {

    private final Resolver resolver = new Resolver();

    @Test
    void shouldResolveDirectObjectFromVisibleObjects() {
        // This object is visible in the room and should match the player's target "door".
        WorldObject ironDoor = new WorldObject(
                "door-1",
                "iron door",
                List.of("door")
        );

        TurnContext turnContext = new TurnContext(
                List.of(ironDoor), // visible objects
                List.of(),         // inventory objects
                null               // last referenced object
        );

        // Represents a command like:
        // open the iron door
        UserCommand command = new UserCommand(
                Model.MANIPULATION,
                "open",
                new CommandTarget("the", List.of("iron"), "door", null),
                null,
                List.of(),
                null
        );

        ResolvedCommand resolved = resolver.resolve(command, turnContext);

        assertNotNull(resolved);
        assertEquals("open", resolved.getVerb());
        assertSame(ironDoor, resolved.getDirectObject());
        assertTrue(resolved.getModifiersList().isEmpty());
    }

    @Test
    void shouldResolveModifierTargetFromInventory() {
        // The player has the bronze key in inventory.
        WorldObject bronzeKey = new WorldObject(
                "key-1",
                "small bronze key",
                List.of("key", "bronze key")
        );

        // The door is visible in the room.
        WorldObject ironDoor = new WorldObject(
                "door-1",
                "iron door",
                List.of("door")
        );

        TurnContext turnContext = new TurnContext(
                List.of(ironDoor),
                List.of(bronzeKey),
                null
        );

        // Represents:
        // unlock door with key
        UserCommand command = new UserCommand(
                Model.MANIPULATION,
                "unlock",
                new CommandTarget(null, List.of(), "door", null),
                null,
                List.of(
                        new CommandModifier(
                                new CommandTarget(null, List.of(), "key", null),
                                "with"
                        )
                ),
                null
        );

        ResolvedCommand resolved = resolver.resolve(command, turnContext);

        assertNotNull(resolved);
        assertSame(ironDoor, resolved.getDirectObject());
        assertEquals(1, resolved.getModifiersList().size());
        assertEquals("with", resolved.getModifiersList().get(0).getPreposition());
        assertSame(bronzeKey, resolved.getModifiersList().get(0).getTarget());
    }

    @Test
    void shouldResolvePronounItUsingLastReferencedObject() {
        // This is the object previously referenced in the conversation/turn flow.
        WorldObject bronzeKey = new WorldObject(
                "key-1",
                "small bronze key",
                List.of("key", "bronze key")
        );

        WorldObject ironDoor = new WorldObject(
                "door-1",
                "iron door",
                List.of("door")
        );

        TurnContext turnContext = new TurnContext(
                List.of(ironDoor),
                List.of(bronzeKey),
                bronzeKey // "it" should resolve to this
        );

        // Represents:
        // unlock door with it
        UserCommand command = new UserCommand(
                Model.MANIPULATION,
                "unlock",
                new CommandTarget(null, List.of(), "door", null),
                null,
                List.of(
                        new CommandModifier(
                                new CommandTarget(null, List.of(), null, "it"),
                                "with"
                        )
                ),
                null
        );

        ResolvedCommand resolved = resolver.resolve(command, turnContext);

        assertNotNull(resolved);
        assertSame(ironDoor, resolved.getDirectObject());
        assertEquals(1, resolved.getModifiersList().size());
        assertSame(bronzeKey, resolved.getModifiersList().get(0).getTarget());
    }

    @Test
    void shouldReturnNullDirectObjectWhenNothingMatches() {
        TurnContext turnContext = new TurnContext(
                List.of(),
                List.of(),
                null
        );

        // Represents:
        // open door
        // But there is no door in scope.
        UserCommand command = new UserCommand(
                Model.MANIPULATION,
                "open",
                new CommandTarget(null, List.of(), "door", null),
                null,
                List.of(),
                null
        );

        ResolvedCommand resolved = resolver.resolve(command, turnContext);

        assertNotNull(resolved);
        assertNull(resolved.getDirectObject());
    }

    @Test
    void shouldPreferBetterDescriptorMatchWhenMultipleObjectsShareSameHead() {
        // Both objects match the head noun "key",
        // but only one matches the descriptor "bronze".
        WorldObject bronzeKey = new WorldObject(
                "key-1",
                "small bronze key",
                List.of("key", "bronze key")
        );

        WorldObject ironKey = new WorldObject(
                "key-2",
                "small iron key",
                List.of("key", "iron key")
        );

        TurnContext turnContext = new TurnContext(
                List.of(bronzeKey, ironKey),
                List.of(),
                null
        );

        // Represents:
        // take bronze key
        UserCommand command = new UserCommand(
                Model.MANIPULATION,
                "take",
                new CommandTarget(null, List.of("bronze"), "key", null),
                null,
                List.of(),
                null
        );

        ResolvedCommand resolved = resolver.resolve(command, turnContext);

        assertNotNull(resolved);
        assertSame(bronzeKey, resolved.getDirectObject());
    }

    @Test
    void shouldResolveQuotedSpeechWithoutTryingToResolveTargets() {
        TurnContext turnContext = new TurnContext(
                List.of(),
                List.of(),
                null
        );

        // Represents:
        // say "open the gate"
        UserCommand command = new UserCommand(
                Model.SPEECH,
                "say",
                null,
                "open the gate",
                List.of(),
                null
        );

        ResolvedCommand resolved = resolver.resolve(command, turnContext);

        assertNotNull(resolved);
        assertEquals(Model.SPEECH, resolved.getDomain());
        assertEquals("say", resolved.getVerb());
        assertNull(resolved.getDirectObject());
        assertEquals("open the gate", resolved.getQuotedText());
        assertTrue(resolved.getModifiersList().isEmpty());
    }
}