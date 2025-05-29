package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.VirtualServer;

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
    public ServerEvent call(VirtualServer visitor) {
        return visitor.visit(this);
    }

}
