package com.russmiles.antifragilesoftware.samples.services.api.objects;

import com.russmiles.antifragilesoftware.samples.rps.Move;

import java.util.UUID;

/**
 * Created by gtarrant-fisher on 13/05/2016.
 */
public class GameDto {

    private  UUID gameId;
    private  String playerEmail;
    private  Move move;


    public GameDto(UUID gameId, String playerEmail, Move move) {
        this.gameId = gameId;
        this.playerEmail = playerEmail;
        this.move = move;
    }

    public GameDto() {
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public String getPlayerEmail() {
        return playerEmail;
    }

    public void setPlayerEmail(String playerEmail) {
        this.playerEmail = playerEmail;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }
}
