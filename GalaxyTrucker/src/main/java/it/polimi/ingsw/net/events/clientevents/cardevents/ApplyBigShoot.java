package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.control.Client;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.control.VirtualServer;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;

public class ApplyBigShoot extends ClientEvent {

    /**
     * Event constructor
     */
    public ApplyBigShoot() {}

    /**
     * This method calls the server method that manages this event reception
     * @param visitor server class that manages client events
     * @param client client that sent the event
     * @return server event response based on client event reception
     */
    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
        return visitor.visit(this);
    }
}
