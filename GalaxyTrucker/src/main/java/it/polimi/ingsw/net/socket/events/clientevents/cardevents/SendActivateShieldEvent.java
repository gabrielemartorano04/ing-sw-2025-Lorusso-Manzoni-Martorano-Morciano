package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class SendActivateShieldEvent extends ClientEvent {

    private boolean decision;
    private int x, y;

    public SendActivateShieldEvent(boolean decision, int x, int y) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.decision = decision;
        this.x = x;
        this.y = y;
    }

    public boolean getDecision() {
        return decision;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
