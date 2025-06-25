package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;

import java.io.Serializable;

/**
 * Represents an abstract server event sent from the server to the client.
 * ServerEvent serves as a base class for defining various specific types of events
 * that a server can generate during its interaction with the client. Each event provides
 * a method for processing it by the clients and a default or customized message describing the event
 */
public abstract class ServerEvent implements Serializable {

    /**
     * Specify if the message is for a player or everyone
     */
    private boolean forAll;

    /**
     * Event constructor
     * @param forAll specify if the game is for a player of everyone
     */
    public ServerEvent(boolean forAll) {
        this.forAll=forAll;}

    /**
     * @return if the message is for a player or everyone
     */
    public boolean isForAll() {
        return forAll;
    }

    /**
     * @return the message associated with the event
     */
    public String getMessage() {return "";};

    /**
     * This method calls the client method that manages this event reception
     * @param visitor client class that manages server events
     */
    public abstract void call(ServerEventHandlerVisitor visitor);
}
