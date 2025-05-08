package it.polimi.ingsw.net.socket.events.clientevents;


import it.polimi.ingsw.model.components.Component;

public class KeepTileEvent extends ClientEvent {
    private Component component;
    public KeepTileEvent(Component component) {
        super(ClientEventType.KEEP_TILE, false);
        this.component = component;
    }
    public Component getComponent() {
        return component;
    }
    @Override
    public String getMessage() {
        return "I want to keep the selected tile";
    }
}
