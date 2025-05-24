package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class BuildCompleteEvent extends ServerEvent {
    private String message;
    private SpaceShip ship;
    public BuildCompleteEvent(SpaceShip ship) {
        super(ServerEventType.END_BUILDING, true);
        message = "All the ships are checked and the building phase is complete";
        this.ship = ship;
    }

    public SpaceShip getShip() {
        return ship;
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
