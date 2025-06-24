package it.polimi.ingsw.net.events.serverevents.buildingevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

public class BuildCompleteEvent extends ServerEvent {
    private String message;
    private ArrayList<Trucker> truckers;
    public BuildCompleteEvent(ArrayList<Trucker> truckers) {
        super(true);
        message = "All the ships are checked and the building phase is complete";
        this.truckers = truckers;
    }

    public ArrayList<Trucker> getTruckers() {
        return truckers;
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
