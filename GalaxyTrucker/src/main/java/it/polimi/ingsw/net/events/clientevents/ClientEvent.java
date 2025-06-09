package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.io.Serializable;

/**
 * An event will be used to communicate between server and client, we will create an event with the view and then
 * send it using RMI or socket based on client's choice
 */

public abstract class ClientEvent implements Serializable {

    private String message;

    public ClientEvent() {
        message="";
    }

    public String getMessage() {
        return message;
    }


    public abstract ServerEvent call(VirtualServer visitor, Client client);
}
