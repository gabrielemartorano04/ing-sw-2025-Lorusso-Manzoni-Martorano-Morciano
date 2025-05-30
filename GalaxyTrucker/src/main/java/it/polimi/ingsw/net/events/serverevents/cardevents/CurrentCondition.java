package it.polimi.ingsw.net.events.serverevents.cardevents;

import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.net.ServerEventHandlerVisitor;
import it.polimi.ingsw.net.events.serverevents.ServerEventType;
import it.polimi.ingsw.view.ParserState;

public class CurrentCondition extends CardEvent{
    private final Condition condition;

    public CurrentCondition(boolean forAll, ParserState parserState, String nickname, Condition condition) {
        super(ServerEventType.CONDITION, forAll, parserState, nickname);
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void call(ServerEventHandlerVisitor visitor) {
        visitor.visit(this);
    }
}
