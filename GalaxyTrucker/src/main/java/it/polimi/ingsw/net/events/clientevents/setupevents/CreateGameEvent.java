package it.polimi.ingsw.net.events.clientevents.setupevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

/**
 * Represents an event triggered by a client to request the creation of a new game
 */
public class CreateGameEvent extends ClientEvent {

    /**
     * Number of players
     */
    private final int numberOfPlayers;

    /**
     * Level of the game
     */
    private final int gameLevel;

    /**
     * Event constructor
     * @param numberOfPlayers number of payers
     * @param gameLevel level of the game
     */
    public CreateGameEvent(int numberOfPlayers, int gameLevel) {
        this.numberOfPlayers = numberOfPlayers;
        this.gameLevel = gameLevel;
    }

    /**
     * @return the number of players
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * @return the game level
     */
    public int getGameLevel() {return gameLevel;}

    /**
     * @return the message associated with the creation of a new game
     */
    @Override
    public String getMessage() {
        return "Create game, num of players: " + numberOfPlayers;
    }

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
        return visitor.visit(this, client);
    }

}

