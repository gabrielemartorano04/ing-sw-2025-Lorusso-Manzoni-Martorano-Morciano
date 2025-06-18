package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

public class CardFinished extends CardEvent {
    private final ArrayList<Trucker> truckersList;
    private final ArrayList<Trucker> truckersRemoved;
    Map<String, Integer> track;
    public CardFinished(boolean forAll, ArrayList<Trucker> truckerList, ArrayList<Trucker> truckerRemoved, Map<String, Integer> track) {
        super(ServerEventType.CARD_FINISHED, forAll, ParserState.DRAW_CARD, null);
        this.truckersList = truckerList;
        this.truckersRemoved = truckerRemoved;
        this.track = track;
        for(Trucker trucker : truckerList){
            System.out.println("trucker " + trucker.getNickname() + " in pos: " + trucker.getPosition());
        }
    }

    public ArrayList<Trucker> getTruckersList() {
        return truckersList;
    }

    public ArrayList<Trucker> getTruckersRemoved(){
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
