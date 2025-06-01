package it.polimi.ingsw.net.events.serverevents.setupevents;

import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;

import java.util.ArrayList;

public class LobbyEvent extends ServerEvent {
    public String message;
    private ArrayList<String> truckers;
    public LobbyEvent( ArrayList<String> truckers) {
        super(ServerEventType.LOBBY, true);
        message = "Current players: " + truckers;
        this.truckers = truckers;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ArrayList<String> getTruckers() {
        return truckers;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
