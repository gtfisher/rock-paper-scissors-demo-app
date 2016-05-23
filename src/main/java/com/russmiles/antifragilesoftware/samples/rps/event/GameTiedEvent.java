package com.russmiles.antifragilesoftware.samples.rps.event;


import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;

import java.util.UUID;

public class GameTiedEvent implements BaseEvent {
	public final UUID gameId;
	
	GameTiedEvent() {
		gameId = null;
	}

	public GameTiedEvent(UUID gameId) {
		this.gameId = gameId;
	}

	@Override
	public String toString() {
		return "GameTiedEvent{" +
				"gameId=" + gameId +
				'}';
	}

	@Override
	public UUID getUUID() {
		return gameId;
	}
}
