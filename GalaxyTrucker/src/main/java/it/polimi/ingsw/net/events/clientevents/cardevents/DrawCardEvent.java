package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

public class DrawCardEvent extends ClientEvent {
    private String currNicknme;
    public DrawCardEvent(String currNicknme) {
        this.currNicknme=currNicknme;
    }

    public String getCurrNickname() {
        return currNicknme;
    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this);
    }
}
