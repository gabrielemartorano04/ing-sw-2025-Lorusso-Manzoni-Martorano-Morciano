package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

import java.util.Map;

public class CurrentCondition extends CardEvent{
    private final Condition condition;
    private final Map<String, Integer> track;
    private final SpaceShip defTruckerShip;

    public CurrentCondition(boolean forAll, ParserState parserState, String nickname, Condition condition, Map<String, Integer> track, SpaceShip defTruckerShip) {
        super(ServerEventType.CONDITION, forAll, parserState, nickname);
        this.condition = condition;
        this.track=track;
        this.defTruckerShip=defTruckerShip;
    }

    public Condition getCondition() {
        return condition;
    }

    public Map<String, Integer> getTrack() {
        return track;
    }
    public SpaceShip getDefTruckerShip() {
        return defTruckerShip;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
