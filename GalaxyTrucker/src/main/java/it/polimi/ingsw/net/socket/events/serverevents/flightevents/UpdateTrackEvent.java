package it.polimi.ingsw.net.socket.events.serverevents.flightevents;

import it.polimi.ingsw.controller.TurnManager;
import it.polimi.ingsw.net.socket.client.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEvent;
import it.polimi.ingsw.net.socket.events.serverevents.ServerEventType;

import java.util.ArrayList;
import java.util.Map;

public class UpdateTrackEvent extends ServerEvent {
    private String message;
    private Map<String, Integer> track;

    public UpdateTrackEvent(ArrayList<TurnManager.TruckerController> truckerControllers) {
        super(ServerEventType.UPDATE_TRACK, false);
        message = "Track updated";
        for (TurnManager.TruckerController truckerController : truckerControllers){
            track.put(truckerController.getNickname(), truckerController.getPosition()%24);
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
