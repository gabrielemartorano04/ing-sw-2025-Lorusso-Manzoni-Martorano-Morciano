package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

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
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }

    public String getNickname() {
        return nickname;
    }
}
