package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class GainCosmicCreditsResponseEvent extends ClientEvent {
    private boolean answer;
    public GainCosmicCreditsResponseEvent(boolean answer) {
        this.answer = answer;
    }

    public boolean getAnswer() {
        return answer;
    }


    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
