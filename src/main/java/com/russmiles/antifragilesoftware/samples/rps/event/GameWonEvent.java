package com.russmiles.antifragilesoftware.samples.rps.event;



import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;

import java.util.UUID;

public class GameWonEvent implements BaseEvent {
	public final UUID gameId;
	public final String winnerEmail;
	public final String loserEmail;

	public GameWonEvent(UUID gameId, String winnerEmail, String loserEmail) {
		this.gameId = gameId;
		this.winnerEmail = winnerEmail;
		this.loserEmail = loserEmail;
	}
	
	public GameWonEvent() {
		gameId = null;
		winnerEmail = null;
		loserEmail = null;
	}

	@Override
	public String toString() {
		return "GameWonEvent{" +
				"gameId=" + gameId +
				", winnerEmail='" + winnerEmail + '\'' +
				", loserEmail='" + loserEmail + '\'' +
				'}';
	}

	@Override
	public UUID getUUID() {
		return gameId;
	}
}
