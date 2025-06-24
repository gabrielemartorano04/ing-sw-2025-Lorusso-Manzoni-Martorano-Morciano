package it.polimi.ingsw.net.events.serverevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;

import java.io.Serializable;

/**
 * An event will be used to communicate between server and client, we will create an event with the view and then
 * send it using RMI or socket based on client's choice
 */

public abstract class ServerEvent implements Serializable {
    private boolean forAll;

    public ServerEvent(boolean forAll) {
        this.forAll=forAll;
    }
    public String getMessage() {return "";};

    public boolean isForAll() {
        return forAll;
    }

    public abstract void call(ServerEventHandlerVisitor visitor);
}
