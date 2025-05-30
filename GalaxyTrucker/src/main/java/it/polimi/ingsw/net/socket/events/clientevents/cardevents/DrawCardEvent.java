package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

public class DrawCardEvent extends ClientEvent {
    private String currNicknme;
    public DrawCardEvent(String currNicknme) {
        this.currNicknme=currNicknme;
    }

    public String getCurrNickname() {
        return currNicknme;
    }

    @Override
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
