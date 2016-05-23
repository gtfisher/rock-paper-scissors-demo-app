package com.russmiles.antifragilesoftware.samples.tests;

import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;
import com.russmiles.antifragilesoftware.samples.es.impl.ApplicationService;
import com.russmiles.antifragilesoftware.samples.es.store.EventStream;
import com.russmiles.antifragilesoftware.samples.rps.command.CreateGameCommand;
import com.russmiles.antifragilesoftware.samples.rps.command.MakeMoveCommand;
import com.russmiles.antifragilesoftware.samples.rps.event.GameTiedEvent;
import com.russmiles.antifragilesoftware.samples.rps.event.GameWonEvent;
import com.russmiles.antifragilesoftware.samples.rps.game.Game;
import com.russmiles.antifragilesoftware.samples.es.store.memory.InMemoryEventStore;
import com.russmiles.antifragilesoftware.samples.rps.Move;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.fail;

/**
 * Created by gtarrant-fisher on 13/05/2016.
 */
public class GameIntegrationTest {
    InMemoryEventStore eventStore = new InMemoryEventStore();
    ApplicationService application = new ApplicationService(eventStore, Game.class);
    UUID gameId = UUID.randomUUID();
    String player1 = "player1";
    String player2 = "player2";

    @Test
    public void tieRock() throws Exception {
        application.handle(new CreateGameCommand(gameId,player1));
        application.handle(new MakeMoveCommand(gameId,player1, Move.rock));
        application.handle(new MakeMoveCommand(gameId,player2, Move.rock));
        assertEventStreamContains(gameId, new GameTiedEvent(gameId));
    }
    @Test
    public void tiePaper() throws Exception {
        application.handle(new CreateGameCommand(gameId,player1));
        application.handle(new MakeMoveCommand(gameId,player1, Move.paper));
        application.handle(new MakeMoveCommand(gameId,player2, Move.paper));
        assertEventStreamContains(gameId, new GameTiedEvent(gameId));
    }

    @Test
    public void victoryP1RockP2Paper() throws Exception {
        application.handle(new CreateGameCommand(gameId, player1));
        application.handle(new MakeMoveCommand(gameId, player1, Move.rock));
        application.handle(new MakeMoveCommand(gameId, player2, Move.paper));
        assertEventStreamContains(gameId, new GameWonEvent(gameId, player2, player1));
    }

    @Test
    public void victoryP1RockP2Scissor() throws Exception {
        application.handle(new CreateGameCommand(gameId, player1));
        application.handle(new MakeMoveCommand(gameId, player1, Move.rock));
        application.handle(new MakeMoveCommand(gameId, player2, Move.scissor));
        assertEventStreamContains(gameId, new GameWonEvent(gameId, player1, player2));
    }

    @Test
    public void victoryP1ScissorP2Paper() throws Exception {
        application.handle(new CreateGameCommand(gameId, player1));
        application.handle(new MakeMoveCommand(gameId, player1, Move.scissor));
        application.handle(new MakeMoveCommand(gameId, player2, Move.paper));
        assertEventStreamContains(gameId, new GameWonEvent(gameId, player1, player2));
    }

    @Test(expected=IllegalArgumentException.class)
    public void same_player_should_fail() throws Exception {
        application.handle(new CreateGameCommand(gameId, player1));
        application.handle(new MakeMoveCommand(gameId, player1, Move.rock));
        application.handle(new MakeMoveCommand(gameId, player1, Move.rock));
    }

    @Test(expected=IllegalStateException.class)
    public void game_not_started() throws Exception {
        application.handle(new MakeMoveCommand(gameId, player1, Move.rock));
    }

    @Test(expected=IllegalStateException.class)
    public void move_after_end_should_fail() throws Exception {
        application.handle(new CreateGameCommand(gameId, player1));
        application.handle(new MakeMoveCommand(gameId, player1, Move.rock));
        application.handle(new MakeMoveCommand(gameId, player2, Move.rock));
        application.handle(new MakeMoveCommand(gameId, "another", Move.rock));
    }



    private void assertEventStreamContains(UUID streamId, BaseEvent expectedBaseEvent) {
        EventStream<Long> eventStream = eventStore.loadEventStream(gameId);
        String expected = EventStringUtil.toString(expectedBaseEvent);
        System.out.println ("expectedBaseEvent->" + expected);
        for (BaseEvent baseEvent : eventStream) {
            String es = EventStringUtil.toString(baseEvent);
            System.out.println ("BaseEvent->" + es);
            if (es.equals(expected)) return;
        }
        fail("Expected event did not occur: " + expected);
    }


}
