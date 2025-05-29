package it.polimi.ingsw.net.socket.events.clientevents.setupevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

/**
 * Event client to server. The first player chooses the players number
 */

public class CreateGameEvent extends ClientEvent {
    private final int numberOfPlayers;
    private final int gameLevel;
    public CreateGameEvent(int numberOfPlayers, int gameLevel) {

        this.numberOfPlayers = numberOfPlayers;
        this.gameLevel = gameLevel;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getGameLevel() {return gameLevel;}

    @Override
    public String getMessage() {
        return "Create game, num of players: " + numberOfPlayers;
    }

    @Override
    public ServerEvent call(VirtualServer visitor){
        return visitor.visit(this);
    };

}

