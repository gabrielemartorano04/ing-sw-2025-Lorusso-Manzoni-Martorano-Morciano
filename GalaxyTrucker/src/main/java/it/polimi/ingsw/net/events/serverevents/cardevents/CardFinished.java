package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

public class CardFinished extends CardEvent {
    private final ArrayList<Trucker> truckersList;
    private final ArrayList<String> truckersRemoved;
    Map<String, Integer> track;
    public CardFinished(boolean forAll, ArrayList<Trucker> truckerList, ArrayList<String> truckerRemoved, Map<String, Integer> track) {
        super(ServerEventType.CARD_FINISHED, forAll, ParserState.DRAW_CARD, null);
        this.truckersList = truckerList;
        this.truckersRemoved = truckerRemoved;
        this.track = track;
    }

    public ArrayList<Trucker> getTruckersList() {
        return truckersList;
    }

    public ArrayList<String> getTruckersRemoved(){
        return truckersRemoved;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }

    public Map<String, Integer> getTrack() {
        return track;
    }
}
