package it.polimi.ingsw.net.socket.events.clientevents.cardevents;

import it.polimi.ingsw.net.socket.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.socket.events.clientevents.ClientEventType;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.server.clienteventhandler.ClientVisitor;

public class PlotGoodEvent extends ClientEvent {

    private boolean decision;
    private int[] coordinates;
    private int positionInLoad;

    public PlotGoodEvent(boolean decision, int[] coordinates, int positionInLoad) {
        super(ClientEventType.SEND_INFO_EVENT);
        this.decision = decision;
        this.coordinates = coordinates;
        this.positionInLoad = positionInLoad;
    }

    public boolean getDecision() {
        return decision;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public int getPositionInLoad() {
        return positionInLoad;
    }

    @Override
    public ServerEvent call(ClientVisitor visitor) {
        return visitor.visit(this);
    }
}
