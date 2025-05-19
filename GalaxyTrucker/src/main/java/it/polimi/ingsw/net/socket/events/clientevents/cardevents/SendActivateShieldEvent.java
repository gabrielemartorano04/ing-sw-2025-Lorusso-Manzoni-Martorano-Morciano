package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class SendActivateShieldEvent extends ClientEvent {

    private boolean decision;
    private int[] coordShield;
    private int[] coordBS;

    public SendActivateShieldEvent(boolean decision, int[] coordShield, int[] coordBS ) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.decision = decision;
        this.coordShield = coordShield;
        this.coordBS = coordBS;
    }

    public boolean getDecision() {
        return decision;
    }

    public int[] getCoordShield() {
        return coordShield;
    }
    public int[] getCoordBS() {
        return coordBS;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
