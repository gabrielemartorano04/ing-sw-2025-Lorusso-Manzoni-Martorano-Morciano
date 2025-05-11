package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;


import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.io.IOException;


/**
 * Event from server to client. It's time for all the players to start building their spaceships
 */


public class StartBuildingEvent extends ServerEvent {
    private String message;
    private final SpaceShip spaceShip;

    public StartBuildingEvent(String username, SpaceShip spaceShip) {
        super(ServerEventType.START_BUILDING, true);
        message = username + " logged\nIt's time to start building";
        this.spaceShip = spaceShip;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

}
