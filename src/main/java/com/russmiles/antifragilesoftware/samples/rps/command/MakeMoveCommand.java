package com.russmiles.antifragilesoftware.samples.rps.command;

import com.russmiles.antifragilesoftware.samples.es.api.Command;
import com.russmiles.antifragilesoftware.samples.rps.Move;


import java.util.UUID;

public class MakeMoveCommand implements Command {
	public final UUID gameId;
	public final String playerEmail;
	public final Move move;

	public MakeMoveCommand(UUID gameId, String playerEmail, Move move) {
		if (gameId == null) throw new IllegalArgumentException("gameId must not be null");
		if (playerEmail == null) throw new IllegalArgumentException("playerEmail must not be null");
		if (move == null) throw new IllegalArgumentException("move must not be null");
		this.gameId = gameId;
		this.playerEmail = playerEmail;
		this.move = move;
		
	}

	@Override
	public UUID aggregateId() {
		return gameId;
	}
}
