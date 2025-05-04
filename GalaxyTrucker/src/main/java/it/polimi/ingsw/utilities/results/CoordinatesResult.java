package it.polimi.ingsw.utilities.results;

import it.polimi.ingsw.model.cards.CardState;

public class CoordinatesResult implements Result {
    private final CardState state;
    private final int[] result;
    public CoordinatesResult(CardState state, int[] result) {
        this.state = state;
        this.result = result;
    }
    public CardState getState() {
        return state;
    }
    public int[] getResult() {
        return result;
    }
}
