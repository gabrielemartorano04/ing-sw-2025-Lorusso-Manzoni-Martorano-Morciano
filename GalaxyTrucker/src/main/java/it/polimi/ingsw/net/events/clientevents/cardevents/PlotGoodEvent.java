package it.polimi.ingsw.net.events.clientevents.cardevents;

import it.polimi.ingsw.net.events.clientevents.ClientEvent;
import it.polimi.ingsw.net.VirtualServer;

public class PlotGoodEvent extends ClientEvent {

    private boolean decision;
    private int[] coordinates;
    private int positionInLoad;
    private String nickname;
    public PlotGoodEvent(boolean decision, int[] coordinates, int positionInLoad, String nickname) {

        this.decision = decision;
        this.coordinates = coordinates;
        this.positionInLoad = positionInLoad;
        this.nickname = nickname;
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
    public void call(VirtualServer visitor) {
         visitor.visit(this);
    }

    public String getNickname() {
        return nickname;
    }
}
