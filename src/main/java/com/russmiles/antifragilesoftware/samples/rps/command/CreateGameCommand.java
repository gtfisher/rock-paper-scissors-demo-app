package com.russmiles.antifragilesoftware.samples.rps.command;

import com.russmiles.antifragilesoftware.samples.es.api.Command;

import java.util.UUID;

/**
 * Created by gtarrant-fisher on 12/05/2016.
 */
public class CreateGameCommand implements Command {
    public final UUID gameId;
    public final String playerEmail;
//    public final Move move;

//    public CreateGameCommand(UUID gameId, String playerEmail, Move move) {
//        this.gameId = gameId;
//        this.playerEmail = playerEmail;
//        this.move = move;
//    }

    public CreateGameCommand(UUID gameId, String playerEmail) {
        if (gameId == null) throw new IllegalArgumentException("gameId must not be null");
        if (playerEmail == null) throw new IllegalArgumentException("playerEmail must not be null");
        this.gameId = gameId;
        this.playerEmail = playerEmail;
    }

    public UUID aggregateId() {
        return gameId;
    }

}
