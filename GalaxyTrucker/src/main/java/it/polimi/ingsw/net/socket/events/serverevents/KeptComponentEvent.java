package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.components.Component;

import java.util.ArrayList;

public class KeptComponentEvent extends ServerEvent{
    private String message;
    ArrayList<Component> kept;

    public KeptComponentEvent(ArrayList<Component> kept) {
        super(ServerEventType.KEPT_COMPONENT, false);
        this.kept = kept;
        message = "Kept component " + kept;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ArrayList<Component> getKept() {
        return kept;
    }
}
