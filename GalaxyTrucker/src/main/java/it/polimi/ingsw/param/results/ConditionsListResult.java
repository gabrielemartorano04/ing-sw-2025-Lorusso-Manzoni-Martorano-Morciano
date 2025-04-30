package it.polimi.ingsw.param.results;

import it.polimi.ingsw.model.cards.CardState;
import it.polimi.ingsw.model.cards.Condition;

import java.util.ArrayList;

public class ConditionsListResult implements Result{
    private final CardState state;
    private final ArrayList<Condition> result;
    public ConditionsListResult(CardState state, ArrayList<Condition> result) {
        this.state = state;
        this.result = result;
    }
    public CardState getState() {
        return state;
    }
    public ArrayList<Condition> getResult() {
        return result;
    }
}


