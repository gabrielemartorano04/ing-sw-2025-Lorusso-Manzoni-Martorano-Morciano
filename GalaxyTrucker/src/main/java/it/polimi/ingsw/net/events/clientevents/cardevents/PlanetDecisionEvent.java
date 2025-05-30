package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

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
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }
}
