package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

public class BuildOptionsEvent extends ServerEvent{
    private String message;

    public BuildOptionsEvent() {
        super(ServerEventType.BUILD_OPTIONS, false);
        message = "Build options requested";
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
