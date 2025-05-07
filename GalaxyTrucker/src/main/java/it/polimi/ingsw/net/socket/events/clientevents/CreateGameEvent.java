package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

/**
 * Event client to server. The first player chooses the players number
 */

public class CreateGameEvent extends ClientEvent {
    private final int numberOfPlayers;

    public CreateGameEvent(int numberOfPlayers) {
        super(ClientEventType.CREATE_GAME);
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    @Override
    public String getMessage() {
        return "Create game, num of players: " + numberOfPlayers;
    }

    @Override
    public void call(ClientVisitor visitor){
        visitor.visit(this);
    };

}

