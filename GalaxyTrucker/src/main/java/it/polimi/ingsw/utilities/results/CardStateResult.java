package it.polimi.ingsw.utilities.results;

import it.polimi.ingsw.model.cards.CardState;

public class CardStateResult implements Result{
    private final CardState state;
    public CardStateResult(CardState state) {
        this.state = state;
    }
    public CardState getState() {
        return state;
    }
    public CardState getResult() {return state;}
}
