package it.polimi.ingsw.net.events.clientevents.buildingevents;


import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

public class KeepTileEvent extends ClientEvent {
    private Component component;
    public KeepTileEvent(Component component) {
        this.component = component;
    }
    public Component getComponent() {
        return component;
    }
    @Override
    public String getMessage() {
        return "I want to keep the selected tile";
    }

    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
