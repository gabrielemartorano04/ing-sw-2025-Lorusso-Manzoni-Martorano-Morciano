package it.polimi.ingsw.net.socket.events.serverevents.flightevents;

import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

public class BooleanRequest extends ServerEvent {
    private String question;
    private int userId;

    public BooleanRequest(ServerEventType eventType, String question, int Id) {
        super(ServerEventType.REQUEST, false);
        this.question = question;
        this.userId = Id;
    }
    @Override
    public String getMessage() {
        return question;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }


}
