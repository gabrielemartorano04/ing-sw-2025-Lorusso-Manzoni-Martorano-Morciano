package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class WithdrawRequest extends ClientEvent {
    private final String nickname;
    private final boolean choice;
    public WithdrawRequest(String nickname,boolean choice) {
        this.nickname = nickname;
        this.choice = choice;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean getChoice(){
        return choice;
    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
        return visitor.visit(this);
    }
}
