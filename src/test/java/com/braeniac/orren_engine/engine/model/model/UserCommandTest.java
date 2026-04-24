

import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.model.CommandModifier;
import com.braeniac.orren_engine.engine.model.CommandTarget;
import com.braeniac.orren_engine.engine.model.UserCommand;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the UserCommand model and the small model objects it depends on.
 *
 * Why these tests matter:
 * -----------------------
 * UserCommand sits between parsing and resolution/execution.
 * Even though it is mostly a data holder, it is still important to verify:
 *
 * 1. constructor values are stored correctly
 * 2. helper methods behave as expected
 * 3. defensive copying works
 * 4. null validation works for required fields
 *
 * These tests are intentionally simple because model classes should be predictable.
 */
class UserCommandTest {

    @Test
    void shouldCreateManipulationUserCommandWithDirectTargetAndModifier() {
        // This target represents a simple direct object like:
        // "door"
        CommandTarget directTarget = new CommandTarget(
                null,
                List.of(),
                "door",
                null
        );

        // This target represents a modifier object like:
        // "key"
        CommandTarget modifierTarget = new CommandTarget(
                null,
                List.of(),
                "key",
                null
        );

        // Important:
        // In your current codebase, CommandModifier is constructed as:
        //   new CommandModifier(CommandTarget target, String preposition)
        //
        // So "with key" is represented as:
        //   target = key
        //   preposition = "with"
        CommandModifier modifier = new CommandModifier(
                modifierTarget,
                "with"
        );

        UserCommand command = new UserCommand(
                CommandDomain.MANIPULATION,
                "unlock",
                directTarget,
                null,
                List.of(modifier),
                null
        );

        assertEquals(CommandDomain.MANIPULATION, command.getDomain());
        assertEquals("unlock", command.getVerb());
        assertSame(directTarget, command.getDirectTarget());
        assertNull(command.getQuotedText());
        assertEquals(1, command.getModifierList().size());
        assertSame(modifier, command.getModifierList().get(0));
        assertNull(command.getAdverb());

        assertTrue(command.hasDirectTarget());
        assertFalse(command.hasQuotedText());
    }

    @Test
    void shouldCreateSpeechUserCommandWithQuotedText() {
        // Speech commands do not need a direct target.
        // Example:
        //   say "hello"
        UserCommand command = new UserCommand(
                CommandDomain.SPEECH,
                "say",
                null,
                "hello",
                List.of(),
                null
        );

        assertEquals(CommandDomain.SPEECH, command.getDomain());
        assertEquals("say", command.getVerb());
        assertNull(command.getDirectTarget());
        assertEquals("hello", command.getQuotedText());
        assertTrue(command.getModifierList().isEmpty());
        assertNull(command.getAdverb());

        assertFalse(command.hasDirectTarget());
        assertTrue(command.hasQuotedText());
    }

    @Test
    void shouldCreateUserCommandWithAdverb() {
        // Example:
        //   open door softly
        CommandTarget directTarget = new CommandTarget(
                null,
                List.of(),
                "door",
                null
        );

        UserCommand command = new UserCommand(
                CommandDomain.MANIPULATION,
                "open",
                directTarget,
                null,
                List.of(),
                "softly"
        );

        assertEquals(CommandDomain.MANIPULATION, command.getDomain());
        assertEquals("open", command.getVerb());
        assertSame(directTarget, command.getDirectTarget());
        assertEquals("softly", command.getAdverb());
    }

    @Test
    void shouldDefensivelyCopyModifierList() {
        CommandModifier modifier = new CommandModifier(
                new CommandTarget(null, List.of(), "key", null),
                "with"
        );

        List<CommandModifier> originalList = new ArrayList<>();
        originalList.add(modifier);

        UserCommand command = new UserCommand(
                CommandDomain.MANIPULATION,
                "unlock",
                new CommandTarget(null, List.of(), "door", null),
                null,
                originalList,
                null
        );

        // Mutating the original list after construction should NOT affect
        // the command's internal modifier list because UserCommand uses List.copyOf(...).
        originalList.clear();

        assertEquals(1, command.getModifierList().size());
        assertSame(modifier, command.getModifierList().get(0));
    }

    @Test
    void shouldExposeUnmodifiableModifierList() {
        UserCommand command = new UserCommand(
                CommandDomain.MANIPULATION,
                "look",
                null,
                null,
                List.of(),
                null
        );

        // Because List.copyOf(...) is used in the constructor,
        // the returned list should be unmodifiable.
        assertThrows(UnsupportedOperationException.class, () ->
                command.getModifierList().add(
                        new CommandModifier(
                                new CommandTarget(null, List.of(), "key", null),
                                "with"
                        )
                )
        );
    }

    @Test
    void shouldThrowWhenDomainIsNull() {
        assertThrows(NullPointerException.class, () ->
                new UserCommand(
                        null,
                        "take",
                        null,
                        null,
                        List.of(),
                        null
                )
        );
    }

    @Test
    void shouldThrowWhenVerbIsNull() {
        assertThrows(NullPointerException.class, () ->
                new UserCommand(
                        CommandDomain.MANIPULATION,
                        null,
                        null,
                        null,
                        List.of(),
                        null
                )
        );
    }

    @Test
    void shouldThrowWhenModifierListIsNull() {
        assertThrows(NullPointerException.class, () ->
                new UserCommand(
                        CommandDomain.MANIPULATION,
                        "take",
                        null,
                        null,
                        null,
                        null
                )
        );
    }

    @Test
    void shouldSupportPronounDirectTarget() {
        // This verifies UserCommand can carry a pronoun-shaped target such as "it".
        CommandTarget pronounTarget = new CommandTarget(
                null,
                List.of(),
                null,
                "it"
        );

        UserCommand command = new UserCommand(
                CommandDomain.MANIPULATION,
                "use",
                pronounTarget,
                null,
                List.of(),
                null
        );

        assertNotNull(command.getDirectTarget());
        assertTrue(command.getDirectTarget().isPronoun());
        assertEquals("it", command.getDirectTarget().getPronoun());
        assertNull(command.getDirectTarget().getHead());
    }
}