package it.polimi.ingsw.param.results;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.Condition;
import it.polimi.ingsw.model.cards.Effect;

import java.util.Map;

public class MapResult implements Result {
    private final CardState state;
    private final Map<Condition,Effect> result;
    public MapResult(CardState state, Map<Condition, Effect> result) {
        this.state = state;
        this.result = result;
    }
    public CardState getState() {
        return state;
    }
    public Map<Condition, Effect> getResult() {
        return result;
    }
}
