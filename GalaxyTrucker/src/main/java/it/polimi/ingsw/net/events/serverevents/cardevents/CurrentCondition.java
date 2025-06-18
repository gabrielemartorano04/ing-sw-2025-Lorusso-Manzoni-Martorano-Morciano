package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

import java.util.ArrayList;
import java.util.Map;

public class CurrentCondition extends CardEvent{
    private final Condition condition;
    private final Map<String, Integer> track;
    private final SpaceShip weakestTruckerShip;
    private final ArrayList<String> truckersRemoved;

    public CurrentCondition(boolean forAll, ParserState parserState, String nickname, Condition condition, Map<String, Integer> track, SpaceShip defTruckerShip, ArrayList<String> deadTruckersName) {
        super(ServerEventType.CONDITION, forAll, parserState, nickname);
        this.condition = condition;
        this.track=track;
        this.weakestTruckerShip =defTruckerShip;
        this.truckersRemoved=deadTruckersName;
    }

    public Condition getCondition() {
        return condition;
    }

    public Map<String, Integer> getTrack() {
        return track;
    }

    public ArrayList<String> getTruckersRemoved(){
        return this.truckersRemoved;
    }
    public SpaceShip getWeakestTruckerShip() {
        return weakestTruckerShip;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
