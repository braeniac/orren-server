import com.braeniac.orren_engine.engine.handler.Handler;
import com.braeniac.orren_engine.engine.handler.OpenHandler;
import com.braeniac.orren_engine.engine.model.CommandDomain;
import com.braeniac.orren_engine.engine.resolver.ResolvedCommand;
import com.braeniac.orren_engine.engine.world.model.Player;
import com.braeniac.orren_engine.engine.world.model.Room;
import com.braeniac.orren_engine.engine.world.model.WorldObject;
import com.braeniac.orren_engine.engine.world.state.TurnContext;
import com.braeniac.orren_engine.engine.world.state.WorldState;
import com.braeniac.orren_engine.engine.world.model.Openable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests OpenHandler.
 *
 * Why this matters:
 * -----------------
 * "Open" is the first handler where object capability matters.
 *
 * The handler should not open objects just because their name says "chest" or "door".
 * It should only open objects that implement Openable.
 */
class OpenHandlerTest {

    @Test
    void shouldOpenClosedOpenableObject() {
        TestOpenableChest chest = new TestOpenableChest();

        Room room = new Room(
                "room_hollow",
                "The Hollow of Orren",
                "You awaken to the hollow echo of dripping water."
        );

        room.addObject(chest);

        Player player = new Player("room_hollow");

        WorldState worldState = new WorldState(player, List.of(room));
        TurnContext turnContext = new TurnContext(worldState, null);

        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.MANIPULATION,
                "open",
                chest,
                null,
                List.of(),
                null
        );

        OpenHandler handler = new OpenHandler();

        String response = handler.handle(command, turnContext);

        assertEquals("You open the old chest.", response);
        assertTrue(chest.isOpen());
    }

    @Test
    void shouldSayAlreadyOpenWhenObjectIsAlreadyOpen() {
        TestOpenableChest chest = new TestOpenableChest();
        chest.open();

        WorldState worldState = new WorldState(
                new Player("room_hollow"),
                List.of(new Room("room_hollow", "The Hollow of Orren", "A damp cave."))
        );

        TurnContext turnContext = new TurnContext(worldState, null);

        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.MANIPULATION,
                "open",
                chest,
                null,
                List.of(),
                null
        );

        OpenHandler handler = new OpenHandler();

        String response = handler.handle(command, turnContext);

        assertEquals("The old chest is already open.", response);
        assertTrue(chest.isOpen());
    }

    @Test
    void shouldRejectNonOpenableObject() {
        WorldObject stone = new WorldObject(
                "obj_stone",
                "stone wall",
                "A cold stone wall.",
                List.of("wall", "stone")
        );

        WorldState worldState = new WorldState(
                new Player("room_hollow"),
                List.of(new Room("room_hollow", "The Hollow of Orren", "A damp cave."))
        );

        TurnContext turnContext = new TurnContext(worldState, null);

        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.MANIPULATION,
                "open",
                stone,
                null,
                List.of(),
                null
        );

        OpenHandler handler = new OpenHandler();

        String response = handler.handle(command, turnContext);

        assertEquals("You can't open the stone wall.", response);
    }

    @Test
    void shouldAskOpenWhatWhenNoDirectObject() {
        WorldState worldState = new WorldState(
                new Player("room_hollow"),
                List.of(new Room("room_hollow", "The Hollow of Orren", "A damp cave."))
        );

        TurnContext turnContext = new TurnContext(worldState, null);

        ResolvedCommand command = new ResolvedCommand(
                CommandDomain.MANIPULATION,
                "open",
                null,
                null,
                List.of(),
                null
        );

        OpenHandler handler = new OpenHandler();

        String response = handler.handle(command, turnContext);

        assertEquals("Open what?", response);
    }

    /**
     * Small test object used only for this test.
     *
     * It extends WorldObject because it exists in the world.
     * It implements Openable because OpenHandler should be allowed to open it.
     */
    private static final class TestOpenableChest extends WorldObject implements Openable {

        private boolean open;

        private TestOpenableChest() {
            super(
                    "obj_chest",
                    "old chest",
                    "An old wooden chest rests against the damp wall.",
                    List.of("chest", "old chest")
            );

            this.open = false;
        }

        @Override
        public boolean isOpen() {
            return open;
        }

        @Override
        public void open() {
            this.open = true;
        }

        @Override
        public void close() {
            this.open = false;
        }
    }
}