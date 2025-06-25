package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

public class CardFinished extends CardEvent {
    private final ArrayList<Trucker> truckersRemoved;
    Map<String, Integer> track;
    public CardFinished(boolean forAll, ArrayList<Trucker> truckerList, ArrayList<Trucker> truckerRemoved, Map<String, Integer> track) {
        super(forAll, ParserState.DRAW_CARD, null, truckerList);
        this.truckersRemoved = truckerRemoved;
        this.track = track;
        for(Trucker trucker : truckerList){
            System.out.println("trucker " + trucker.getNickname() + " in pos: " + trucker.getPosition());
        }
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
