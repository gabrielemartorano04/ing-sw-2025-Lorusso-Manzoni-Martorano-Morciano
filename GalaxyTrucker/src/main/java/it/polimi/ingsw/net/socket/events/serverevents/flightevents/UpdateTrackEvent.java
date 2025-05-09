package it.polimi.ingsw.net.socket.events.serverevents.flightevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;
import java.util.Map;

public class UpdateTrackEvent extends ServerEvent {
    private String message;
    private Map<String, Integer> track;

    public UpdateTrackEvent(ArrayList<Trucker> truckers) {
        super(ServerEventType.UPDATE_TRACK, false);
        message = "Track updated";
        for (Trucker trucker : truckers){
            track.put(trucker.getNickname(), trucker.getPosition()%24);
        }
    }

    @Override
    public String toString() {
        return message;
    }

    public Map<String, Integer> getTrack() {
        return track;
    }

    @Override
    public void call (ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
