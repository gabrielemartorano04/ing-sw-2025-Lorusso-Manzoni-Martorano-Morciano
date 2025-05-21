package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class PlanetDecisionEvent extends ClientEvent {

    private int index;
    private String nickname;

    public PlanetDecisionEvent(int index, String nickname) {
        this.index = index;
        this.nickname = nickname;
    }

    public int getIndex() {
        return index;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
