package it.polimi.ingsw.net.socket.events.clientevents;

import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

import java.util.ArrayList;

public class SendAlienCoord extends ClientEvent {
    private ArrayList<int[]> coord;
    public SendAlienCoord(ArrayList<int[]> coord) {
        super(ClientEventType.SEND_INFO_EVENT);
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
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }

}
