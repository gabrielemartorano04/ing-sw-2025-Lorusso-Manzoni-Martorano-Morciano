package it.polimi.ingsw.net.events.clientevents;

import it.polimi.ingsw.net.Client;
import it.polimi.ingsw.net.VirtualServer;
import it.polimi.ingsw.net.events.serverevents.ServerEvent;

import java.util.ArrayList;

public class SendAlienCoord extends ClientEvent {
    private ArrayList<int[]> coord;
    public SendAlienCoord(ArrayList<int[]> coord) {
        this.coord = coord;
    }
    public ArrayList<int[]> getCoord() {
        return coord;
    }
    @Override
    public String getMessage() {
        return "Send AlienCoord";
    }

    @Override
    public ServerEvent call(VirtualServer visitor, Client client) {
         return visitor.visit(this, client);
    }

}
