package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.net.control.ServerEventHandlerVisitor;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

public class CurrentCondition extends CardEvent{
    private final Condition condition;
    private final Map<String, Integer> track;
    private final ArrayList<Trucker> truckersRemoved;

    public CurrentCondition(boolean forAll, ParserState parserState, String nickname, Condition condition, Map<String, Integer> track, ArrayList<Trucker> deadTruckersName, ArrayList<Trucker> truckers) {
        super(forAll, parserState, nickname, truckers);
        this.condition = condition;
        this.track=track;
        this.truckersRemoved=deadTruckersName;
    }

    public Condition getCondition() {
        return condition;
    }

    public Map<String, Integer> getTrack() {
        return track;
    }

    public ArrayList<Trucker> getTruckersRemoved(){
        return this.truckersRemoved;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
