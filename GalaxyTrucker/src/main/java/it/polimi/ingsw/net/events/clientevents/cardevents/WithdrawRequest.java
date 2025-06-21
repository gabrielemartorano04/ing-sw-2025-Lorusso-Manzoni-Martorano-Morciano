package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class WithdrawRequest extends ClientEvent {
    private final String nickname;
    public WithdrawRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
        return visitor.visit(this);
    }
}
