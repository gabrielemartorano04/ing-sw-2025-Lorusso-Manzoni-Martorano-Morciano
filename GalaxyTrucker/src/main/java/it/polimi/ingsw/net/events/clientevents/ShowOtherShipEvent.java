package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class ShowOtherShipEvent extends ClientEvent{
    private String nickname;
    public ShowOtherShipEvent(String nickname) {
        this.nickname = nickname;
    }
    public String getNicknameRequested() {
        return nickname;
    }
    @Override
    public String getMessage() {
        return "I want to see "+nickname+"'s spaceship";
    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this);
    }
}
