package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;

import java.rmi.ServerError;

public class NotYourTurnError extends ServerEvent {
    private final String message;
    public NotYourTurnError(String message) {
        super(ServerEventType.NOT_YOUR_TURN, false);
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

}
