package it.polimi.ingsw.param.results;

import it.polimi.ingsw.model.cards.CardState;

public class CardStateResult implements Result{
    private final CardState state;
    private final CardState result;
    public CardStateResult(CardState state, CardState result) {
        this.state = state;
        this.result = result;
    }
    public CardState getState() {
        return state;
    }
    public CardState getResult() {
        return result;
    }
}
