package it.polimi.ingsw.param.results;

import it.polimi.ingsw.model.cards.CardState;

public class IntResult implements Result {
    private final CardState state;
    private final int result;
    public IntResult(CardState state, int result) {
        this.state = state;
        this.result = result;
    }
    public CardState getState() {
        return state;
    }
    public int getResult() {
        return result;
    }
}
