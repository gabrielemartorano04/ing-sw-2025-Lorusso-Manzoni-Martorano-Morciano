package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

public class RemoveBatteriesEvent extends ClientEvent {

    private ArrayList<int[]> coordinatesToRemoveBatteries;
    private String nickname;
    public RemoveBatteriesEvent(ArrayList<int[]> coordinatesToRemoveBatteries,String nickname) {

        this.coordinatesToRemoveBatteries = coordinatesToRemoveBatteries;
        this.nickname = nickname;
    }

    public ArrayList<int[]> getCoordinatesToRemoveBatteries() {
        return coordinatesToRemoveBatteries;
    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this);
    }

    public String getNickname() {
        return nickname;
    }
}
