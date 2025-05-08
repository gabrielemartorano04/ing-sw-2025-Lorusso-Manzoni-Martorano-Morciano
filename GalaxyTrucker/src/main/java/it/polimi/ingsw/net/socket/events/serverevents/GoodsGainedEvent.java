package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.model.components.Component;

import java.util.ArrayList;

public class GoodsGainedEvent extends ServerEvent{
    private String message;
    ArrayList<Component> components;

    public GoodsGainedEvent(ArrayList<Component> components) {
        super(ServerEventType.GOOD_GAINED, false);
        message = "Available components gained";
        this.components = components;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }
}
