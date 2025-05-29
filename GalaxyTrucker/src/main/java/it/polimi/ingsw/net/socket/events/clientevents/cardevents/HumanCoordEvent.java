package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

import java.util.ArrayList;

public class HumanCoordEvent extends ClientEvent {

    private ArrayList<int[]> humanCoord;
    private String nickname;
    public HumanCoordEvent(ArrayList<int[]> humanCoord, String nickname) {
        this.humanCoord = humanCoord;
        this.nickname = nickname;
    }

    public ArrayList<int[]> getHumanCoord() {
        return humanCoord;
    }

    @Override
    public ServerEvent call(VirtualServer visitor) {
        return visitor.visit(this);
    }

    public String getNickname() {
        return nickname;
    }
}
