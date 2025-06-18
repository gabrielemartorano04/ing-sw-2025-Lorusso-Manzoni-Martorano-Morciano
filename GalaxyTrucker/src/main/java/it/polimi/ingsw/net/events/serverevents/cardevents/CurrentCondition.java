package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.Trucker;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

public class CurrentCondition extends CardEvent{
    private final Condition condition;
    private final Map<String, Integer> track;
    private final Trucker weakestTrucker;
    private final ArrayList<Trucker> truckersRemoved;

    public CurrentCondition(boolean forAll, ParserState parserState, String nickname, Condition condition, Map<String, Integer> track, Trucker weakestTrucker, ArrayList<Trucker> deadTruckersName) {
        super(ServerEventType.CONDITION, forAll, parserState, nickname);
        this.condition = condition;
        this.track=track;
        this.weakestTrucker =weakestTrucker;
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
    public Trucker getWeakestTrucker() {
        return weakestTrucker;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
