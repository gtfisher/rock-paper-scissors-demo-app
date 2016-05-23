package com.russmiles.antifragilesoftware.samples.rps.event;



import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;
import com.russmiles.antifragilesoftware.samples.rps.Move;

import java.util.UUID;

public class MoveDecidedEvent implements BaseEvent {

	public final UUID gameId;
	public final String playerEmail;
	public final Move move;

	public MoveDecidedEvent(UUID gameId, String playerEmail, Move move) {
		this.gameId = gameId;
		this.playerEmail = playerEmail;
		this.move = move;
	}
	
	public MoveDecidedEvent() {
		gameId = null;
		playerEmail = null;
		move = null;
	}

	@Override
	public String toString() {
		return "MoveDecidedEvent{" +
				"gameId=" + gameId +
				", playerEmail='" + playerEmail + '\'' +
				", move=" + move +
				'}';
	}

	@Override
	public UUID getUUID() {
		return gameId;
	}
}
