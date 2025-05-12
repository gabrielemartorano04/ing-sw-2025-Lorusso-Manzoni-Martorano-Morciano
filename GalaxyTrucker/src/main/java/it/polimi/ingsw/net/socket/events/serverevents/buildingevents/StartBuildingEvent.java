package it.polimi.ingsw.net.socket.events.serverevents.buildingevents;


import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Event from server to client. It's time for all the players to start building their spaceships
 */


public class StartBuildingEvent extends ServerEvent {
    private String message;
    private
    ArrayList<Card>[] stacks;
    private final SpaceShip spaceShip;

    public StartBuildingEvent(String username, SpaceShip spaceShip, ArrayList<Card>[] stacks) {
        super(ServerEventType.START_BUILDING, true);
        message = username + " logged\nIt's time to start building";
        this.spaceShip = spaceShip;
        this.stacks = stacks;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public ArrayList<Card>[] getStacks() {return stacks;}

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

}
