package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.Event;

public class CreateGameEvent extends Event {
    private final int numberOfPlayers;

    public CreateGameEvent(int numberOfPlayers) {
        super(ClientEventType.CREATE_GAME);
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

