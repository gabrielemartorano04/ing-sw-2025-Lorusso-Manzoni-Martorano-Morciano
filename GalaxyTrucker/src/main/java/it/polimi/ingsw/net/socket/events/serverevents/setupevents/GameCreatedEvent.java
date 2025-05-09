package it.polimi.ingsw.net.socket.events.serverevents.setupevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class GameCreatedEvent extends ServerEvent {
    private String message;
    public GameCreatedEvent(String message) {
        super(ServerEventType.GAME_CREATED, false);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
