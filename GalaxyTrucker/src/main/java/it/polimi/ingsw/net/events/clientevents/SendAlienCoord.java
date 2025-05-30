package it.polimi.ingsw.net.events.clientevents;

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
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }

}
