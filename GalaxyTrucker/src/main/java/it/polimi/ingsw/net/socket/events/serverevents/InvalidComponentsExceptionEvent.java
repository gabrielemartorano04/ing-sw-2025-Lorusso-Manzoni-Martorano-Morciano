package it.polimi.ingsw.net.socket.events.serverevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.cardevents.CardEvent;

public class InvalidComponentsExceptionEvent extends CardEvent {

    private String message;

    public InvalidComponentsExceptionEvent(String message, boolean forAll) {
        //TODO sistemare ingressi del super
        super(ServerEventType.INVALID_COMPONENTS_ERROR, forAll, null, null);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
