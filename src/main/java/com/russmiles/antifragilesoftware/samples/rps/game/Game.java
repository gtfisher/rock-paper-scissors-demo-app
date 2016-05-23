package com.russmiles.antifragilesoftware.samples.rps.game;

import com.russmiles.antifragilesoftware.samples.rps.command.CreateGameCommand;
import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;
import com.russmiles.antifragilesoftware.samples.rps.Move;
import com.russmiles.antifragilesoftware.samples.rps.command.MakeMoveCommand;
import com.russmiles.antifragilesoftware.samples.rps.event.GameWonEvent;
import com.russmiles.antifragilesoftware.samples.rps.event.MoveDecidedEvent;
import com.russmiles.antifragilesoftware.samples.rps.event.GameCreatedEvent;
import com.russmiles.antifragilesoftware.samples.rps.event.GameTiedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Game {
    enum State {
        notInitalized, created, waiting, tied, won
    }
    private State state = State.notInitalized;
    private String player;
    private Move move;

    public List<? extends BaseEvent> handle(CreateGameCommand c) {
        if (state != State.notInitalized) throw new IllegalStateException(state.toString());
        return Arrays.asList(
                new GameCreatedEvent(c.gameId, c.playerEmail));
    }

    public List<? extends BaseEvent> handle(MakeMoveCommand c) {
        if (State.created == state) {
            return Arrays.asList(new MoveDecidedEvent(c.gameId, c.playerEmail, c.move));
        } else if (State.waiting == state) {
            if (player.equals(c.playerEmail)) throw new IllegalArgumentException("Player already in game");
            return Arrays.asList(
                    new MoveDecidedEvent(c.gameId, c.playerEmail, c.move),
                    makeEndGameEvent(c.gameId, c.playerEmail, c.move));
        } else {
            throw new IllegalStateException(state.toString());
        }
    }

    private BaseEvent makeEndGameEvent(UUID gameId, String opponentEmail, Move opponentMove) {
        if (move.defeats(opponentMove)) {
            return new GameWonEvent(gameId, player, opponentEmail);
        } else if (opponentMove.defeats(move)) {
            return new GameWonEvent(gameId, opponentEmail, player);
        } else {
            return new GameTiedEvent(gameId);
        }
    }

    public void handle(GameCreatedEvent e) {
        state = State.created;
    }

    public void handle(MoveDecidedEvent e) {
        if (state == State.created) {
            move = e.move;
            player = e.playerEmail;
            state = State.waiting;
        }
    }

    public void handle(GameWonEvent e) {
        state = State.won;
    }

    public void handle(GameTiedEvent e) {
        state = State.tied;
    }
}