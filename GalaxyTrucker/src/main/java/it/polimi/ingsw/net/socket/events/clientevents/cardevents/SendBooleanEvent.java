package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class SendBooleanEvent extends ClientEvent {
    private boolean answer;
    public SendBooleanEvent(final boolean answer) {
        super(ClientEventType.SEND_INFO_EVENT, true);
        this.answer = answer;
    }
    public boolean getAnswer() {
        return answer;
    }
    @Override
    public String getMessage() {
        return "Sent answer " + answer;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
