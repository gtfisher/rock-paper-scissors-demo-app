package com.russmiles.antifragilesoftware.samples.rps.game;


import com.russmiles.antifragilesoftware.samples.rps.event.*;
import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;
import com.russmiles.antifragilesoftware.samples.rps.Move;
import com.russmiles.antifragilesoftware.samples.rps.event.GameCreatedEvent;
import com.russmiles.antifragilesoftware.samples.rps.event.GameTiedEvent;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GamesProjection implements Consumer<Event<BaseEvent>> {


	Logger logger = Logger.getLogger(GamesProjection.class.getName());

	public static enum State {
		inProgress(false), 
		won(true), 
		tied(true);
		
		public final boolean completed;

		private State(boolean completed) {
			this.completed = completed;
		}
	}
	public static class GameState {
		public UUID gameId;
		public String createdBy;
		public Map<String, Move> moves = new HashMap<String, Move>();
		public State state;
		public String winner;
		public String loser;
	}
	private Map<UUID, GameState> games = new HashMap<>();
	
	public GameState get(UUID gameId) {
		return games.get(gameId);
	}

	public List<UUID> get () {
		List<UUID> list = new ArrayList<UUID>();
		return list;
	}

    @Override
    public void accept(Event<BaseEvent> baseEventEvent) {
        logger.log(Level.INFO, "Accept Event ${0}", baseEventEvent);

    }


	public void handle(GameCreatedEvent e) {
		GameState game = new GameState();
		game.gameId = e.gameId;
		game.state = State.inProgress;
		games.put(e.gameId, game);
	}
	
	public void handle(MoveDecidedEvent e) {
		GameState game = games.get(e.gameId);
		game.moves.put(e.playerEmail, e.move);
	}

	public void handle(GameWonEvent e) {
		GameState game = games.get(e.gameId);
		game.state = State.won;
		game.winner = e.winnerEmail;
		game.loser = e.loserEmail;
	}

	public void handle(GameTiedEvent e) {
		GameState game = games.get(e.gameId);
		game.state = State.tied;
	}
}
