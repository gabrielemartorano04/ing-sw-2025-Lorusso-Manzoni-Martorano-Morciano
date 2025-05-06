package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;
import it.polimi.ingsw.net.socket.events.EventType;

/**
 * Event client to server. The first player chooses the players number
 */

public class CreateGameEvent extends Event {
    private final int numberOfPlayers;

    public CreateGameEvent(int numberOfPlayers) {
        super(EventType.CREATE_GAME);
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    @Override
    public String toString() {
        return "CreateGameEvent{ numberOfPlayers=" + numberOfPlayers + " }";
    }
}

