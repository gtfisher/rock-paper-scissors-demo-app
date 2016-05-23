package com.russmiles.antifragilesoftware.samples.rps.event;

import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;

import java.util.UUID;

/**
 * Created by gtarrant-fisher on 13/05/2016.
 */
public class GameCreatedEvent implements BaseEvent {
    public final UUID gameId;
    public final String playerEmail;

    public GameCreatedEvent(UUID gameId, String playerEmail) {
        this.gameId = gameId;
        this.playerEmail = playerEmail;
    }

    @Override
    public String toString() {
        return "GameCreatedEvent{" +
                "gameId=" + gameId +
                ", playerEmail='" + playerEmail + '\'' +
                '}';
    }

    @Override
    public UUID getUUID() {
        return gameId;
    }
}
