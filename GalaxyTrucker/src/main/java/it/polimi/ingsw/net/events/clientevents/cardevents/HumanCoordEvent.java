package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

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
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this);
    }

    public String getNickname() {
        return nickname;
    }
}
